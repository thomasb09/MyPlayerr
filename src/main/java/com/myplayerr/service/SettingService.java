package com.myplayerr.service;

import com.myplayerr.database.SettingDAO;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SettingService {

    private final SettingDAO settingsDAO = new SettingDAO();
    private final ChansonService chansonService = new ChansonService();

    public void setPathMusic() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select MP3 Folder");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();

            settingsDAO.saveSetting("mp3Path", selectedPath);

            chansonService.scanAndImportMusic(selectedPath);
        }
    }
}