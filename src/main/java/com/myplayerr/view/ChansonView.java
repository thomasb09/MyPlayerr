package com.myplayerr.view;

import com.myplayerr.database.ChansonDAO;
import com.myplayerr.model.Album;
import com.myplayerr.model.Chanson;
import com.myplayerr.view.utils.ChansonViewBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChansonView {

    private ChansonViewBox _chansonViewBox;
    private ChansonDAO _chansonDAO;

    public void setDependance(ChansonViewBox chansonViewBox, ChansonDAO chansonDAO) {
        _chansonViewBox = chansonViewBox;
        _chansonDAO = chansonDAO;
    }

    public VBox getView(Album album) {
        List<Chanson> chansons = (album == null) ?
                _chansonDAO.getAllChansons():
                _chansonDAO.getChansonByAlbum(album.getId());

        VBox rootVBox = new VBox();
        rootVBox.setSpacing(10);
        rootVBox.setAlignment(Pos.TOP_CENTER);

        Label title = new Label((album == null) ? "Toutes les chansons" : "Chansons de " +album.getNom());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        ScrollPane scrollPane = new ScrollPane();
        VBox songList = new VBox();
        songList.setSpacing(15);
        songList.setAlignment(Pos.TOP_LEFT);

        for (Chanson chanson : chansons) {
            songList.getChildren().add(_chansonViewBox.createChansonBox(chanson));
        }

        scrollPane.setContent(songList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        rootVBox.getChildren().addAll(title, scrollPane);
        rootVBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");
        return rootVBox;
    }
}
