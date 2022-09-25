package com.bello.bboard;

import com.bello.bboard.AudioManager.AudioStream;
import com.bello.bboard.Utils.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Bboard extends Application {

    public static ConfigManager config = new ConfigManager();

    public static AudioStream audioStream = new AudioStream();

    public static KeyboardListener keyboard = new KeyboardListener();

    @Override
    public void start(Stage stage) throws IOException, LineUnavailableException {
        Platform.setImplicitExit(true);
        stage.setOnCloseRequest((ae) -> {
            Platform.exit();
            System.exit(0);
        });
        //audioStream.debugg();
        AudioHandler.filterDevices();

        FXMLLoader fxmlLoader = new FXMLLoader(Bboard.class.getResource("hello-view.fxml"));
        config.loadConfig();
        Parent load = fxmlLoader.load();
        GlobalControls.setControllers(fxmlLoader);
        Scene scene = new Scene(load, 600, 400);
        labelsChange();
        setEvents();
        stage.getIcons().add(new Image(Objects.requireNonNull(Bboard.class.getResourceAsStream("icon.jpg"))));
        stage.setTitle("BBoard");
        stage.setScene(scene);
        stage.show();
    }

    public void labelsChange() {
        BBConfig cfg = config.getConfig();
        Controller controller = GlobalControls.getControllers();
        controller.inputList.getSelectionModel().select(Objects.equals(cfg.getInputAdapter(), "") ? "Select Input Driver" : cfg.getInputAdapter());
        controller.outputList.getSelectionModel().select(Objects.equals(cfg.getOutputAdapter(), "") ? "Select Output Driver" : cfg.getOutputAdapter());
        controller.inputList.getItems().addAll(AudioHandler.getInputMixers().keySet().stream().toList());
        controller.inputList.getItems().add("None");
        controller.outputList.getItems().addAll(AudioHandler.getOutPutMixers().keySet().stream().toList());
        controller.outputList.getItems().add("None");
        updateList();
    }


    public static void updateList() {
        Controller controller = GlobalControls.getControllers();
        controller.hotkeyContainer.getItems().clear();
        BBConfig cfg = config.getConfig();
        cfg.getKeys().clear();
        List<String> keys = cfg.getHotkeys().stream().map(item -> {
            String[] hotkey = item.split(";");
            String key = hotkey[0];
            String file = hotkey[1];
            cfg.getKeys().put(key.toUpperCase(), file);
            return "Hotkey: " + key + "    File: " + file;
        }).toList();
        controller.hotkeyContainer.getItems().addAll(keys);
    }

    public void setEvents() {
        Controller controller = GlobalControls.getControllers();
        controller.inputList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            config.getConfig().setInputAdapter(newValue);
            try {
                config.saveConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        controller.outputList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            config.getConfig().setOutputAdapter(newValue);
            try {
                config.saveConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void main(String[] args) {
        launch();
    }
}
