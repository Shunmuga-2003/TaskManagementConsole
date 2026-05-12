package com.taskmanager.controller;

import com.taskmanager.service.TaskService;
import com.taskmanager.view.ConsoleView;
import com.taskmanager.view.ReportView;

public class ReportController {
    private final TaskService taskService = TaskService.getInstance();

    public void run() {
        boolean back = false;
        while (!back) {
            int choice = ReportView.showReportMenu();
            try {
                switch (choice) {
                    case 1 -> { ReportView.displayPriorityReport(taskService.countByPriority()); ConsoleView.pressEnterToContinue(); }
                    case 2 -> { ReportView.displayStatusReport(taskService.countByStatus()); ConsoleView.pressEnterToContinue(); }
                    case 3 -> { ReportView.displayTagReport(taskService.countByTag()); ConsoleView.pressEnterToContinue(); }
                    case 4 -> back = true;
                }
            } catch (Exception e) {
                ConsoleView.printError(e.getMessage());
                ConsoleView.pressEnterToContinue();
            }
        }
    }
}
