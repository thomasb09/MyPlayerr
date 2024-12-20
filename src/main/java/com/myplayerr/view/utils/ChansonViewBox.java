package com.myplayerr.view.utils;

import com.myplayerr.controller.MainController;
import com.myplayerr.model.Chanson;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;

public class ChansonViewBox {

    public MainController _controller;

    public void setDependance(MainController controller){
        _controller = controller;
    }

    public HBox createChansonBox(Chanson chanson) {
        HBox chansonBox = new HBox();
        chansonBox.setSpacing(20);
        chansonBox.setAlignment(Pos.CENTER_LEFT);
        chansonBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        Label songLabel = new Label(chanson.getTitre() + " - " + chanson.getDuree());
        songLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        String imagePath = chanson.getAlbum().getImagePath();
        File img = new File(imagePath);
        Image albumImage = new Image(img.toURI().toString());
        ImageView smallAlbumImage = new ImageView(albumImage);
        smallAlbumImage.setFitWidth(40);
        smallAlbumImage.setFitHeight(40);
        smallAlbumImage.setPreserveRatio(true);

        chansonBox.getChildren().addAll(songLabel, smallAlbumImage);

        chansonBox.setOnMouseClicked(event -> _controller.setCurrentSong(chanson.getCheminFichier(), chanson.getTitre()));

        return chansonBox;
    }
}
