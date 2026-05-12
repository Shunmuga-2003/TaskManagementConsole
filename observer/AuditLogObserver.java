package com.taskmanager.observer;

import com.taskmanager.model.AuditLog;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuditLogObserver implements TaskObserver {

    private final List<AuditLog> logs = new ArrayList<>();

    @Override
    public void onTaskCreated(Task task, String actorUserId) {
        logs.add(new AuditLog(task.getId(), actorUserId, "TASK_CREATED",
            "Title: " + task.getTitle() + " | Priority: " + task.getPriority()));
    }

    @Override
    public void onTaskUpdated(Task task, String field, String oldValue, String newValue, String actorUserId) {
        logs.add(new AuditLog(task.getId(), actorUserId, "TASK_UPDATED",
            field + " changed from [" + oldValue + "] to [" + newValue + "]"));
    }

    @Override
    public void onTaskStatusChanged(Task task, String oldStatus, String newStatus, String actorUserId) {
        logs.add(new AuditLog(task.getId(), actorUserId, "STATUS_CHANGED",
            oldStatus + " → " + newStatus));
    }

    @Override
    public void onTaskDeleted(Task task, String actorUserId) {
        logs.add(new AuditLog(task.getId(), actorUserId, "TASK_DELETED",
            "Title: " + task.getTitle()));
    }

    @Override
    public void onCommentAdded(Task task, String comment, String actorUserId) {
        logs.add(new AuditLog(task.getId(), actorUserId, "COMMENT_ADDED",
            "Comment: " + comment));
    }

    public List<AuditLog> getLogs() { return Collections.unmodifiableList(logs); }

    public List<AuditLog> getLogsForTask(String taskId) {
        return logs.stream().filter(l -> l.getTaskId().equals(taskId)).toList();
    }
}
