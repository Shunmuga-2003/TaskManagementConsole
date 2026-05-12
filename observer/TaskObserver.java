package com.taskmanager.observer;

import com.taskmanager.model.Task;

public interface TaskObserver {
    void onTaskCreated(Task task, String actorUserId);
    void onTaskUpdated(Task task, String field, String oldValue, String newValue, String actorUserId);
    void onTaskStatusChanged(Task task, String oldStatus, String newStatus, String actorUserId);
    void onTaskDeleted(Task task, String actorUserId);
    void onCommentAdded(Task task, String comment, String actorUserId);
}
