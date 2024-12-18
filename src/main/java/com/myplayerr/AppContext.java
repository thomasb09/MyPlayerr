package com.myplayerr;

import com.myplayerr.controller.MainController;
import com.myplayerr.database.*;
import com.myplayerr.service.MP3FileService;
import com.myplayerr.service.PlayerService;
import com.myplayerr.service.SettingService;
import com.myplayerr.view.*;
import com.myplayerr.view.utils.AlbumArtisteView;
import com.myplayerr.view.utils.ChansonViewBox;

public class AppContext {

    private static AppContext instance;

    private final PlayerService playerService;
    private final MP3FileService mp3FileService;
    private final SettingService settingService;

    private final PlaylistView playlistView;
    private final ArtisteView artisteView;
    private final ChansonView chansonView;
    private final AlbumView albumView;
    private final AjoutChansonView ajoutChansonView;
    private final RechercheView rechercheView;
    private final ChansonViewBox chansonViewBox;
    private final AlbumArtisteView albumArtisteView;

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
        playerService = new PlayerService();
        mp3FileService = new MP3FileService();
        settingService = new SettingService();

        // Initialisation des vues
        playlistView = new PlaylistView();
        artisteView = new ArtisteView();
        chansonView = new ChansonView();
        albumView = new AlbumView();
        ajoutChansonView = new AjoutChansonView();
        rechercheView = new RechercheView();
        chansonViewBox = new ChansonViewBox();
        albumArtisteView = new AlbumArtisteView();

        // Initialisation des contolleur
        mainController = new MainController(
                playerService,
                mp3FileService,
                settingService,
                playlistView,
                artisteView,
                chansonView,
                albumView,
                ajoutChansonView,
                rechercheView,
                chansonViewBox,
                settingDAO
        );
    }

    public void setDependance() {
        albumView.setDependance(mainController.getMainPane(), albumDAO, chansonView, albumArtisteView);
        chansonDAO.setDependance(albumDAO);
        playlistDAO.setDependance(chansonDAO);
        rechercheView.setDependance(chansonViewBox);
        chansonView.setDependance(chansonViewBox, chansonDAO);
        albumDAO.setDependance(artisteDAO);
        artisteView.setDependance(mainController.getMainPane(), artisteDAO, albumArtisteView, albumView);
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
