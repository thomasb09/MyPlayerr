package com.myplayerr.service;

import com.myplayerr.database.ChansonDAO;
import com.myplayerr.model.Chanson;

import java.util.List;
import java.util.stream.Collectors;

public class RechercheChansonService {

    private ChansonDAO _chansonDAO;

    public void setDependance(ChansonDAO chansonDAO){
        _chansonDAO = chansonDAO;
    }

    public List<Chanson> rechercheChansons(String query) {
        List<Chanson> allChansons = _chansonDAO.getAllChansons();
        return allChansons.stream()
                .filter(chanson -> chanson.getTitre().toLowerCase().contains(query.toLowerCase())
                        || chanson.getArtiste().getNom().toLowerCase().contains(query.toLowerCase())
                        || chanson.getAlbum().getNom().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
