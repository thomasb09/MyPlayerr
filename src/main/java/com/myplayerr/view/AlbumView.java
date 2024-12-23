package com.myplayerr.view;

import com.myplayerr.database.AlbumDAO;
import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;
import com.myplayerr.view.utils.EntityBoxView;
import com.myplayerr.view.utils.ViewUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class AlbumView {

    private BorderPane _pane;
    private AlbumDAO _albumDAO;
    private ChansonView _chansonView;
    private EntityBoxView _entityBoxView;

    public AlbumView() {
    }

    public void setDependance(BorderPane pane, AlbumDAO albumDAO, ChansonView chansonView, EntityBoxView entityBoxView) {
        _pane = pane;
        _albumDAO = albumDAO;
        _chansonView = chansonView;
        _entityBoxView = entityBoxView;
    }

    public VBox getView(Artiste artiste) {
        List<Album> albums;
        albums = (null == artiste) ?
                _albumDAO.getAllAlbums() :
                _albumDAO.getAlbumsByArtiste(artiste.getId());

        VBox rootVBox = ViewUtils.createRootVBox((null == artiste) ? "Albums" : "Albums de : " + artiste.getNom());

        FlowPane albumPane = ViewUtils.createFlowPane();

        ScrollPane scrollPane = ViewUtils.createScrollPane(albumPane);

        for (Album album : albums) {
            VBox albumBox = _entityBoxView.createEntityBox(album);
            albumBox.setOnMouseClicked(event -> _pane.setCenter(_chansonView.getView(album)));
            albumPane.getChildren().add(albumBox);
        }

        rootVBox.getChildren().addAll(scrollPane);
        return rootVBox;
    }
}
