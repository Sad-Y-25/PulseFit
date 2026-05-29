package com.example.pulsefit.models;

public class History {
    private int id;
    private String activite;
    private String duree;
    private int calories;

    public History() {
    }

    public History(int id, String activite, String duree, int calories) {
        this.id = id;
        this.activite = activite;
        this.duree = duree;
        this.calories = calories;
    }

    // Getters
    public int getId() { return id; }
    public String getActivite() { return activite; }
    public String getDuree() { return duree; }
    public int getCalories() { return calories; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setActivite(String activite) { this.activite = activite; }
    public void setDuree(String duree) { this.duree = duree; }
    public void setCalories(int calories) { this.calories = calories; }
}