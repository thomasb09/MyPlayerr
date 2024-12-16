module com.myplayerr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mp3agic;
    requires okhttp3;
    requires org.json;


    opens com.myplayerr to javafx.fxml;
    exports com.myplayerr;
    exports com.myplayerr.controller;
    opens com.myplayerr.controller to javafx.fxml;
}
