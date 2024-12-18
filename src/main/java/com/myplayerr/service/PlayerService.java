package com.myplayerr.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class PlayerService {

    private MediaPlayer _currentPlayer;

    private boolean _isPlaying = false;
    private String _currentSong;

    public void playSong(String filePath) {
        if (_currentPlayer != null) {
            _currentPlayer.stop();
        }

        try {
            File mp3File = new File(filePath);
            if (!mp3File.exists()) {
                return;
            }

            Media media = new Media(mp3File.toURI().toString());
            _currentPlayer = new MediaPlayer(media);
            _currentPlayer.play();
            _currentSong = filePath;
            _isPlaying = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la lecture du fichier : " + filePath);
        }
    }

    public void playPauseSong(){
        if (_currentPlayer != null) {
            if (_isPlaying){
                _currentPlayer.pause();
                _isPlaying = false;
            }
            else {
                _currentPlayer.play();
                _isPlaying = true;
            }
        }
    }

    public void stopSong() {
        if (_currentPlayer != null) {
            _currentPlayer.stop();
            _isPlaying = false;
        }
    }

    public String get_currentSong(){
        return _currentSong;
    }
}
