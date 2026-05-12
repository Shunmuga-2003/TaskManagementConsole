package com.taskmanager.model;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    REVIEW,
    DONE,
    CANCELLED;

    public static TaskStatus fromString(String s) {
        return switch (s.toUpperCase().replace(" ", "_")) {
            case "1", "TODO"        -> TODO;
            case "2", "IN_PROGRESS" -> IN_PROGRESS;
            case "3", "REVIEW"      -> REVIEW;
            case "4", "DONE"        -> DONE;
            case "5", "CANCELLED"   -> CANCELLED;
            default -> throw new IllegalArgumentException("Invalid status: " + s);
        };
    }
}
