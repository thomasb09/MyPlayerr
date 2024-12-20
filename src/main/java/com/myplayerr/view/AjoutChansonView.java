package com.myplayerr.view;

import com.myplayerr.service.AjoutChansonService;
import com.myplayerr.view.utils.ViewUtils;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class AjoutChansonView {

    private AjoutChansonService _ajoutChansonService;

    public void setDependance(AjoutChansonService ajoutChansonService) {
        _ajoutChansonService = ajoutChansonService;
    }

    public VBox getView() {
        VBox rootVBox = ViewUtils.createRootVBox("Ajouter une Chanson");

        TextField searchField = new TextField();
        searchField.setPromptText("Entrez un titre ou un critère...");

        Button searchButton = new Button("Rechercher");

        VBox resultList = ViewUtils.createResultList();
        ScrollPane scrollPane = ViewUtils.createScrollPane(resultList);

        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            resultList.getChildren().clear();

            ProgressIndicator loadingIndicator = new ProgressIndicator();
            loadingIndicator.setMaxSize(50, 50);
            VBox loadingContainer = new VBox(loadingIndicator);
            loadingContainer.setAlignment(Pos.CENTER);
            loadingContainer.setStyle("-fx-padding: 20;");

            resultList.getChildren().add(loadingContainer);

            Task<List<List<String>>> searchTask = _ajoutChansonService.rechercheParTitreAsync(query);

            searchTask.setOnSucceeded(workerStateEvent -> {
                resultList.getChildren().clear();
                List<List<String>> results = searchTask.getValue();

                if (results.isEmpty()) {
                    Label noResultsLabel = new Label("Aucun résultat trouvé.");
                    noResultsLabel.setStyle("-fx-text-fill: white;");
                    resultList.getChildren().add(noResultsLabel);
                } else {
                    results.forEach(result -> resultList.getChildren().add(createResultBox(result)));
                }
            });

            searchTask.setOnFailed(workerStateEvent -> {
                resultList.getChildren().clear();
                Throwable exception = searchTask.getException();
                Label errorLabel = new Label("Erreur : " + exception.getMessage());
                errorLabel.setStyle("-fx-text-fill: white;");
                resultList.getChildren().add(errorLabel);
            });

            Thread thread = new Thread(searchTask);
            thread.setDaemon(true);
            thread.start();
        });

        rootVBox.getChildren().addAll(ViewUtils.createSearchBox(searchField, searchButton), scrollPane);

        return rootVBox;
    }

    private HBox createResultBox(List<String> result) {
        HBox resultBox = new HBox();
        resultBox.setSpacing(20);
        resultBox.setAlignment(Pos.CENTER_LEFT);
        resultBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        String resultText = result.get(0);
        String url = result.get(2);
        String imageUrl = result.size() > 1 ? result.get(1) : "";

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

        if (!imageUrl.isEmpty()) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() throws Exception {
                    return new Image(imageUrl, 50, 50, true, true); // Télécharger l'image
                }
            };

            loadImageTask.setOnSucceeded(event -> {
                thumbnail.setImage(loadImageTask.getValue());
                imageContainer.getChildren().setAll(thumbnail);
            });

            new Thread(loadImageTask).start();
        }

        Button addButton = new Button("Ajouter");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addButton.setOnAction(event -> {
            _ajoutChansonService.ajouterChanson(url);
        });

        resultBox.getChildren().addAll(imageContainer, resultLabel, addButton);
        return resultBox;
    }
}
