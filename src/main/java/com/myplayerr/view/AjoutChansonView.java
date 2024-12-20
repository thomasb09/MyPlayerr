package com.myplayerr.view;

import com.myplayerr.service.AjoutChansonService;
import com.myplayerr.view.utils.RechercheViewBox;
import com.myplayerr.view.utils.ResultViewList;
import com.myplayerr.view.utils.ViewUtils;
import javafx.scene.layout.VBox;

public class AjoutChansonView {

    private AjoutChansonService _ajoutChansonService;

    public void setDependance(AjoutChansonService ajoutChansonService) {
        _ajoutChansonService = ajoutChansonService;
    }

    public VBox getView() {
        VBox rootVBox = ViewUtils.createRootVBox("Ajouter une Chanson");

        RechercheViewBox searchBox = new RechercheViewBox();
        ResultViewList resultListView = new ResultViewList(_ajoutChansonService);

        searchBox.setOnSearch(resultListView::performSearch);

        rootVBox.getChildren().addAll(searchBox.getNode(), resultListView.getScrollPane());

        return rootVBox;
    }
}
