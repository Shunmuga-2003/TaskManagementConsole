package com.taskmanager.service;

import com.taskmanager.model.Tag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TagService {
    private static TagService instance;
    private final Map<String, Tag> tagsByName = new ConcurrentHashMap<>();

    private TagService() {}

    public static synchronized TagService getInstance() {
        if (instance == null) instance = new TagService();
        return instance;
    }

    public Tag getOrCreate(String name) {
        return tagsByName.computeIfAbsent(name.toLowerCase().trim(), Tag::new);
    }

    public Optional<Tag> findByName(String name) {
        return Optional.ofNullable(tagsByName.get(name.toLowerCase().trim()));
    }

    public List<Tag> getAllTags() {
        return List.copyOf(tagsByName.values());
    }
}
