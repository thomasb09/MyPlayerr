package com.myplayerr.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.List;

public class AjoutChansonService {

    private RechercheTitreService _rechercheTitreService;
    private DownloadChansonSevice _downloadChansonSevice;

    private final ObservableList<String> downloads = FXCollections.observableArrayList();

    public void setDependance(RechercheTitreService rechercheTitreService, DownloadChansonSevice downloadChansonSevice) {
        _rechercheTitreService = rechercheTitreService;
        _downloadChansonSevice = downloadChansonSevice;
    }

    public Task<List<List<String>>> rechercheParTitreAsync(String query) {
        return _rechercheTitreService.rechercheYouTubeAsync(query);
    }
    public ObservableList<String> getDownloads() {
        return downloads;
    }

    public Task<Void> ajouterChansonAsync(String songName, String rawUrl) {
        String searchUrl = "ytsearch:" + rawUrl;
        downloads.add(songName);

        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    _downloadChansonSevice.download(searchUrl);
                } finally {
                    javafx.application.Platform.runLater(() -> downloads.remove(songName));
                }
                return null;
            }
        };
    }
}
