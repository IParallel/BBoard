package com.bello.bboard.Utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BBConfig {
    private String outputAdapter = "";
    private String inputAdapter = "";
    private List<String> hotkeys = new ArrayList<>();

    public void addHotkey(String hotkey) {
        this.hotkeys.add(hotkey);
    }

    public void delHotkey(String hotkey) {
        this.hotkeys.remove(hotkey);
    }

    public List<String> getHotkeys() {
        return hotkeys;
    }

    public void setHotkeys(List<String> hotkeys) {
        this.hotkeys = hotkeys;
    }

    public String getOutputAdapter() {
        return outputAdapter;
    }

    public void setOutputAdapter(String outputAdapter) {
        this.outputAdapter = outputAdapter;
    }

    public String getInputAdapter() {
        return inputAdapter;
    }

    public void setInputAdapter(String inputAdapter) {
        this.inputAdapter = inputAdapter;
    }
}
