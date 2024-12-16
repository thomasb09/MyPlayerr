package com.myplayerr.controller;

import com.myplayerr.view.AlbumView;
import com.myplayerr.view.ArtisteView;
import com.myplayerr.view.ChansonView;
import com.myplayerr.view.PlaylistView;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private void showPlaylist() {
        mainPane.setCenter(new PlaylistView().getView());
    }

    @FXML
    private void showAlbum() {
        mainPane.setCenter(new AlbumView().getView());
    }

    @FXML
    private void showChanson() {
        mainPane.setCenter(new ChansonView().getView());
    }

    @FXML
    private void showArtiste() {
        mainPane.setCenter(new ArtisteView().getView());
    }
}
