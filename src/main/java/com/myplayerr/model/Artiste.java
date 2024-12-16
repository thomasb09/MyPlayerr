package com.myplayerr.model;

import java.util.ArrayList;
import java.util.List;

public class Artiste {
    private int id;
    private String nom;
    private List<Album> albums;
    private List<Chanson> chansons;

    public Artiste(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.albums = new ArrayList<>();
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

    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public void addChanson(Chanson chanson) {
        chansons.add(chanson);
    }
}
