package com.myplayerr.view;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AlbumView {

    public StackPane getView() {
        StackPane pane = new StackPane();
        pane.getChildren().add(new Label("Album View"));
        return pane;
    }
}
