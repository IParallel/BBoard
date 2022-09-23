package com.bello.bboard;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

public class Controller {

    @FXML
    protected ListView<String> hotkeyContainer = new ListView<>();
    @FXML
    protected ChoiceBox<String> inputList = new ChoiceBox<>();

    @FXML
    protected ChoiceBox<String> outputList = new ChoiceBox<>();


    @FXML
    protected void deleteHotkey() {
        int selectedIndex = hotkeyContainer.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) return;

        hotkeyContainer.getItems().remove(selectedIndex);

    }



}
