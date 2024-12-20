package com.myplayerr.view.utils;

import com.myplayerr.service.AjoutChansonService;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ResultViewBox {

    private HBox resultBox;
    private List<String> result;
    private AjoutChansonService _ajoutChansonService;

    public ResultViewBox(List<String> result, AjoutChansonService ajoutChansonService) {
        this.result = result;
        this._ajoutChansonService = ajoutChansonService;
        createBox();
    }

    private void createBox() {
        resultBox = new HBox();
        resultBox.setSpacing(20);
        resultBox.setAlignment(Pos.CENTER_LEFT);
        resultBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        String resultText = result.get(0);
        String imageUrl = result.get(1);
        String url = result.get(2);

        Label resultLabel = new Label(resultText);
        resultLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        ImageView thumbnail = new ImageView();
        thumbnail.setFitWidth(50);
        thumbnail.setFitHeight(50);

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(50, 50);

        VBox imageContainer = new VBox(loadingIndicator);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefSize(50, 50);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    return new Image(imageUrl, 50, 50, true, true);
                }
            };

            loadImageTask.setOnSucceeded(event -> {
                thumbnail.setImage(loadImageTask.getValue());
                imageContainer.getChildren().setAll(thumbnail);
            });

            loadImageTask.setOnFailed(event -> {
                imageContainer.getChildren().clear();
                Label errorImageLabel = new Label("Image\nnon\ndisponible");
                errorImageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
                imageContainer.getChildren().add(errorImageLabel);
            });

            new Thread(loadImageTask).setDaemon(true);
            new Thread(loadImageTask).start();
        }

        Button addButton = new Button("Ajouter");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addButton.setOnAction(event -> _ajoutChansonService.ajouterChanson(url));

        resultBox.getChildren().addAll(imageContainer, resultLabel, addButton);
    }

    public HBox getNode() {
        return resultBox;
    }
}
