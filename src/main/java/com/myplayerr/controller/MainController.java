package com.myplayerr.controller;

import com.myplayerr.database.SettingDAO;
import com.myplayerr.service.MP3FileService;
import com.myplayerr.service.PlayerService;
import com.myplayerr.service.SettingService;
import com.myplayerr.view.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label songLabel;

    private PlayerService _playerService;
    private MP3FileService _MP3FileService;
    private SettingService _settingService;
    private PlaylistView _playlistView;
    private ArtisteView _artisteView;
    private ChansonView _chansonView;
    private AlbumView _albumView;
    private AjoutChansonView _ajoutChansonView;
    private RechercheView _rechercheView;
    private ChansonViewBox _chansonViewBox;
    private SettingDAO _settingDAO;

    public MainController() {
        // Le constructeur par défaut est requis par JavaFX FXMLLoader
    }

    public MainController(PlayerService playerService,
                          MP3FileService MP3FileService,
                          SettingService settingService,
                          PlaylistView playlistView,
                          ArtisteView artisteView,
                          ChansonView chansonView,
                          AlbumView albumView,
                          AjoutChansonView ajoutChansonView,
                          RechercheView rechercheView,
                          ChansonViewBox chansonViewBox,
                          SettingDAO settingDAO) {
        _playerService = playerService;
        _MP3FileService = MP3FileService;
        _settingService = settingService;
        _playlistView = playlistView;
        _artisteView = artisteView;
        _chansonView = chansonView;
        _albumView = albumView;
        _ajoutChansonView = ajoutChansonView;
        _rechercheView = rechercheView;
        _chansonViewBox = chansonViewBox;
        _settingDAO = settingDAO;
    }

    @FXML
    public void initialize() {
        String musicPath = _settingDAO.getSetting("mp3Path");

        _chansonViewBox.setController(this);
        if (musicPath != null) {
            _MP3FileService.scanAndImportMusic(musicPath);
        }
    }

    @FXML
    private void chansonPrecedente() {
        //TODO
    }

    @FXML
    private void playPause() {
        _playerService.playPauseSong();
    }

    @FXML
    private void chansonSuivante() {
        //TODO
    }

    @FXML
    private void stopChanson() {
        _playerService.stopSong();
    }

    @FXML
    private void showPlaylist() {
        mainPane.setCenter(_playlistView.getView());
    }

    @FXML
    private void showArtistes() {
        mainPane.setCenter(_artisteView.getView());
    }

    @FXML
    private void showAlbums() {
        mainPane.setCenter(_albumView.getView());
    }

    @FXML
    private void showChansons() {
        mainPane.setCenter(_chansonView.getView());
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
    private void changePathMusic() {
        _settingService.setPathMusic();
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
        _playerService.playSong(filePath);
        songLabel.setText(titre);
    }

    public BorderPane getMainPane(){
        return mainPane;
    }
}
