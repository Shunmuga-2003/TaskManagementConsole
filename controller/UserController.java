package com.taskmanager.controller;

import com.taskmanager.service.AuthService;
import com.taskmanager.service.UserService;
import com.taskmanager.view.ConsoleView;
import com.taskmanager.view.UserView;

public class UserController {
    private final UserService userService = UserService.getInstance();
    private final AuthService authService = AuthService.getInstance();

    public void run() {
        boolean back = false;
        while (!back) {
            int choice = UserView.showUserMenu();
            try {
                switch (choice) {
                    case 1 -> { UserView.displayUserList(userService.getAllUsers()); ConsoleView.pressEnterToContinue(); }
                    case 2 -> { UserView.displayProfile(authService.getCurrentUser()); ConsoleView.pressEnterToContinue(); }
                    case 3 -> back = true;
                }
            } catch (Exception e) {
                ConsoleView.printError(e.getMessage());
                ConsoleView.pressEnterToContinue();
            }
        }
    }
}
