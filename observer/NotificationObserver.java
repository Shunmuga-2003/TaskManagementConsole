package com.taskmanager.observer;

import com.taskmanager.model.Task;

public class NotificationObserver implements TaskObserver {

    @Override
    public void onTaskCreated(Task task, String actorUserId) {
        System.out.println("\n  [NOTIFY] New task created: \"" + task.getTitle() + "\"");
    }

    @Override
    public void onTaskUpdated(Task task, String field, String oldValue, String newValue, String actorUserId) {
        System.out.println("\n  [NOTIFY] Task \"" + task.getTitle() + "\" updated — "
            + field + ": " + oldValue + " → " + newValue);
    }

    @Override
    public void onTaskStatusChanged(Task task, String oldStatus, String newStatus, String actorUserId) {
        System.out.println("\n  [NOTIFY] Task \"" + task.getTitle() + "\" status changed: "
            + oldStatus + " → " + newStatus);
    }

    @Override
    public void onTaskDeleted(Task task, String actorUserId) {
        System.out.println("\n  [NOTIFY] Task \"" + task.getTitle() + "\" has been deleted.");
    }

    @Override
    public void onCommentAdded(Task task, String comment, String actorUserId) {
        System.out.println("\n  [NOTIFY] New comment on \"" + task.getTitle() + "\": " + comment);
    }
}
