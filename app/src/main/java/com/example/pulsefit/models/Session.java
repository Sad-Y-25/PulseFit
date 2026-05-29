package com.example.pulsefit.models;

public class Session {
    private int id;
    private String titre;
    private String coach;
    private String date;
    private int places;

    public Session() {
    }

    public Session(int id, String titre, String coach, String date, int places) {
        this.id = id;
        this.titre = titre;
        this.coach = coach;
        this.date = date;
        this.places = places;
    }

    // Getters
    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getCoach() { return coach; }
    public String getDate() { return date; }
    public int getPlaces() { return places; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setCoach(String coach) { this.coach = coach; }
    public void setDate(String date) { this.date = date; }
    public void setPlaces(int places) { this.places = places; }
}