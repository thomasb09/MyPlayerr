package com.myplayerr.model;

public class Artiste implements EntityBox{
    private int id;
    private String nom;
    private String imagePath;

    public Artiste() {}

    public Artiste(int id, String nom, String imagePath) {
        this.id = id;
        this.nom = nom;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getImagePath() { return imagePath; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
