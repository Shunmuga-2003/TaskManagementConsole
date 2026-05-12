package com.taskmanager.model.state;

import com.taskmanager.exception.TaskException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;

public class CancelledState implements TaskState {

    @Override
    public void transition(Task task, TaskStatus newStatus) {
        throw new TaskException("CANCELLED task cannot be transitioned. It is a terminal state.");
    }

    @Override
    public String allowedNextStates() { return "None (terminal state)"; }

    @Override
    public String getStateName() { return "CANCELLED"; }
}
