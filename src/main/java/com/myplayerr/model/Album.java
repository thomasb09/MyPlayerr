package com.myplayerr.model;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private int id;
    private String nom;
    private Artiste artiste;
    private List<Chanson> chansons;

    public Album(int id, String nom, Artiste artiste) {
        this.id = id;
        this.nom = nom;
        this.artiste = artiste;
        this.chansons = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public void addChanson(Chanson chanson) {
        chansons.add(chanson);
    }
}
