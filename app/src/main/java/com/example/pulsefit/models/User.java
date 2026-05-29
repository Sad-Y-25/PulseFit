package com.example.pulsefit.models;

public class User {
    private int id;
    private String nom;
    private String email;
    private String password;
    private String photo;

    // Constructeur vide (souvent requis par Firebase ou SQLite)
    public User() {
    }

    // Constructeur complet
    public User(int id, String nom, String email, String password, String photo) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}