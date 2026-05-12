package com.taskmanager.view;

import com.taskmanager.model.*;
import com.taskmanager.service.UserService;
import com.taskmanager.util.DateUtil;
import com.taskmanager.util.TablePrinter;

import java.util.List;

public class TaskView extends ConsoleView {

    public static int showTaskMenu() {
        printHeader("TASK MENU");
        System.out.println("  1.  Create Task");
        System.out.println("  2.  View My Created Tasks");
        System.out.println("  3.  View Tasks Assigned to Me");
        System.out.println("  4.  Update Task");
        System.out.println("  5.  Delete Task");
        System.out.println("  6.  Change Task Status");
        System.out.println("  7.  Assign Task to User");
        System.out.println("  8.  Search & Filter Tasks");
        System.out.println("  9.  Add Comment to Task");
        System.out.println("  10. Manage Tags");
        System.out.println("  11. View Delayed Tasks");
        System.out.println("  12. View Audit Log for Task");
        System.out.println("  13. Back to Main Menu");
        System.out.println(DLINE);
        return readMenuChoice(13);
    }

    public static int showUpdateMenu() {
        printHeader("UPDATE TASK");
        System.out.println("  1. Update Title");
        System.out.println("  2. Update Description");
        System.out.println("  3. Update Due Date");
        System.out.println("  4. Update Priority");
        System.out.println("  5. Back");
        System.out.println(DLINE);
        return readMenuChoice(5);
    }

    public static int showTagMenu() {
        printHeader("TAG MANAGEMENT");
        System.out.println("  1. Add Tag to Task");
        System.out.println("  2. Remove Tag from Task");
        System.out.println("  3. Back");
        System.out.println(DLINE);
        return readMenuChoice(3);
    }

    public static int showSortMenu() {
        System.out.println("\n  Sort by:");
        System.out.println("  1. Priority");
        System.out.println("  2. Due Date");
        System.out.println("  3. Status");
        System.out.println("  4. Title");
        System.out.println("  5. No sorting");
        System.out.println(DLINE);
        return readMenuChoice(5);
    }

    public static void displayTaskList(List<Task> tasks) {
        UserService us = UserService.getInstance();
        List<String> headers = List.of("ID", "Title", "Status", "Priority", "Due Date", "Assignee", "Tags", "Delayed?");
        List<List<String>> rows = tasks.stream().map(t -> List.of(
            t.getId().substring(0, 8),
            truncate(t.getTitle(), 22),
            t.getStatus().name(),
            t.getPriority().name(),
            DateUtil.format(t.getDueDate()),
            t.getAssigneeId() == null ? "-" : us.getUsernameById(t.getAssigneeId()),
            t.getTags().isEmpty() ? "-" : t.getTags().stream().map(Tag::getName).reduce((a, b) -> a + "," + b).orElse("-"),
            t.isDelayed() ? "YES" : "no"
        )).toList();
        TablePrinter.print(headers, rows);
    }

    public static void displayTaskDetail(Task t) {
        UserService us = UserService.getInstance();
        printHeader("TASK DETAILS");
        printInfo("ID          : " + t.getId());
        printInfo("Title       : " + t.getTitle());
        printInfo("Description : " + t.getDescription());
        printInfo("Status      : " + t.getStatus() + "  (allowed next: " + t.allowedTransitions() + ")");
        printInfo("Priority    : " + t.getPriority());
        printInfo("Due Date    : " + DateUtil.format(t.getDueDate()));
        printInfo("Creator     : " + us.getUsernameById(t.getCreatorId()));
        printInfo("Assignee    : " + (t.getAssigneeId() == null ? "-" : us.getUsernameById(t.getAssigneeId())));
        printInfo("Created     : " + t.getCreatedAt());
        printInfo("Updated     : " + t.getUpdatedAt());
        printInfo("Delayed?    : " + (t.isDelayed() ? "YES ⚠" : "No"));
        printInfo("Tags        : " + (t.getTags().isEmpty() ? "-" : t.getTags().toString()));
        System.out.println(DLINE);
        if (!t.getComments().isEmpty()) {
            System.out.println("  Comments:");
            t.getComments().forEach(c -> {
                String author = us.getUsernameById(c.getAuthorId());
                System.out.println("    [" + author + "] " + c.toString());
            });
        } else {
            System.out.println("  No comments yet.");
        }
        System.out.println(LINE);
    }

    public static void displayStatusOptions() {
        System.out.println("\n  Status options:");
        System.out.println("  1. TODO");
        System.out.println("  2. IN_PROGRESS");
        System.out.println("  3. REVIEW");
        System.out.println("  4. DONE");
        System.out.println("  5. CANCELLED");
    }

    public static void displayPriorityOptions() {
        System.out.println("\n  Priority options:");
        System.out.println("  1. LOW");
        System.out.println("  2. MEDIUM");
        System.out.println("  3. HIGH");
        System.out.println("  4. CRITICAL");
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
