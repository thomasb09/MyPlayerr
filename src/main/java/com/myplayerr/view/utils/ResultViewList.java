package com.myplayerr.view.utils;

import com.myplayerr.service.AjoutChansonService;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ResultViewList {

    private AjoutChansonService _ajoutChansonService;
    private VBox resultList;
    private ScrollPane scrollPane;

    public ResultViewList(AjoutChansonService ajoutChansonService) {
        _ajoutChansonService = ajoutChansonService;
        resultList = ViewUtils.createResultList();
        scrollPane = ViewUtils.createScrollPane(resultList);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void performSearch(String query) {
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
                results.forEach(result -> resultList.getChildren().add(new ResultViewBox(result, _ajoutChansonService).getNode()));
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
    }
}
