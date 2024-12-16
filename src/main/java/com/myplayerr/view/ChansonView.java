package com.myplayerr.view;

import com.myplayerr.database.ChansonDAO;
import com.myplayerr.model.Chanson;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChansonView {
    public VBox getView() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label title = new Label("Chansons");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        ChansonDAO chansonDAO = new ChansonDAO();
        List<Chanson> chansons = chansonDAO.getAllChansons();

        for (Chanson chanson : chansons) {
            listView.getItems().add(chanson.getTitre() + " - " + chanson.getArtiste().getNom() + " (" + chanson.getAlbum().getNom() + ")");
        }

        vbox.getChildren().addAll(title, listView);
        return vbox;
    }
}
