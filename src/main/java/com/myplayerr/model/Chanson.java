package com.myplayerr.model;

public class Chanson {
    private int id;
    private String titre;
    private Album album;
    private Artiste artiste;
    private String duree;
    private String cheminFichier;

    public Chanson(int id, String titre, Album album, Artiste artiste, String duree, String cheminFichier) {
        this.id = id;
        this.titre = titre;
        this.album = album;
        this.artiste = artiste;
        this.duree = duree;
        this.cheminFichier = cheminFichier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }
}
