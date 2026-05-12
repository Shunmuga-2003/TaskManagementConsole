package com.taskmanager;

import com.taskmanager.controller.*;
import com.taskmanager.observer.AuditLogObserver;
import com.taskmanager.observer.NotificationObserver;
import com.taskmanager.service.AuthService;
import com.taskmanager.service.TaskService;
import com.taskmanager.view.AuthView;
import com.taskmanager.view.ConsoleView;

public class AppRouter {
    private final AuthService    authService = AuthService.getInstance();
    private final AuthController authCtrl    = new AuthController();
    private final AuditLogObserver auditLog  = new AuditLogObserver();

    public AppRouter() {
        TaskService ts = TaskService.getInstance();
        ts.addObserver(auditLog);
        ts.addObserver(new NotificationObserver());
    }

    public void start() {
        AuthView.showWelcome();
        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                running = handleAuthMenu();
            } else {
                handleMainMenu();
            }
        }
        System.out.println("\n  Goodbye! 👋\n");
    }

    private boolean handleAuthMenu() {
        int choice = AuthView.showAuthMenu();
        try {
            switch (choice) {
                case 1 -> authCtrl.handleLogin();
                case 2 -> authCtrl.handleRegister();
                case 3 -> { return false; }
            }
        } catch (Exception e) {
            ConsoleView.printError(e.getMessage());
            ConsoleView.pressEnterToContinue();
        }
        return true;
    }

    private void handleMainMenu() {
        boolean loggedIn = true;
        while (loggedIn && authService.isLoggedIn()) {
            printMainMenu();
            int choice = ConsoleView.readMenuChoice(4);
            try {
                switch (choice) {
                    case 1 -> new TaskController(auditLog).run();
                    case 2 -> new UserController().run();
                    case 3 -> new ReportController().run();
                    case 4 -> { authCtrl.handleLogout(); loggedIn = false; }
                }
            } catch (Exception e) {
                ConsoleView.printError(e.getMessage());
                ConsoleView.pressEnterToContinue();
            }
        }
    }

    private void printMainMenu() {
        String user = authService.getCurrentUser().getUsername();
        ConsoleView.printHeader("MAIN MENU  [" + user + "]");
        System.out.println("  1. Task Management");
        System.out.println("  2. User Management");
        System.out.println("  3. Reports & Analytics");
        System.out.println("  4. Logout");
        System.out.println(ConsoleView.DLINE);
    }
}
