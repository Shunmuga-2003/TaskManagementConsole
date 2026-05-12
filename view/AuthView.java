package com.taskmanager.view;

public class AuthView extends ConsoleView {

    public static void showWelcome() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("       TASK MANAGEMENT SYSTEM  v1.0");
        System.out.println("=".repeat(55));
        System.out.println("  Patterns: MVC | State | Observer | Strategy");
        System.out.println("=".repeat(55));
    }

    public static int showAuthMenu() {
        printHeader("WELCOME");
        System.out.println("  1. Login");
        System.out.println("  2. Register");
        System.out.println("  3. Exit");
        System.out.println(DLINE);
        return readMenuChoice(3);
    }

    public static String[] promptLogin() {
        printHeader("LOGIN");
        String username = prompt("Username");
        String password = prompt("Password");
        return new String[]{username, password};
    }

    public static String[] promptRegister() {
        printHeader("REGISTER");
        String username = prompt("Username");
        String password = prompt("Password (min 4 chars)");
        String email    = prompt("Email");
        return new String[]{username, password, email};
    }
}
