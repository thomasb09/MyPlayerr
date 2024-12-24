package com.myplayerr.view.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;

public class ViewUtils {

    public static VBox createRootVBox(String title) {
        VBox rootVBox = new VBox();
        rootVBox.setSpacing(15);
        rootVBox.setAlignment(Pos.TOP_CENTER);
        rootVBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        rootVBox.getChildren().add(titleLabel);

        return rootVBox;
    }

    public static HBox createSearchBox(TextField searchField, Button searchButton) {
        HBox searchBox = new HBox();
        searchBox.setSpacing(10);
        searchBox.setAlignment(Pos.CENTER);

        searchField.setStyle("-fx-font-size: 14px; -fx-pref-width: 300;");
        searchButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;");

        searchBox.getChildren().addAll(searchField, searchButton);

        return searchBox;
    }

    public static ScrollPane createScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        return scrollPane;
    }

    public static VBox createResultList() {
        VBox resultList = new VBox();
        resultList.setSpacing(10);
        resultList.setAlignment(Pos.TOP_LEFT);
        return resultList;
    }

    public static ScrollPane createScrollPane(FlowPane flowPane) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(600);
        scrollPane.getStyleClass().add("scroll-pane");

        return scrollPane;
    }

    public static FlowPane createFlowPane() {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(20);
        flowPane.setVgap(20);
        flowPane.setAlignment(Pos.CENTER);
        return flowPane;
    }

    public static ImageView createImage(String imagePath){
        Image albumImage;

        if (imagePath == null || imagePath.isEmpty() || !new File(imagePath).exists()) {
            URL defaultImageUrl = ViewUtils.class.getResource("/images/albums/defaultAlbum.png");
            albumImage = new Image(defaultImageUrl != null ? defaultImageUrl.toString() : "");
        } else {
            albumImage = new Image(new File(imagePath).toURI().toString());
        }

        return new ImageView(albumImage);
    }
}
