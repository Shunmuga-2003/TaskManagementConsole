package com.taskmanager.service;

import com.taskmanager.exception.TaskException;
import com.taskmanager.model.*;
import com.taskmanager.observer.TaskObserver;
import com.taskmanager.strategy.SortStrategy;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskService {
    private static TaskService instance;

    private final Map<String, Task> tasks = new ConcurrentHashMap<>();
    private final List<TaskObserver> observers = new ArrayList<>();

    private TaskService() {}

    public static synchronized TaskService getInstance() {
        if (instance == null) instance = new TaskService();
        return instance;
    }

    // ── Observer management ───────────────────────────────────
    public void addObserver(TaskObserver observer) { observers.add(observer); }

    private void notifyCreated(Task t, String actor)  { observers.forEach(o -> o.onTaskCreated(t, actor)); }
    private void notifyUpdated(Task t, String field, String ov, String nv, String actor) {
        observers.forEach(o -> o.onTaskUpdated(t, field, ov, nv, actor));
    }
    private void notifyStatus(Task t, String ov, String nv, String actor) {
        observers.forEach(o -> o.onTaskStatusChanged(t, ov, nv, actor));
    }
    private void notifyDeleted(Task t, String actor)  { observers.forEach(o -> o.onTaskDeleted(t, actor)); }
    private void notifyComment(Task t, String c, String actor) { observers.forEach(o -> o.onCommentAdded(t, c, actor)); }

    // ── CRUD ──────────────────────────────────────────────────
    public Task createTask(String title, String description, LocalDate dueDate,
                           Priority priority, String creatorId, String assigneeId) {
        Task task = new Task(title, description, dueDate, priority, creatorId);
        if (assigneeId != null) task.setAssigneeId(assigneeId);
        tasks.put(task.getId(), task);
        notifyCreated(task, creatorId);
        return task;
    }

    public Task getTaskById(String id) {
        Task task = tasks.get(id);
        if (task == null) throw new TaskException("Task not found with id: " + id);
        return task;
    }

    public void updateTitle(String taskId, String newTitle, String actorId) {
        Task task = getTaskById(taskId);
        String old = task.getTitle();
        task.setTitle(newTitle);
        notifyUpdated(task, "Title", old, newTitle, actorId);
    }

    public void updateDescription(String taskId, String newDesc, String actorId) {
        Task task = getTaskById(taskId);
        String old = task.getDescription();
        task.setDescription(newDesc);
        notifyUpdated(task, "Description", old, newDesc, actorId);
    }

    public void updateDueDate(String taskId, LocalDate newDate, String actorId) {
        Task task = getTaskById(taskId);
        String old = task.getDueDate() == null ? "N/A" : task.getDueDate().toString();
        task.setDueDate(newDate);
        notifyUpdated(task, "DueDate", old, newDate.toString(), actorId);
    }

    public void updatePriority(String taskId, Priority newPriority, String actorId) {
        Task task = getTaskById(taskId);
        String old = task.getPriority().name();
        task.setPriority(newPriority);
        notifyUpdated(task, "Priority", old, newPriority.name(), actorId);
    }

    public void assignTask(String taskId, String newAssigneeId, String actorId) {
        Task task = getTaskById(taskId);
        String old = task.getAssigneeId() == null ? "none" : task.getAssigneeId();
        task.setAssigneeId(newAssigneeId);
        notifyUpdated(task, "Assignee", old, newAssigneeId, actorId);
    }

    public void changeStatus(String taskId, TaskStatus newStatus, String actorId) {
        Task task = getTaskById(taskId);
        String old = task.getStatus().name();
        task.transitionTo(newStatus);
        notifyStatus(task, old, newStatus.name(), actorId);
    }

    public void deleteTask(String taskId, String actorId) {
        Task task = getTaskById(taskId);
        tasks.remove(taskId);
        notifyDeleted(task, actorId);
    }

    // ── Comments & Tags ───────────────────────────────────────
    public void addComment(String taskId, String content, String authorId) {
        Task task = getTaskById(taskId);
        Comment comment = new Comment(taskId, authorId, content);
        task.addComment(comment);
        notifyComment(task, content, authorId);
    }

    public void addTag(String taskId, Tag tag) {
        getTaskById(taskId).addTag(tag);
    }

    public void removeTag(String taskId, String tagName) {
        getTaskById(taskId).removeTag(tagName);
    }

    // ── Queries ───────────────────────────────────────────────
    public List<Task> getAllTasks() { return List.copyOf(tasks.values()); }

    public List<Task> getTasksByCreator(String userId) {
        return tasks.values().stream()
            .filter(t -> userId.equals(t.getCreatorId())).toList();
    }

    public List<Task> getTasksByAssignee(String userId) {
        return tasks.values().stream()
            .filter(t -> userId.equals(t.getAssigneeId())).toList();
    }

    public List<Task> getDelayedTasks() {
        return tasks.values().stream().filter(Task::isDelayed).toList();
    }

    public List<Task> searchTasks(String keyword, TaskStatus filterStatus,
                                   Priority filterPriority, String filterTag,
                                   SortStrategy sortStrategy) {
        List<Task> result = tasks.values().stream()
            .filter(t -> keyword == null || keyword.isBlank()
                || t.getTitle().toLowerCase().contains(keyword.toLowerCase())
                || t.getDescription().toLowerCase().contains(keyword.toLowerCase()))
            .filter(t -> filterStatus == null || t.getStatus() == filterStatus)
            .filter(t -> filterPriority == null || t.getPriority() == filterPriority)
            .filter(t -> filterTag == null || filterTag.isBlank()
                || t.getTags().stream().anyMatch(tag -> tag.getName().equalsIgnoreCase(filterTag)))
            .collect(Collectors.toList());

        return sortStrategy != null ? sortStrategy.sort(result) : result;
    }

    // ── Distribution / Reports ────────────────────────────────
    public Map<Priority, Long> countByPriority() {
        return tasks.values().stream()
            .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
    }

    public Map<TaskStatus, Long> countByStatus() {
        return tasks.values().stream()
            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }

    public Map<String, Long> countByTag() {
        Map<String, Long> result = new LinkedHashMap<>();
        tasks.values().forEach(t -> t.getTags().forEach(tag -> {
            result.merge(tag.getName(), 1L, Long::sum);
        }));
        return result;
    }
}
