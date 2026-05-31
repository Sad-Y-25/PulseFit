package com.example.pulsefit.models;

public class Workout {
    private int id; // NOUVEAU : L'identifiant unique
    private String title;
    private String details;

    public Workout(int id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDetails() { return details; }
}