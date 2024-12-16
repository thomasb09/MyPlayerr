package com.myplayerr.model;

public class Album {
    private int id;
    private String titre;
    private int artisteId;

    public Album(int id, String titre, int artisteId) {
        this.id = id;
        this.titre = titre;
        this.artisteId = artisteId;
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

    public int getArtisteId() {
        return artisteId;
    }

    public void setArtisteId(int artisteId) {
        this.artisteId = artisteId;
    }
}
