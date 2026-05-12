package com.taskmanager.service;

import com.taskmanager.exception.AuthException;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private static UserService instance;
    private final Map<String, User> usersById       = new ConcurrentHashMap<>();
    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();

    private UserService() {
        // Seed demo users
        register("alice", "alice123", "alice@demo.com");
        register("bob",   "bob123",   "bob@demo.com");
        register("carol", "carol123", "carol@demo.com");
    }

    public static synchronized UserService getInstance() {
        if (instance == null) instance = new UserService();
        return instance;
    }

    public User register(String username, String password, String email) {
        if (username == null || username.isBlank())
            throw new ValidationException("Username cannot be blank.");
        if (password == null || password.length() < 4)
            throw new ValidationException("Password must be at least 4 characters.");
        if (usersByUsername.containsKey(username.toLowerCase()))
            throw new AuthException("Username '" + username + "' is already taken.");

        User user = new User(username.toLowerCase(), password, email);
        usersById.put(user.getId(), user);
        usersByUsername.put(user.getUsername(), user);
        return user;
    }

    public User login(String username, String password) {
        User user = usersByUsername.get(username.toLowerCase());
        if (user == null || !user.getPassword().equals(password))
            throw new AuthException("Invalid username or password.");
        return user;
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username.toLowerCase()));
    }

    public List<User> getAllUsers() {
        return List.copyOf(usersById.values());
    }

    public String getUsernameById(String id) {
        return findById(id).map(User::getUsername).orElse("unknown");
    }
}
