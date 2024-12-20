package com.myplayerr.model;

public class Album implements EntityBox {
    private int id;
    private String nom;
    private Artiste artiste;
    private String imagePath; // Nouveau champ

    public Album() {}

    public Album(int id, String nom, Artiste artiste, String imagePath) {
        this.id = id;
        this.nom = nom;
        this.artiste = artiste;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public Artiste getArtiste() { return artiste; }
    public String getImagePath() { return imagePath; }
    @Override
    public String getNom() {
        return nom;
    }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setArtiste(Artiste artiste) { this.artiste = artiste; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
