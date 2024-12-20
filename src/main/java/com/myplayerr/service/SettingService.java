package com.myplayerr.service;

import com.myplayerr.database.SettingDAO;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SettingService {

    private SettingDAO _settingsDAO;
    private MP3FileService _MP3FileService;

    public void setDependance(SettingDAO settingsDAO, MP3FileService mp3FileService) {
        _settingsDAO = settingsDAO;
        _MP3FileService = mp3FileService;
    }

    public void setPathMusic() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select MP3 Folder");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String selectedPath = selectedDirectory.getAbsolutePath();

            _settingsDAO.saveSetting("mp3Path", selectedPath);

            _MP3FileService.scanAndImportMusic(selectedPath);
        }
    }
}
