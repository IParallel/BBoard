module com.bello.bboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;


    opens com.bello.bboard to javafx.fxml;
    exports com.bello.bboard;
}
