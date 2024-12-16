module com.myplayerr {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.myplayerr to javafx.fxml;
    exports com.myplayerr;
    exports com.myplayerr.controller;
    opens com.myplayerr.controller to javafx.fxml;
}
