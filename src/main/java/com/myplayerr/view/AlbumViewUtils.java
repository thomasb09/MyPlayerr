package com.myplayerr.view;

import com.myplayerr.database.ChansonDAO;
import com.myplayerr.model.Album;
import com.myplayerr.model.Chanson;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class AlbumViewUtils {

    public static void showAlbumSongs(BorderPane pane, Album album) {
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

        List<Chanson> chansons = ChansonDAO.getChansonByAlbum(album.getId());
        for (Chanson chanson : chansons) {
            songList.getChildren().add(ChansonViewBox.createChansonBox(chanson));
        }

        scrollPane.setContent(songList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        songVBox.getChildren().addAll(albumTitle, scrollPane);
        pane.setCenter(songVBox);
    }
}
