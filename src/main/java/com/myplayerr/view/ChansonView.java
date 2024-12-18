package com.myplayerr.view;

import com.myplayerr.database.ChansonDAO;
import com.myplayerr.model.Chanson;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChansonView {

    private ChansonViewBox _chansonViewBox;

    public void setDependance(ChansonViewBox chansonViewBox) {
        _chansonViewBox = chansonViewBox;
    }

    public VBox getView() {
        VBox rootVBox = new VBox();
        rootVBox.setSpacing(10);
        rootVBox.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Toutes les chansons");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        ScrollPane scrollPane = new ScrollPane();
        VBox songList = new VBox();
        songList.setSpacing(15);
        songList.setAlignment(Pos.TOP_LEFT);

        List<Chanson> chansons = new ChansonDAO().getAllChansons();

        for (Chanson chanson : chansons) {
            songList.getChildren().add(_chansonViewBox.createChansonBox(chanson));
        }

        scrollPane.setContent(songList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        rootVBox.getChildren().addAll(title, scrollPane);
        rootVBox.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");
        return rootVBox;
    }
}
