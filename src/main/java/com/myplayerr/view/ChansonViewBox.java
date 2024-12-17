package com.myplayerr.view;

import com.myplayerr.controller.MainController;
import com.myplayerr.model.Chanson;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;

public class ChansonViewBox {

    public static MainController controller;

    public static void setController(MainController controller){
        ChansonViewBox.controller = controller;
    }

    public static HBox createChansonBox(Chanson chanson) {
        HBox chansonBox = new HBox();
        chansonBox.setSpacing(20);
        chansonBox.setAlignment(Pos.CENTER_LEFT);
        chansonBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        Label songLabel = new Label(chanson.getTitre() + " - " + chanson.getDuree());
        songLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        String imagePath = "/images/albums/" + "img.png";//chanson.getAlbum().getImagePath();
        URL imageUrl = ChansonViewBox.class.getResource(imagePath);
        Image albumImage =  new Image(imageUrl.toExternalForm());
        ImageView smallAlbumImage = new ImageView(albumImage);
        smallAlbumImage.setFitWidth(40);
        smallAlbumImage.setFitHeight(40);
        smallAlbumImage.setPreserveRatio(true);

        chansonBox.getChildren().addAll(songLabel, smallAlbumImage);

        chansonBox.setOnMouseClicked(event -> controller.setCurrentSong(chanson.getCheminFichier(), chanson.getTitre()));

        return chansonBox;
    }
}
