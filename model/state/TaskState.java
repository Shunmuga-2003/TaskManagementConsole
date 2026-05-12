package com.taskmanager.model.state;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;

public interface TaskState {
    void transition(Task task, TaskStatus newStatus);
    String allowedNextStates();
    String getStateName();
}
