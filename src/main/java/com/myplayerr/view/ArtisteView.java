package com.myplayerr.view;

import com.myplayerr.database.AlbumDAO;
import com.myplayerr.database.ArtisteDAO;
import com.myplayerr.model.Album;
import com.myplayerr.model.Artiste;
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

public class ArtisteView {

    private final BorderPane pane;

    public ArtisteView(BorderPane pane) {
        this.pane = pane;
    }

    public VBox getView() {
        VBox rootVBox = new VBox();
        rootVBox.setSpacing(10);
        rootVBox.setAlignment(Pos.TOP_CENTER);
        rootVBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Label title = new Label("Artistes");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        FlowPane artistePane = new FlowPane();
        artistePane.setHgap(20);
        artistePane.setVgap(20);
        artistePane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(artistePane);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        List<Artiste> artistes = ArtisteDAO.getAllArtistes();

        for (Artiste artiste : artistes) {
            VBox artisteBox = createArtisteBox(artiste);
            artisteBox.setOnMouseClicked(event -> showAlbumsByArtiste(artiste));
            artistePane.getChildren().add(artisteBox);
        }

        rootVBox.getChildren().addAll(title, scrollPane);
        return rootVBox;
    }

    private VBox createArtisteBox(Artiste artiste) {
        VBox artisteBox = new VBox();
        artisteBox.setSpacing(10);
        artisteBox.setAlignment(Pos.CENTER);
        artisteBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        String imagePath = "/images/artistes/" + "img.png"; // artiste.getImagePath();
        URL imageUrl = getClass().getResource(imagePath);
        Image artisteImage = new Image(imageUrl.toExternalForm());

        ImageView imageView = new ImageView(artisteImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label artisteLabel = new Label(artiste.getNom());
        artisteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        artisteBox.getChildren().addAll(imageView, artisteLabel);
        return artisteBox;
    }

    private void showAlbumsByArtiste(Artiste artiste) {
        List<Album> albums = AlbumDAO.getAlbumsByArtiste(artiste.getId());

        FlowPane albumPane = new FlowPane();
        albumPane.setHgap(20);
        albumPane.setVgap(20);
        albumPane.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(albumPane);
        scrollPane.setFitToWidth(true);
        AlbumPaneView albumPaneView = new AlbumPaneView(albumPane, scrollPane);

        for (Album album : albums) {
            VBox albumBox = createAlbumBox(album);
            albumBox.setOnMouseClicked(event -> AlbumViewUtils.showAlbumSongs(pane, album));
            albumPaneView.albumPane().getChildren().add(albumBox);
        }

        VBox albumView = new VBox();
        albumView.setSpacing(10);
        albumView.setAlignment(Pos.TOP_CENTER);
        albumView.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");
        albumView.getChildren().addAll(new Label("Albums de " + artiste.getNom()), albumPaneView.scrollPane());

        pane.setCenter(albumView);
    }

    private record AlbumPaneView(FlowPane albumPane, ScrollPane scrollPane) {
    }

    private VBox createAlbumBox(Album album) {
        VBox albumBox = new VBox();
        albumBox.setAlignment(Pos.CENTER);
        albumBox.setSpacing(10);
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
        albumLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-text-alignment: center;");
        albumLabel.setWrapText(true);

        albumBox.getChildren().addAll(imageView, albumLabel);

        return albumBox;
    }

}
