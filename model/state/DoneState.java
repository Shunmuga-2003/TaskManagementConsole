package com.taskmanager.model.state;

import com.taskmanager.exception.TaskException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;

public class DoneState implements TaskState {

    @Override
    public void transition(Task task, TaskStatus newStatus) {
        throw new TaskException("DONE task cannot be transitioned further. It is a terminal state.");
    }

    @Override
    public String allowedNextStates() { return "None (terminal state)"; }

    @Override
    public String getStateName() { return "DONE"; }
}
