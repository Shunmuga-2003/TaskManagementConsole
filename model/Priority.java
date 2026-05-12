package com.taskmanager.model;

public enum Priority {
    LOW, MEDIUM, HIGH, CRITICAL;

    public static Priority fromString(String s) {
        return switch (s.toUpperCase()) {
            case "1", "LOW"      -> LOW;
            case "2", "MEDIUM"   -> MEDIUM;
            case "3", "HIGH"     -> HIGH;
            case "4", "CRITICAL" -> CRITICAL;
            default -> throw new IllegalArgumentException("Invalid priority: " + s);
        };
    }
}
