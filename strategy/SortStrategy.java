package com.taskmanager.strategy;

import com.taskmanager.model.Task;
import java.util.List;

public interface SortStrategy {
    List<Task> sort(List<Task> tasks);
    String getStrategyName();
}
