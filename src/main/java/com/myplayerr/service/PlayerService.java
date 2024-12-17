package com.myplayerr.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class PlayerService {

    private MediaPlayer currentPlayer;

    private boolean isPlaying = false;

    public void playSong(String filePath) {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }

        try {
            File mp3File = new File(filePath);
            if (!mp3File.exists()) {
                return;
            }

            Media media = new Media(mp3File.toURI().toString());
            currentPlayer = new MediaPlayer(media);
            currentPlayer.play();
            isPlaying = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la lecture du fichier : " + filePath);
        }
    }

    public void playPauseSong(){
        if (currentPlayer != null) {
            if (isPlaying){
                currentPlayer.pause();
                isPlaying = false;
            }
            else {
                currentPlayer.play();
                isPlaying = true;
            }
        }
    }

    public void stopSong() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            isPlaying = false;
        }
    }
}
