package com.taskmanager.controller;

import com.taskmanager.model.User;
import com.taskmanager.service.AuthService;
import com.taskmanager.view.AuthView;
import com.taskmanager.view.ConsoleView;

public class AuthController {
    private final AuthService authService = AuthService.getInstance();

    public User handleLogin() {
        String[] creds = AuthView.promptLogin();
        User user = authService.login(creds[0], creds[1]);
        ConsoleView.printSuccess("Welcome back, " + user.getUsername() + "!");
        return user;
    }

    public User handleRegister() {
        String[] data = AuthView.promptRegister();
        User user = authService.register(data[0], data[1], data[2]);
        ConsoleView.printSuccess("Account created! Welcome, " + user.getUsername() + "!");
        return user;
    }

    public void handleLogout() {
        String name = authService.getCurrentUser().getUsername();
        authService.logout();
        ConsoleView.printSuccess("Goodbye, " + name + "!");
    }
}
