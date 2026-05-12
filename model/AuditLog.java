package com.taskmanager.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditLog {
    private final String id;
    private final String taskId;
    private final String userId;
    private final String action;
    private final String details;
    private final LocalDateTime timestamp;

    public AuditLog(String taskId, String userId, String action, String details) {
        this.id        = UUID.randomUUID().toString();
        this.taskId    = taskId;
        this.userId    = userId;
        this.action    = action;
        this.details   = details;
        this.timestamp = LocalDateTime.now();
    }

    public String getId()        { return id; }
    public String getTaskId()    { return taskId; }
    public String getUserId()    { return userId; }
    public String getAction()    { return action; }
    public String getDetails()   { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] Task:%s | User:%s | %s | %s",
            timestamp.toString().substring(0, 19), taskId.substring(0, 8),
            userId.substring(0, 8), action, details);
    }
}
