package com.myplayerr.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private final List<Chanson> chansons;

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        this.chansons = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public void addChanson(Chanson chanson) {
        this.chansons.add(chanson);
    }

    public void removeChanson(Chanson chanson) {
        this.chansons.remove(chanson);
    }

    public void setId(int id) {
        this.id = id;
    }
}
