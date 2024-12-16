package com.myplayerr.model;

public class
Chanson {
    private int id;
    private String titre;
    private int albumId;
    private String duree;
    private String cheminFichier;

    public Chanson(int id, String titre, int albumId, String duree, String cheminFichier) {
        this.id = id;
        this.titre = titre;
        this.albumId = albumId;
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

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
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
