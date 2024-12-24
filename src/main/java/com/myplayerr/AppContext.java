package com.myplayerr;

import com.myplayerr.controller.MainController;
import com.myplayerr.database.*;
import com.myplayerr.service.*;
import com.myplayerr.view.*;
import com.myplayerr.view.utils.ChansonViewBox;
import com.myplayerr.view.utils.EntityBoxView;

public class AppContext {

    private static AppContext instance;

    private final AudDService audDService;
    private final PlayerService playerService;
    private final MP3FileService mp3FileService;
    private final SettingService settingService;
    private final AjoutChansonService ajoutChansonService;
    private final RechercheTitreService rechercheTitreService;
    private final DownloadChansonSevice downloadChansonSevice;
    private final FFmpegConvertisseurService fFmpegConvertisseurService;
    private final RechercheChansonService rechercheChansonService;
    private final ImageService imageService;

    private final PlaylistView playlistView;
    private final ArtisteView artisteView;
    private final ChansonView chansonView;
    private final AlbumView albumView;
    private final AjoutChansonView ajoutChansonView;
    private final RechercheView rechercheView;
    private final ChansonViewBox chansonViewBox;
    private final EntityBoxView entityBoxView;
    private final DownloadView downloadView;

    private final AlbumDAO albumDAO;
    private final ArtisteDAO artisteDAO;
    private final ChansonDAO chansonDAO;
    private final PlaylistDAO playlistDAO;
    private final SettingDAO settingDAO;

    private final MainController mainController;

    private AppContext() {
        // Initialisation des DAO
        albumDAO = new AlbumDAO();
        artisteDAO = new ArtisteDAO();
        chansonDAO = new ChansonDAO();
        playlistDAO = new PlaylistDAO();
        settingDAO = new SettingDAO();

        // Initialisation des services
        audDService = new AudDService();
        playerService = new PlayerService();
        mp3FileService = new MP3FileService();
        settingService = new SettingService();
        ajoutChansonService = new AjoutChansonService();
        rechercheTitreService = new RechercheTitreService();
        downloadChansonSevice = new DownloadChansonSevice();
        fFmpegConvertisseurService = new FFmpegConvertisseurService();
        rechercheChansonService = new RechercheChansonService();
        imageService = new ImageService();

        // Initialisation des vues
        playlistView = new PlaylistView();
        artisteView = new ArtisteView();
        chansonView = new ChansonView();
        albumView = new AlbumView();
        ajoutChansonView = new AjoutChansonView();
        rechercheView = new RechercheView();
        chansonViewBox = new ChansonViewBox();
        entityBoxView = new EntityBoxView();
        downloadView = new DownloadView();

        // Initialisation des contolleur
        mainController = new MainController(
                playerService,
                settingService,
                playlistView,
                artisteView,
                chansonView,
                albumView,
                ajoutChansonView,
                rechercheView,
                downloadView
        );
    }

    public void setDependance() {
        settingService.setDependance(settingDAO, mp3FileService);
        mp3FileService.setDependance(chansonDAO, artisteDAO, audDService, imageService, albumDAO);
        albumView.setDependance(mainController.getMainPane(), albumDAO, chansonView, entityBoxView);
        chansonDAO.setDependance(albumDAO);
        playlistDAO.setDependance(chansonDAO);
        rechercheView.setDependance(chansonViewBox, rechercheChansonService);
        rechercheChansonService.setDependance(chansonDAO);
        chansonView.setDependance(chansonViewBox, chansonDAO);
        albumDAO.setDependance(artisteDAO);
        artisteView.setDependance(mainController.getMainPane(), artisteDAO, entityBoxView, albumView);
        chansonViewBox.setDependance(mainController);
        ajoutChansonView.setDependance(ajoutChansonService);
        ajoutChansonService.setDependance(rechercheTitreService, downloadChansonSevice);
        downloadChansonSevice.setDependance(settingDAO, mp3FileService, fFmpegConvertisseurService);
        downloadView.setDependance(ajoutChansonService);

        mainController.setDependance();

        String musicPath = settingDAO.getSetting("mp3Path");

        if (musicPath != null) {
            mp3FileService.scanAndImportMusic(musicPath);
        }
    }

    public static synchronized AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public MainController getMainController() {
        return mainController;
    }
}
