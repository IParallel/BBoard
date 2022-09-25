package com.bello.bboard;

import com.bello.bboard.AudioManager.AudioStream;
import com.bello.bboard.Utils.GlobalControls;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Controller {

    @FXML
    protected ListView<String> hotkeyContainer = new ListView<>();
    @FXML
    protected ChoiceBox<String> inputList = new ChoiceBox<>();

    @FXML
    protected ChoiceBox<String> outputList = new ChoiceBox<>();

    @FXML
    protected Slider volumeControl = new Slider();

    @FXML
    protected Button startStopButton;


    @FXML
    protected void deleteHotkey() throws IOException {
        int selectedIndex = hotkeyContainer.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) return;

        hotkeyContainer.getItems().remove(selectedIndex);
        Bboard.config.getConfig().getHotkeys().remove(selectedIndex);
        Bboard.config.saveConfig();
    }


    @FXML
    protected void addHotkey() throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(Bboard.class.getResource("addHotkey.fxml"));
        Parent load = fxmlLoader.load();
        addHotkeyControllers controller = fxmlLoader.getController();
        Scene scene = new Scene(load);
        stage.getIcons().add(new Image(Objects.requireNonNull(Bboard.class.getResourceAsStream("icon.jpg"))));
        stage.setResizable(false);
        scene.setOnDragOver(event -> {
            if (event.getGestureSource() != scene
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        scene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                controller.filePath.setText(db.getFiles().get(0).getAbsolutePath());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
        stage.setTitle("Add a hotkey and a file");
        stage.setScene(scene);
        stage.show();
    }

    public void startStopMixer() {
        AudioStream audioStream = Bboard.audioStream;
        if (audioStream.isRunning() || !audioStream.getClipAudioPlayer().isStopped()) {
            startStopButton.setText("Start");
            audioStream.setStop(true);
            audioStream.stopClip();
            audioStream.setRunning(false);
            audioStream.getClipAudioPlayer().setStopped(true);
        }else {
            startStopButton.setText("Stop");
            audioStream.start();
            audioStream.getClipAudioPlayer().setStopped(false);
        }
    }

    public void volumeChange() throws IOException {
        Bboard.config.getConfig().setVolume((int) volumeControl.getValue());
        Bboard.config.saveConfig();
    }

    public void editHotkey() throws IOException {
        Controller mainCtrls = GlobalControls.getControllers();
        int selectedIndex = mainCtrls.hotkeyContainer.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) return;
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(Objects.requireNonNull(Bboard.class.getResourceAsStream("icon.jpg"))));
        FXMLLoader fxmlLoader = new FXMLLoader(Bboard.class.getResource("editHotkey.fxml"));
        Parent load = fxmlLoader.load();
        addHotkeyControllers controller = fxmlLoader.getController();
        String[] split = Bboard.config.getConfig().getHotkeys().get(selectedIndex).split(";");
        controller.filePath.setText(split[1]);
        controller.hotkeyField.setText(split[0]);
        Scene scene = new Scene(load);
        scene.setOnDragOver(event -> {
            if (event.getGestureSource() != scene
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        scene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                controller.filePath.setText(db.getFiles().get(0).getAbsolutePath());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
        stage.setTitle("Edit this hotkey");
        stage.setScene(scene);
        stage.show();
    }



}
