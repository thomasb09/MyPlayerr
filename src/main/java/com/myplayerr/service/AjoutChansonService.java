package com.myplayerr.service;

import javafx.concurrent.Task;

import java.util.List;

public class AjoutChansonService {

    private RechercheTitreService _rechercheTitreService;
    private DownloadChansonSevice _downloadChansonSevice;

    public void setDependance(RechercheTitreService rechercheTitreService, DownloadChansonSevice downloadChansonSevice) {
        _rechercheTitreService = rechercheTitreService;
        _downloadChansonSevice = downloadChansonSevice;
    }

    public Task<List<List<String>>> rechercheParTitreAsync(String query) {
        return _rechercheTitreService.rechercheYouTubeAsync(query);
    }

    public void ajouterChanson(String url) {
        try{
            _downloadChansonSevice.download(url);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
