package com.myplayerr.view;

import com.myplayerr.database.AlbumDAO;
import com.myplayerr.model.Album;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class AlbumView {
    public VBox getView() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label title = new Label("Albums");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        AlbumDAO albumDAO = new AlbumDAO();
        List<Album> albums = albumDAO.getAllAlbums();

        for (Album album : albums) {
            listView.getItems().add(album.getNom() + " (par " + album.getArtiste().getNom() + ")");
        }

        vbox.getChildren().addAll(title, listView);
        return vbox;
    }
}
