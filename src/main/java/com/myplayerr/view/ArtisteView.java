package com.myplayerr.view;

import com.myplayerr.database.ArtisteDAO;
import com.myplayerr.model.Artiste;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class ArtisteView {
    public VBox getView() {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Label title = new Label("Artistes");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        ArtisteDAO artisteDAO = new ArtisteDAO();
        List<Artiste> artistes = artisteDAO.getAllArtistes();

        for (Artiste artiste : artistes) {
            listView.getItems().add(artiste.getNom());
        }

        vbox.getChildren().addAll(title, listView);
        return vbox;
    }
}
