package com.myplayerr.view.utils;

import com.myplayerr.model.EntityBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

public class EntityBoxView {

    public VBox createEntityBox(EntityBox entityBox) {
        VBox albumBox = new VBox();
        albumBox.setAlignment(Pos.CENTER);
        albumBox.setSpacing(5);
        albumBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        String imagePath = entityBox.getImagePath();
        File img = new File(imagePath);
        Image albumImage = new Image(img.toURI().toString());

        ImageView imageView = new ImageView(albumImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label albumLabel = new Label(entityBox.getNom());
        albumLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        albumBox.getChildren().addAll(imageView, albumLabel);
        return albumBox;
    }
}
