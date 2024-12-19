package com.myplayerr.view;

import com.myplayerr.model.Chanson;
import com.myplayerr.view.utils.ChansonViewBox;
import com.myplayerr.view.utils.ViewUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class RechercheView {

    private ChansonViewBox _chansonViewBox;

    public void setDependance(ChansonViewBox chansonViewBox) {
        _chansonViewBox = chansonViewBox;
    }

    public VBox getView() {
        VBox rootVBox = ViewUtils.createRootVBox("Rechercher une Chanson");

        TextField searchField = new TextField();
        searchField.setPromptText("Entrez un nom, titre ou album...");

        Button searchButton = new Button("Rechercher");

        VBox resultList = ViewUtils.createResultList();
        ScrollPane scrollPane = ViewUtils.createScrollPane(resultList);

        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            List<Chanson> results = searchChansons(query);
            resultList.getChildren().clear();
            results.stream()
                    .map(_chansonViewBox::createChansonBox)
                    .forEach(resultList.getChildren()::add);
        });

        rootVBox.getChildren().addAll(ViewUtils.createSearchBox(searchField, searchButton), scrollPane);

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
