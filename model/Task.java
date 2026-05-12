package com.taskmanager.model;

import com.taskmanager.model.state.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Task {
    private final String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private TaskStatus status;
    private final String creatorId;
    private String assigneeId;
    private final LocalDate createdAt;
    private LocalDate updatedAt;

    private final List<Tag>     tags     = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();

    private TaskState currentState;
    private final ReentrantLock lock = new ReentrantLock();

    public Task(String title, String description, LocalDate dueDate,
                Priority priority, String creatorId) {
        this.id          = UUID.randomUUID().toString();
        this.title       = title;
        this.description = description;
        this.dueDate     = dueDate;
        this.priority    = priority;
        this.creatorId   = creatorId;
        this.status      = TaskStatus.TODO;
        this.createdAt   = LocalDate.now();
        this.updatedAt   = LocalDate.now();
        this.currentState = new TodoState();
    }

    // ── State machine ──────────────────────────────────────────
    public void transitionTo(TaskStatus newStatus) {
        lock.lock();
        try {
            currentState.transition(this, newStatus);
            this.status    = newStatus;
            this.updatedAt = LocalDate.now();
            applyState();
        } finally {
            lock.unlock();
        }
    }

    private void applyState() {
        this.currentState = switch (status) {
            case TODO        -> new TodoState();
            case IN_PROGRESS -> new InProgressState();
            case REVIEW      -> new ReviewState();
            case DONE        -> new DoneState();
            case CANCELLED   -> new CancelledState();
        };
    }

    public String allowedTransitions() {
        return currentState.allowedNextStates();
    }

    // ── Lock helpers for concurrent access ────────────────────
    public void lock()   { lock.lock(); }
    public void unlock() { lock.unlock(); }

    // ── Getters ───────────────────────────────────────────────
    public String     getId()          { return id; }
    public String     getTitle()       { return title; }
    public String     getDescription() { return description; }
    public LocalDate  getDueDate()     { return dueDate; }
    public Priority   getPriority()    { return priority; }
    public TaskStatus getStatus()      { return status; }
    public String     getCreatorId()   { return creatorId; }
    public String     getAssigneeId()  { return assigneeId; }
    public LocalDate  getCreatedAt()   { return createdAt; }
    public LocalDate  getUpdatedAt()   { return updatedAt; }
    public List<Tag>     getTags()     { return Collections.unmodifiableList(tags); }
    public List<Comment> getComments() { return Collections.unmodifiableList(comments); }

    // ── Setters ───────────────────────────────────────────────
    public void setTitle(String title)             { this.title = title;       this.updatedAt = LocalDate.now(); }
    public void setDescription(String description) { this.description = description; this.updatedAt = LocalDate.now(); }
    public void setDueDate(LocalDate dueDate)      { this.dueDate = dueDate;   this.updatedAt = LocalDate.now(); }
    public void setPriority(Priority priority)     { this.priority = priority; this.updatedAt = LocalDate.now(); }
    public void setAssigneeId(String assigneeId)   { this.assigneeId = assigneeId; this.updatedAt = LocalDate.now(); }
    public void setStatus(TaskStatus status)       { this.status = status; applyState(); this.updatedAt = LocalDate.now(); }

    // ── Tag helpers ───────────────────────────────────────────
    public void addTag(Tag tag) {
        if (!tags.contains(tag)) tags.add(tag);
    }
    public void removeTag(String tagName) {
        tags.removeIf(t -> t.getName().equalsIgnoreCase(tagName));
    }

    // ── Comment helpers ───────────────────────────────────────
    public void addComment(Comment comment) { comments.add(comment); }

    public boolean isDelayed() {
        return dueDate != null
            && LocalDate.now().isAfter(dueDate)
            && status != TaskStatus.DONE
            && status != TaskStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return String.format("Task{id='%s', title='%s', status=%s, priority=%s, dueDate=%s}",
            id.substring(0, 8), title, status, priority, dueDate);
    }
}
