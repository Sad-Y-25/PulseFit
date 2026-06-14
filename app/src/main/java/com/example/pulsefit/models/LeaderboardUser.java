package com.example.pulsefit.models;

public class LeaderboardUser {
    private String name;
    private int caloriesBurned;

    public LeaderboardUser(String name, int caloriesBurned) {
        this.name = name;
        this.caloriesBurned = caloriesBurned;
    }

    public String getName() {
        return name;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }
}
