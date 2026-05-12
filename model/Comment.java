package com.taskmanager.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment {
    private final String id;
    private final String taskId;
    private final String authorId;
    private String content;
    private final LocalDateTime createdAt;

    public Comment(String taskId, String authorId, String content) {
        this.id        = UUID.randomUUID().toString();
        this.taskId    = taskId;
        this.authorId  = authorId;
        this.content   = content;
        this.createdAt = LocalDateTime.now();
    }

    public String getId()          { return id; }
    public String getTaskId()      { return taskId; }
    public String getAuthorId()    { return authorId; }
    public String getContent()     { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return "[" + createdAt.toLocalDate() + " " + createdAt.toLocalTime().withNano(0) + "] " + content;
    }
}
