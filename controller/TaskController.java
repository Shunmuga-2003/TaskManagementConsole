package com.taskmanager.controller;

import com.taskmanager.model.*;
import com.taskmanager.observer.AuditLogObserver;
import com.taskmanager.service.*;
import com.taskmanager.strategy.*;
import com.taskmanager.util.DateUtil;
import com.taskmanager.view.ConsoleView;
import com.taskmanager.view.TaskView;

import java.time.LocalDate;
import java.util.List;

public class TaskController {
    private final TaskService    taskService    = TaskService.getInstance();
    private final UserService    userService    = UserService.getInstance();
    private final TagService     tagService     = TagService.getInstance();
    private final AuthService    authService    = AuthService.getInstance();
    private final AuditLogObserver auditLog;

    public TaskController(AuditLogObserver auditLog) {
        this.auditLog = auditLog;
    }

    public void run() {
        boolean back = false;
        while (!back) {
            int choice = TaskView.showTaskMenu();
            try {
                switch (choice) {
                    case 1  -> createTask();
                    case 2  -> viewMyCreatedTasks();
                    case 3  -> viewMyAssignedTasks();
                    case 4  -> updateTask();
                    case 5  -> deleteTask();
                    case 6  -> changeStatus();
                    case 7  -> assignTask();
                    case 8  -> searchTasks();
                    case 9  -> addComment();
                    case 10 -> manageTags();
                    case 11 -> viewDelayedTasks();
                    case 12 -> viewAuditLog();
                    case 13 -> back = true;
                }
            } catch (Exception e) {
                ConsoleView.printError(e.getMessage());
                ConsoleView.pressEnterToContinue();
            }
        }
    }

    // ── 1. Create Task ────────────────────────────────────────
    private void createTask() {
        ConsoleView.printHeader("CREATE TASK");
        String title = ConsoleView.prompt("Title");
        String desc  = ConsoleView.prompt("Description");
        String dateStr = ConsoleView.prompt("Due Date (yyyy-MM-dd)");
        LocalDate dueDate = DateUtil.parse(dateStr);

        TaskView.displayPriorityOptions();
        String prioInput = ConsoleView.prompt("Priority (1-4)");
        Priority priority = Priority.fromString(prioInput);

        String assigneeUsername = ConsoleView.promptOptional("Assign to username");
        String assigneeId = null;
        if (!assigneeUsername.isBlank()) {
            assigneeId = userService.findByUsername(assigneeUsername)
                .orElseThrow(() -> new RuntimeException("User '" + assigneeUsername + "' not found."))
                .getId();
        }

        Task task = taskService.createTask(title, desc, dueDate, priority,
            authService.getCurrentUserId(), assigneeId);
        ConsoleView.printSuccess("Task created: [" + task.getId().substring(0, 8) + "] " + task.getTitle());
        ConsoleView.pressEnterToContinue();
    }

    // ── 2. My Created Tasks ───────────────────────────────────
    private void viewMyCreatedTasks() {
        ConsoleView.printHeader("MY CREATED TASKS");
        List<Task> tasks = taskService.getTasksByCreator(authService.getCurrentUserId());
        if (tasks.isEmpty()) { ConsoleView.printInfo("No tasks found."); }
        else { TaskView.displayTaskList(tasks); }
        ConsoleView.pressEnterToContinue();
    }

    // ── 3. Assigned Tasks ─────────────────────────────────────
    private void viewMyAssignedTasks() {
        ConsoleView.printHeader("TASKS ASSIGNED TO ME");
        List<Task> tasks = taskService.getTasksByAssignee(authService.getCurrentUserId());
        if (tasks.isEmpty()) { ConsoleView.printInfo("No tasks assigned to you."); }
        else { TaskView.displayTaskList(tasks); }
        ConsoleView.pressEnterToContinue();
    }

    // ── 4. Update Task ────────────────────────────────────────
    private void updateTask() {
        String taskId = resolveTaskId();
        Task task = taskService.getTaskById(taskId);
        TaskView.displayTaskDetail(task);

        int choice = TaskView.showUpdateMenu();
        String actorId = authService.getCurrentUserId();
        switch (choice) {
            case 1 -> { String t = ConsoleView.prompt("New Title"); taskService.updateTitle(taskId, t, actorId); ConsoleView.printSuccess("Title updated."); }
            case 2 -> { String d = ConsoleView.prompt("New Description"); taskService.updateDescription(taskId, d, actorId); ConsoleView.printSuccess("Description updated."); }
            case 3 -> { String dt = ConsoleView.prompt("New Due Date (yyyy-MM-dd)"); taskService.updateDueDate(taskId, DateUtil.parse(dt), actorId); ConsoleView.printSuccess("Due date updated."); }
            case 4 -> { TaskView.displayPriorityOptions(); String p = ConsoleView.prompt("New Priority (1-4)"); taskService.updatePriority(taskId, Priority.fromString(p), actorId); ConsoleView.printSuccess("Priority updated."); }
            case 5 -> {}
        }
        ConsoleView.pressEnterToContinue();
    }

    // ── 5. Delete Task ────────────────────────────────────────
    private void deleteTask() {
        String taskId = resolveTaskId();
        Task task = taskService.getTaskById(taskId);
        ConsoleView.printInfo("About to delete: \"" + task.getTitle() + "\"");
        String confirm = ConsoleView.prompt("Type YES to confirm");
        if ("YES".equalsIgnoreCase(confirm)) {
            taskService.deleteTask(taskId, authService.getCurrentUserId());
            ConsoleView.printSuccess("Task deleted.");
        } else {
            ConsoleView.printInfo("Deletion cancelled.");
        }
        ConsoleView.pressEnterToContinue();
    }

    // ── 6. Change Status ──────────────────────────────────────
    private void changeStatus() {
        String taskId = resolveTaskId();
        Task task = taskService.getTaskById(taskId);
        ConsoleView.printInfo("Current status: " + task.getStatus() + " | Allowed: " + task.allowedTransitions());
        TaskView.displayStatusOptions();
        String statusInput = ConsoleView.prompt("New Status (1-5)");
        TaskStatus newStatus = TaskStatus.fromString(statusInput);
        taskService.changeStatus(taskId, newStatus, authService.getCurrentUserId());
        ConsoleView.printSuccess("Status changed to " + newStatus);
        ConsoleView.pressEnterToContinue();
    }

    // ── 7. Assign Task ────────────────────────────────────────
    private void assignTask() {
        String taskId = resolveTaskId();
        ConsoleView.printHeader("ASSIGN TASK");
        userService.getAllUsers().forEach(u -> ConsoleView.printInfo(u.getUsername()));
        String username = ConsoleView.prompt("Assign to username");
        String assigneeId = userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User '" + username + "' not found.")).getId();
        taskService.assignTask(taskId, assigneeId, authService.getCurrentUserId());
        ConsoleView.printSuccess("Task assigned to " + username);
        ConsoleView.pressEnterToContinue();
    }

    // ── 8. Search & Filter ────────────────────────────────────
    private void searchTasks() {
        ConsoleView.printHeader("SEARCH & FILTER TASKS");
        String keyword   = ConsoleView.promptOptional("Keyword");
        String statusStr = ConsoleView.promptOptional("Filter by Status (TODO/IN_PROGRESS/REVIEW/DONE/CANCELLED)");
        String prioStr   = ConsoleView.promptOptional("Filter by Priority (LOW/MEDIUM/HIGH/CRITICAL)");
        String tagStr    = ConsoleView.promptOptional("Filter by Tag");

        TaskStatus filterStatus = statusStr.isBlank() ? null : TaskStatus.fromString(statusStr);
        Priority filterPriority = prioStr.isBlank() ? null : Priority.fromString(prioStr);

        SortStrategy sort = switch (TaskView.showSortMenu()) {
            case 1 -> new SortByPriority();
            case 2 -> new SortByDueDate();
            case 3 -> new SortByStatus();
            case 4 -> new SortByTitle();
            default -> null;
        };

        List<Task> results = taskService.searchTasks(keyword, filterStatus, filterPriority, tagStr, sort);
        ConsoleView.printInfo("Found " + results.size() + " task(s).");
        if (!results.isEmpty()) TaskView.displayTaskList(results);
        ConsoleView.pressEnterToContinue();
    }

    // ── 9. Add Comment ────────────────────────────────────────
    private void addComment() {
        String taskId = resolveTaskId();
        String content = ConsoleView.prompt("Comment");
        taskService.addComment(taskId, content, authService.getCurrentUserId());
        ConsoleView.printSuccess("Comment added.");
        ConsoleView.pressEnterToContinue();
    }

    // ── 10. Manage Tags ───────────────────────────────────────
    private void manageTags() {
        String taskId = resolveTaskId();
        int choice = TaskView.showTagMenu();
        switch (choice) {
            case 1 -> {
                String name = ConsoleView.prompt("Tag name");
                Tag tag = tagService.getOrCreate(name);
                taskService.addTag(taskId, tag);
                ConsoleView.printSuccess("Tag #" + tag.getName() + " added.");
            }
            case 2 -> {
                String name = ConsoleView.prompt("Tag name to remove");
                taskService.removeTag(taskId, name);
                ConsoleView.printSuccess("Tag #" + name + " removed.");
            }
            case 3 -> {}
        }
        ConsoleView.pressEnterToContinue();
    }

    // ── 11. Delayed Tasks ─────────────────────────────────────
    private void viewDelayedTasks() {
        ConsoleView.printHeader("DELAYED TASKS");
        List<Task> delayed = taskService.getDelayedTasks();
        if (delayed.isEmpty()) { ConsoleView.printInfo("No delayed tasks. Great job!"); }
        else { TaskView.displayTaskList(delayed); }
        ConsoleView.pressEnterToContinue();
    }

    // ── 12. Audit Log ─────────────────────────────────────────
    private void viewAuditLog() {
        String taskId = resolveTaskId();
        ConsoleView.printHeader("AUDIT LOG");
        var logs = auditLog.getLogsForTask(taskId);
        if (logs.isEmpty()) { ConsoleView.printInfo("No audit entries for this task."); }
        else { logs.forEach(l -> ConsoleView.printInfo(l.toString())); }
        ConsoleView.pressEnterToContinue();
    }

    // ── Helper ────────────────────────────────────────────────
    private String resolveTaskId() {
        ConsoleView.printHeader("SELECT TASK");
        List<Task> all = taskService.getAllTasks();
        if (all.isEmpty()) throw new RuntimeException("No tasks exist yet.");
        TaskView.displayTaskList(all);
        String input = ConsoleView.prompt("Enter Task ID (first 8 chars)");
        return all.stream()
            .filter(t -> t.getId().startsWith(input))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Task with ID prefix '" + input + "' not found."))
            .getId();
    }
}
