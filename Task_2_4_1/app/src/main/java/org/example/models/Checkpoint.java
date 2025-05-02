package org.example.models;

public class Checkpoint {
    private String name;
    private String date;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Checkpoint(String name, String date) {
        this.name = name;
        this.date = date;
    }
}