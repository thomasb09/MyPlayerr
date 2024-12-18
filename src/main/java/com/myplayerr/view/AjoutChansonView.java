package com.myplayerr.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class AjoutChansonView {

    private BorderPane _pane;

    public AjoutChansonView() {
    }

    public void setPane(BorderPane pane){
        _pane = pane;
    }

    public VBox getView() {
        VBox rootVBox = new VBox();
        rootVBox.setSpacing(15);
        rootVBox.setAlignment(Pos.TOP_CENTER);
        rootVBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Label titleLabel = new Label("Ajouter une Chanson");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox searchBox = new HBox();
        searchBox.setSpacing(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Entrez un titre ou un critère...");
        searchField.setStyle("-fx-font-size: 14px; -fx-pref-width: 300;");

        Button searchButton = new Button("Rechercher");
        searchButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;");

        VBox resultList = new VBox();
        resultList.setSpacing(10);
        resultList.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(resultList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            resultList.getChildren().clear();
            List<String> results = searchSongsFromAPI(query); // Simulation
            results.forEach(result -> resultList.getChildren().add(createResultBox(result)));
        });

        searchBox.getChildren().addAll(searchField, searchButton);
        rootVBox.getChildren().addAll(titleLabel, searchBox, scrollPane);

        return rootVBox;
    }

    private List<String> searchSongsFromAPI(String query) {
        // TODO: Appeler une API pour récupérer les chansons en fonction de la requête
        return Arrays.asList(
                "Chanson 1 - Artiste A",
                "Chanson 2 - Artiste B",
                "Chanson 3 - Artiste C"
        );
    }

    private HBox createResultBox(String result) {
        HBox resultBox = new HBox();
        resultBox.setSpacing(20);
        resultBox.setAlignment(Pos.CENTER_LEFT);
        resultBox.setStyle("-fx-padding: 10; -fx-background-color: #333333; -fx-background-radius: 8;");

        Label resultLabel = new Label(result);
        resultLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Button addButton = new Button("Ajouter");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addButton.setOnAction(event -> {
            // TODO: Ajouter la chanson sélectionnée dans la base de données
            System.out.println("Chanson ajoutée : " + result);
        });

        resultBox.getChildren().addAll(resultLabel, addButton);
        return resultBox;
    }
}
