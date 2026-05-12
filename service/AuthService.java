package com.taskmanager.service;

import com.taskmanager.model.User;

public class AuthService {
    private static AuthService instance;
    private User currentUser;

    private AuthService() {}

    public static synchronized AuthService getInstance() {
        if (instance == null) instance = new AuthService();
        return instance;
    }

    public User login(String username, String password) {
        currentUser = UserService.getInstance().login(username, password);
        return currentUser;
    }

    public User register(String username, String password, String email) {
        User user = UserService.getInstance().register(username, password, email);
        currentUser = user;
        return user;
    }

    public void logout() { currentUser = null; }

    public User getCurrentUser() { return currentUser; }

    public boolean isLoggedIn() { return currentUser != null; }

    public String getCurrentUserId() {
        return currentUser == null ? null : currentUser.getId();
    }
}
