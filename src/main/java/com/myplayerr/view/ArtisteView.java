package com.myplayerr.view;

import com.myplayerr.database.ArtisteDAO;
import com.myplayerr.model.Artiste;
import com.myplayerr.view.utils.AlbumArtisteView;
import com.myplayerr.view.utils.ViewUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ArtisteView {

    private BorderPane _pane;
    private ArtisteDAO _artisteDAO;
    private AlbumArtisteView _albumArtisteView;
    private AlbumView _albumView;

    public ArtisteView() {
    }

    public void setDependance(BorderPane pane, ArtisteDAO artisteDAO, AlbumArtisteView albumArtisteView, AlbumView albumView) {
        _pane = pane;
        _artisteDAO = artisteDAO;
        _albumArtisteView = albumArtisteView;
        _albumView = albumView;
    }

    public VBox getView() {
        VBox rootVBox = ViewUtils.createRootVBox("Artistes");

        FlowPane artistePane = ViewUtils.createFlowPane();

        ScrollPane scrollPane = ViewUtils.createScrollPane(artistePane);

        List<Artiste> artistes = _artisteDAO.getAllArtistes();

        for (Artiste artiste : artistes) {
            VBox artisteBox = _albumArtisteView.createEntityBox(artiste);
            artisteBox.setOnMouseClicked(event ->  _pane.setCenter(_albumView.getView(artiste)));
            artistePane.getChildren().add(artisteBox);
        }

        rootVBox.getChildren().addAll(scrollPane);
        return rootVBox;
    }
}
