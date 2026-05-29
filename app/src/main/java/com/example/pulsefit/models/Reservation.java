package com.example.pulsefit.models;

public class Reservation {
    private int id;
    private int userId;
    private int sessionId;

    public Reservation() {
    }

    public Reservation(int id, int userId, int sessionId) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getSessionId() { return sessionId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
}