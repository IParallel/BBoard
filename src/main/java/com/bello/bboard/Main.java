package com.bello.bboard;

import com.bello.bboard.Utils.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static ConfigManager config = new ConfigManager();


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        config.loadConfig();
        Parent load = fxmlLoader.load();
        GlobalControls.setControllers(fxmlLoader);
        Scene scene = new Scene(load);
        labelsChange();
        stage.getIcons().add(new Image("https://t3.ftcdn.net/jpg/03/25/51/78/360_F_325517814_G7uy7THr1Y2SJM5MbLWQmmkz9frN7NcD.jpg"));
        stage.setTitle("BBoard");
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
