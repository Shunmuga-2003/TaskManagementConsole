package com.taskmanager.model.state;

import com.taskmanager.exception.TaskException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;

public class ReviewState implements TaskState {

    @Override
    public void transition(Task task, TaskStatus newStatus) {
        if (newStatus != TaskStatus.DONE
                && newStatus != TaskStatus.IN_PROGRESS
                && newStatus != TaskStatus.CANCELLED) {
            throw new TaskException("REVIEW task can only move to DONE, IN_PROGRESS or CANCELLED. Allowed: " + allowedNextStates());
        }
    }

    @Override
    public String allowedNextStates() { return "DONE, IN_PROGRESS, CANCELLED"; }

    @Override
    public String getStateName() { return "REVIEW"; }
}
