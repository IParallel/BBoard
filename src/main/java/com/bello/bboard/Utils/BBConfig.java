package com.bello.bboard.Utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BBConfig {
    private String outputAdapter = "";
    private String inputAdapter = "";
    private int volume = 50;
    private List<String> hotkeys = new ArrayList<>();

    private HashMap<String, String> keys = new HashMap<>();

    public HashMap<String, String> getKeys() {
        return keys;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setKeys(HashMap<String, String> keys) {
        this.keys = keys;
    }

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
