package com.taskmanager.model;

import java.util.UUID;

public class Tag {
    private final String id;
    private String name;

    public Tag(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name.toLowerCase().trim();
    }

    public String getId()   { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name.toLowerCase().trim(); }

    @Override
    public String toString() { return "#" + name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return name.equals(tag.name);
    }

    @Override
    public int hashCode() { return name.hashCode(); }
}
