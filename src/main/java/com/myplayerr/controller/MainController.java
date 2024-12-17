package com.myplayerr.controller;

import com.myplayerr.database.SettingDAO;
import com.myplayerr.service.ChansonService;
import com.myplayerr.service.SettingService;
import com.myplayerr.view.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainPane;

    private final ChansonService chansonService = new ChansonService();
    private final SettingService settingService = new SettingService();
    private final SettingDAO settingDAO = new SettingDAO();

    @FXML
    public void initialize() {
        String musicPath = settingDAO.getSetting("mp3Path");

        if (musicPath != null) {
            chansonService.scanAndImportMusic(musicPath);
        }
    }

    @FXML
    private void showPlaylist() {
        mainPane.setCenter(new PlaylistView().getView());
    }

    @FXML
    private void showArtistes() {
        mainPane.setCenter(new ArtisteView(mainPane).getView());
    }

    @FXML
    private void showAlbums() {
        mainPane.setCenter(new AlbumView(mainPane).getView());
    }

    @FXML
    private void showChansons() {
        mainPane.setCenter(new ChansonView().getView());
    }

    @FXML
    public void showRechercheView() {
        new RechercheView(mainPane).getView();
    }

    @FXML
    public void showAjoutChansonView() {
        new AjoutChansonView(mainPane).getView();
    }

    @FXML
    private void changePathMusic() {
        settingService.setPathMusic();
    }

    @FXML
    private void showSettings() {
    }

    @FXML
    private void showAbout() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About MyPlayerr");
        aboutAlert.setHeaderText("MyPlayerr - Gestionnaire MP3");
        aboutAlert.setContentText("Version 1.0.0\nDéveloppé par Thomas Bisson.");
        aboutAlert.showAndWait();
    }

    @FXML
    private void exitApp() {
        System.exit(0);
    }
}
