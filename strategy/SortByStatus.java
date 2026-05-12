package com.taskmanager.strategy;

import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortByStatus implements SortStrategy {
    @Override
    public List<Task> sort(List<Task> tasks) {
        List<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparing(Task::getStatus));
        return sorted;
    }
    @Override public String getStrategyName() { return "Sort by Status"; }
}
