package com.taskmanager.view;

import com.taskmanager.model.Priority;
import com.taskmanager.model.TaskStatus;

import java.util.Map;

public class ReportView extends ConsoleView {

    public static int showReportMenu() {
        printHeader("REPORTS & ANALYTICS");
        System.out.println("  1. Tasks by Priority");
        System.out.println("  2. Tasks by Status");
        System.out.println("  3. Tasks by Tag");
        System.out.println("  4. Back to Main Menu");
        System.out.println(DLINE);
        return readMenuChoice(4);
    }

    public static void displayPriorityReport(Map<Priority, Long> data) {
        printHeader("TASKS BY PRIORITY");
        long total = data.values().stream().mapToLong(Long::longValue).sum();
        for (Priority p : Priority.values()) {
            long count = data.getOrDefault(p, 0L);
            printInfo(String.format("%-10s : %3d  %s", p.name(), count, bar(count, total)));
        }
        System.out.println(LINE);
    }

    public static void displayStatusReport(Map<TaskStatus, Long> data) {
        printHeader("TASKS BY STATUS");
        long total = data.values().stream().mapToLong(Long::longValue).sum();
        for (TaskStatus s : TaskStatus.values()) {
            long count = data.getOrDefault(s, 0L);
            printInfo(String.format("%-12s : %3d  %s", s.name(), count, bar(count, total)));
        }
        System.out.println(LINE);
    }

    public static void displayTagReport(Map<String, Long> data) {
        printHeader("TASKS BY TAG");
        if (data.isEmpty()) {
            printInfo("No tags found.");
        } else {
            long total = data.values().stream().mapToLong(Long::longValue).sum();
            data.forEach((tag, count) ->
                printInfo(String.format("#%-14s : %3d  %s", tag, count, bar(count, total))));
        }
        System.out.println(LINE);
    }

    private static String bar(long count, long total) {
        if (total == 0) return "";
        int filled = (int) (count * 20 / total);
        return "[" + "█".repeat(filled) + "░".repeat(20 - filled) + "] " + count;
    }
}
