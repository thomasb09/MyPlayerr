package com.myplayerr.view;

import com.myplayerr.service.AjoutChansonService;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DownloadView {

    private final VBox downloadBox = new VBox();

    public void setDependance(AjoutChansonService ajoutChansonService) {
        downloadBox.setSpacing(10);
        downloadBox.setStyle("-fx-padding: 10; -fx-background-color: #2c3e50; -fx-text-fill: white;");

        ajoutChansonService.getDownloads().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (String songName : change.getAddedSubList()) {
                        addDownload(songName);
                    }
                } else if (change.wasRemoved()) {
                    for (String songName : change.getRemoved()) {
                        removeDownload(songName);
                    }
                }
            }
        });
    }

    private void addDownload(String songName) {
        Label songLabel = new Label(songName);
        songLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10px;");
        songLabel.setMaxWidth(110);
        songLabel.setEllipsisString("...");
        songLabel.setWrapText(false);

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(15, 15);

        HBox downloadItem = new HBox(loadingIndicator, songLabel);
        downloadItem.setSpacing(5);
        downloadItem.setStyle("-fx-alignment: center-left;");
        downloadItem.setMaxWidth(200);

        downloadBox.getChildren().add(downloadItem);
    }

    private void removeDownload(String songName) {
        downloadBox.getChildren().removeIf(node -> {
            if (node instanceof HBox) {
                HBox item = (HBox) node;
                Label label = (Label) item.getChildren().get(1);
                return label.getText().equals(songName);
            }
            return false;
        });
    }

    public VBox getView() {
        return downloadBox;
    }
}
