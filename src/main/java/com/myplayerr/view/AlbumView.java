package com.myplayerr.view;

import com.myplayerr.database.AlbumDAO;
import com.myplayerr.model.Album;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;

public class AlbumView {

    private final BorderPane pane;

    public AlbumView(BorderPane pane) {
        this.pane = pane;
    }

    public VBox getView() {
        VBox rootVBox = new VBox();
        rootVBox.setSpacing(10);
        rootVBox.setAlignment(Pos.TOP_CENTER);
        rootVBox.setStyle("-fx-background-color: #2b2b2b;");

        Label title = new Label("Albums");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        FlowPane albumPane = new FlowPane();
        albumPane.setHgap(20);
        albumPane.setVgap(20);
        albumPane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(albumPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);
        scrollPane.getStyleClass().add("scroll-pane");

        List<Album> albums = AlbumDAO.getAllAlbums();

        for (Album album : albums) {
            VBox albumBox = createAlbumBox(album);
            albumBox.setOnMouseClicked(event -> AlbumViewUtils.showAlbumSongs(pane, album));
            albumPane.getChildren().add(albumBox);
        }

        rootVBox.getChildren().addAll(title, scrollPane);
        return rootVBox;
    }

    private VBox createAlbumBox(Album album) {
        VBox albumBox = new VBox();
        albumBox.setAlignment(Pos.CENTER);
        albumBox.setSpacing(5);
        albumBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        String imagePath = "/images/albums/" + "img.png"; // album.getImagePath();
        URL imageUrl = getClass().getResource(imagePath);
        Image albumImage = (imageUrl != null)
                ? new Image(imageUrl.toExternalForm())
                : new Image(getClass().getResource("/images/default.png").toExternalForm());

        ImageView imageView = new ImageView(albumImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label albumLabel = new Label(album.getNom());
        albumLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        albumBox.getChildren().addAll(imageView, albumLabel);
        return albumBox;
    }
}
