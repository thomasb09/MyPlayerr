package com.myplayerr.view.utils;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class RechercheViewBox {

    private TextField searchField;
    private Button searchButton;
    private Consumer<String> onSearch;

    public RechercheViewBox() {
        searchField = new TextField();
        searchField.setPromptText("Entrez un titre ou un critère...");
        searchField.getStyleClass().add("search-field");

        searchButton = new Button("Rechercher");
        searchButton.getStyleClass().add("search-button");

        HBox searchBox = new HBox(15, searchField, searchButton);
        searchBox.getStyleClass().add("search-box");

        searchButton.setOnAction(event -> {
            if (onSearch != null) {
                String query = searchField.getText();
                onSearch.accept(query);
            }
        });
    }

    public void setOnSearch(Consumer<String> onSearch) {
        this.onSearch = onSearch;
    }

    public Node getNode() {
        return searchField.getParent();
    }
}
