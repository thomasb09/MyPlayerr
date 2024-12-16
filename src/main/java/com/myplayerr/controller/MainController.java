package com.myplayerr.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MainController {

    @FXML
    private ListView<String> songList;

    @FXML
    private Button playButton;

    @FXML
    public void initialize() {
        songList.getItems().addAll("Chanson 1", "Chanson 2", "Chanson 3");
    }

    @FXML
    public void handlePlay() {
        String selectedSong = songList.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            System.out.println("Lecture de : " + selectedSong);
        } else {
            System.out.println("Aucune chanson sélectionnée");
        }
    }
}
