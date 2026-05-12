package com.taskmanager.model.state;

import com.taskmanager.exception.TaskException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;

public class InProgressState implements TaskState {

    @Override
    public void transition(Task task, TaskStatus newStatus) {
        if (newStatus != TaskStatus.REVIEW && newStatus != TaskStatus.CANCELLED) {
            throw new TaskException("IN_PROGRESS task can only move to REVIEW or CANCELLED. Allowed: " + allowedNextStates());
        }
    }

    @Override
    public String allowedNextStates() { return "REVIEW, CANCELLED"; }

    @Override
    public String getStateName() { return "IN_PROGRESS"; }
}
