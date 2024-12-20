package com.myplayerr.controller;

import com.myplayerr.service.PlayerService;
import com.myplayerr.service.SettingService;
import com.myplayerr.view.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    BorderPane mainPane;

    @FXML
    Label songLabel;

    private PlayerService _playerService;
    private SettingService _settingService;
    private PlaylistView _playlistView;
    private ArtisteView _artisteView;
    private ChansonView _chansonView;
    private AlbumView _albumView;
    private AjoutChansonView _ajoutChansonView;
    private RechercheView _rechercheView;

    public MainController() {
        // Le constructeur par défaut est requis par JavaFX FXMLLoader
    }

    public MainController(PlayerService playerService,
                          SettingService settingService,
                          PlaylistView playlistView,
                          ArtisteView artisteView,
                          ChansonView chansonView,
                          AlbumView albumView,
                          AjoutChansonView ajoutChansonView,
                          RechercheView rechercheView) {
        _playerService = playerService;
        _settingService = settingService;
        _playlistView = playlistView;
        _artisteView = artisteView;
        _chansonView = chansonView;
        _albumView = albumView;
        _ajoutChansonView = ajoutChansonView;
        _rechercheView = rechercheView;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void chansonPrecedente() {
        //TODO
    }

    @FXML
    void playPause() {
        _playerService.playPauseSong();
    }

    @FXML
    private void chansonSuivante() {
        //TODO
    }

    @FXML
    void stopChanson() {
        _playerService.stopSong();
    }

    @FXML
    void showPlaylist() {
        mainPane.setCenter(_playlistView.getView());
    }

    @FXML
    void showArtistes() {
        mainPane.setCenter(_artisteView.getView());
    }

    @FXML
    void showAlbums() {
        mainPane.setCenter(_albumView.getView(null));
    }

    @FXML
    void showChansons() {
        mainPane.setCenter(_chansonView.getView(null));
    }

    @FXML
    public void showRechercheView() {
        mainPane.setCenter(_rechercheView.getView());
    }

    @FXML
    public void showAjoutChansonView() {
        mainPane.setCenter(_ajoutChansonView.getView());
    }

    @FXML
    void changePathMusic() {
        _settingService.setPathMusic();
    }

    @FXML
    private void showSettings() {
    }

    @FXML
    void showAbout() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About MyPlayerr");
        aboutAlert.setHeaderText("MyPlayerr - Gestionnaire MP3");
        aboutAlert.setContentText("Version 1.0.0\nDéveloppé par Thomas Bisson.");
        aboutAlert.showAndWait();
    }

    @FXML
    void exitApp() {
        System.exit(0);
    }

    public void setCurrentSong(String filePath, String titre) {
        _playerService.playSong(filePath);
        songLabel.setText(titre);
    }

    public BorderPane getMainPane(){
        return mainPane;
    }
}
