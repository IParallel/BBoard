package com.bello.bboard.Utils;

import com.bello.bboard.Controller;
import javafx.fxml.FXMLLoader;

public class GlobalControls {

    private static Controller controllers = null;

    public static Controller getControllers() {
        return controllers;
    }

    public static void setControllers(FXMLLoader loader) {
        controllers = loader.getController();
    }
}
