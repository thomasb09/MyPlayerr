package com.myplayerr.view;

import com.myplayerr.model.Chanson;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class RechercheView {

    private ChansonViewBox _chansonViewBox;

    public RechercheView() {
    }

    public void setDependance(ChansonViewBox chansonViewBox) {
        _chansonViewBox = chansonViewBox;
    }

    public VBox getView() {
        VBox rootVBox = new VBox();
        rootVBox.setSpacing(15);
        rootVBox.setAlignment(Pos.TOP_CENTER);
        rootVBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Label titleLabel = new Label("Rechercher une Chanson");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox searchBox = new HBox();
        searchBox.setSpacing(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Entrez un nom, titre ou album...");
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
            List<Chanson> results = searchChansons(query);
            resultList.getChildren().clear();
            results.stream()
                    .map(_chansonViewBox::createChansonBox)
                    .forEach(resultList.getChildren()::add);
        });

        searchBox.getChildren().addAll(searchField, searchButton);
        rootVBox.getChildren().addAll(titleLabel, searchBox, scrollPane);

        return rootVBox;
    }

    private List<Chanson> searchChansons(String query) {
        List<Chanson> allChansons = new com.myplayerr.database.ChansonDAO().getAllChansons();
        return allChansons.stream()
                .filter(chanson -> chanson.getTitre().toLowerCase().contains(query.toLowerCase())
                        || chanson.getArtiste().getNom().toLowerCase().contains(query.toLowerCase())
                        || chanson.getAlbum().getNom().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}
