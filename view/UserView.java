package com.taskmanager.view;

import com.taskmanager.model.User;
import com.taskmanager.util.TablePrinter;

import java.util.List;

public class UserView extends ConsoleView {

    public static int showUserMenu() {
        printHeader("USER MENU");
        System.out.println("  1. List All Users");
        System.out.println("  2. View My Profile");
        System.out.println("  3. Back to Main Menu");
        System.out.println(DLINE);
        return readMenuChoice(3);
    }

    public static void displayUserList(List<User> users) {
        List<String> headers = List.of("ID", "Username", "Email");
        List<List<String>> rows = users.stream()
            .map(u -> List.of(u.getId().substring(0, 8), u.getUsername(), u.getEmail()))
            .toList();
        TablePrinter.print(headers, rows);
    }

    public static void displayProfile(User user) {
        printHeader("MY PROFILE");
        printInfo("ID       : " + user.getId());
        printInfo("Username : " + user.getUsername());
        printInfo("Email    : " + user.getEmail());
        System.out.println(LINE);
    }
}
