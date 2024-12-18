package com.myplayerr.view;

import com.myplayerr.database.ChansonDAO;
import com.myplayerr.model.Album;
import com.myplayerr.model.Chanson;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class AlbumSongsView {

    private ChansonDAO _chansonDAO;
    private ChansonViewBox _chansonViewBox;

    public void setDependance(ChansonDAO chansonDAO, ChansonViewBox chansonViewBox) {
        _chansonDAO = chansonDAO;
        _chansonViewBox = chansonViewBox;
    }

    public VBox getAlbumSongs(Album album) {
        VBox songVBox = new VBox();
        songVBox.setSpacing(20);
        songVBox.setAlignment(Pos.TOP_CENTER);
        songVBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Label albumTitle = new Label("Chansons de l'album : " + album.getNom());
        albumTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        ScrollPane scrollPane = new ScrollPane();
        VBox songList = new VBox();
        songList.setSpacing(15);
        songList.setAlignment(Pos.TOP_LEFT);

        List<Chanson> chansons = _chansonDAO.getChansonByAlbum(album.getId());
        for (Chanson chanson : chansons) {
            songList.getChildren().add(_chansonViewBox.createChansonBox(chanson));
        }

        scrollPane.setContent(songList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        songVBox.getChildren().addAll(albumTitle, scrollPane);

        return songVBox;
    }
}
