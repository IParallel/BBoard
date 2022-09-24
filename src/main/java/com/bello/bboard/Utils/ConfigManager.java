package com.bello.bboard.Utils;

import com.bello.bboard.Adapter.ConfigAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private final File file;

    private BBConfig config = new BBConfig();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(BBConfig.class, new ConfigAdapter())
            .setPrettyPrinting()
            .create();

    public ConfigManager() {
        file = new File("./config/cfg.json");
        try {
            Files.createDirectory(Path.of("./config/"));
            if (!file.exists()) {
                file.createNewFile();
                saveConfig();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() {
        try {
            // create a reader
            Reader reader = Files.newBufferedReader(file.toPath());

            // convert JSON string to User object
            config = gson.fromJson(reader, BBConfig.class);
            // close reader
            reader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() throws IOException {
        try (Writer writer = new FileWriter(file.getAbsolutePath())) {
            gson.toJson(config, writer);
        }

    }


    public BBConfig getConfig() {
        return config;
    }

}
