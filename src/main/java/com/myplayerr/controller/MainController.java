package com.myplayerr.controller;

import com.myplayerr.database.SettingDAO;
import com.myplayerr.service.ChansonService;
import com.myplayerr.view.AlbumView;
import com.myplayerr.view.ArtisteView;
import com.myplayerr.view.ChansonView;
import com.myplayerr.view.PlaylistView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {

    @FXML
    private BorderPane mainPane;

    private final ChansonService chansonService = new ChansonService();
    private final SettingDAO settingsDAO = new SettingDAO();

    @FXML
    public void initialize() {
        SettingDAO settingsDAO = new SettingDAO();
        String musicPath = settingsDAO.getSetting("mp3Path");

        if (musicPath != null) {
            chansonService.scanAndImportMusic(musicPath);
        }
    }



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

    @FXML
    private void changePathMusic() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select MP3 Folder");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();

            SettingDAO settingsDAO = new SettingDAO();
            settingsDAO.saveSetting("mp3Path", selectedPath);

            chansonService.scanAndImportMusic(selectedPath);
        }
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
