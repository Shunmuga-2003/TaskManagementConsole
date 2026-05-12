package com.taskmanager.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parse(String s) {
        try {
            return LocalDate.parse(s.trim(), FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd (e.g. 2025-12-31)");
        }
    }

    public static String format(LocalDate date) {
        return date == null ? "N/A" : date.format(FMT);
    }
}
