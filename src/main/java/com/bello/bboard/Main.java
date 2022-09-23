package com.bello.bboard;

import com.bello.bboard.Utils.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static ConfigManager config = new ConfigManager();


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        config.loadConfig();
        config.getConfig().setVersion("1");
        config.saveConfig();
        Parent load = fxmlLoader.load();
        GlobalControls.setControllers(fxmlLoader);
        Scene scene = new Scene(load);
        labelsChange();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void labelsChange() {
        Controller controller = GlobalControls.getControllers();
        controller.inputList.getSelectionModel().select("Select Input Driver");
        controller.outputList.getSelectionModel().select("Select Output Driver");
        DevicesList devicesList = AudioHandler.getAdapters();
        controller.inputList.getItems().addAll(devicesList.inputList());
        controller.outputList.getItems().addAll(devicesList.outputList());

        controller.hotkeyContainer.getItems().addAll(devicesList.inputList());
    }

    public static void main(String[] args) {
        launch();
    }
}
