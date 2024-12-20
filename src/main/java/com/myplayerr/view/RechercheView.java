package com.myplayerr.view;

import com.myplayerr.model.Chanson;
import com.myplayerr.service.RechercheChansonService;
import com.myplayerr.view.utils.ChansonViewBox;
import com.myplayerr.view.utils.ViewUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class RechercheView {

    private ChansonViewBox _chansonViewBox;
    private RechercheChansonService _rechercheChansonService;

    public void setDependance(ChansonViewBox chansonViewBox, RechercheChansonService rechercheChansonService) {
        _chansonViewBox = chansonViewBox;
        _rechercheChansonService = rechercheChansonService;
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
            List<Chanson> results = _rechercheChansonService.rechercheChansons(query);
            resultList.getChildren().clear();
            results.stream()
                    .map(_chansonViewBox::createChansonBox)
                    .forEach(resultList.getChildren()::add);
        });

        rootVBox.getChildren().addAll(ViewUtils.createSearchBox(searchField, searchButton), scrollPane);

        return rootVBox;
    }
}
