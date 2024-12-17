package com.myplayerr.controller;

import com.myplayerr.database.SettingDAO;
import com.myplayerr.service.MP3FileService;
import com.myplayerr.service.PlayerService;
import com.myplayerr.service.SettingService;
import com.myplayerr.view.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label songLabel;

    private final PlayerService playerService = new PlayerService();
    private final MP3FileService MP3FileService = new MP3FileService();
    private final SettingService settingService = new SettingService();
    private final SettingDAO settingDAO = new SettingDAO();

    @FXML
    public void initialize() {
        String musicPath = settingDAO.getSetting("mp3Path");

        ChansonViewBox.setController(this);
        if (musicPath != null) {
            MP3FileService.scanAndImportMusic(musicPath);
        }
    }

    @FXML
    private void chansonPrecedente(ActionEvent actionEvent) {
        //TODO
    }

    @FXML
    private void playPause(ActionEvent actionEvent) {
        playerService.playPauseSong();
    }

    @FXML
    private void chansonSuivante() {
        //TODO
    }

    @FXML
    private void stopChanson() {
        playerService.stopSong();
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

    public void setCurrentSong(String filePath, String titre) {
        playerService.playSong(filePath);

        songLabel.setText(titre);
    }

}
