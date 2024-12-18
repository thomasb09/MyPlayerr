package com.myplayerr.view;

import com.myplayerr.view.utils.ViewUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class AjoutChansonView {

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
            List<String> results = searchSongsFromAPI(query); // Simulation
            results.forEach(result -> resultList.getChildren().add(createResultBox(result)));
        });

        rootVBox.getChildren().addAll(ViewUtils.createSearchBox(searchField, searchButton), scrollPane);

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
