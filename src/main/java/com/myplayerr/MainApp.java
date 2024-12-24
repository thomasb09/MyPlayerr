package com.myplayerr;

import com.myplayerr.database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.initializeDatabase();

//        Image appIcon = new Image(getClass().getResource("./images/albums/defaultAlbum.png").toExternalForm());
//        primaryStage.getIcons().add(appIcon);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/mainView.fxml"));

        AppContext context = AppContext.getInstance();

        loader.setController(context.getMainController());

        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("MyPlayerr");
        primaryStage.setMaximized(true);
        primaryStage.show();

        context.setDependance();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
