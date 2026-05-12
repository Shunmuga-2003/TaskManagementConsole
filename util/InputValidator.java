package com.taskmanager.util;

import com.taskmanager.exception.ValidationException;

public class InputValidator {

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    public static String requireMinLength(String value, String fieldName, int min) {
        requireNonBlank(value, fieldName);
        if (value.trim().length() < min) {
            throw new ValidationException(fieldName + " must be at least " + min + " characters.");
        }
        return value.trim();
    }

    public static int requirePositiveInt(String value, String fieldName) {
        try {
            int i = Integer.parseInt(value.trim());
            if (i <= 0) throw new ValidationException(fieldName + " must be a positive number.");
            return i;
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a valid integer.");
        }
    }

    public static int requireInRange(String value, String fieldName, int lo, int hi) {
        int i = requirePositiveInt(value, fieldName);
        if (i < lo || i > hi) {
            throw new ValidationException(fieldName + " must be between " + lo + " and " + hi + ".");
        }
        return i;
    }
}
