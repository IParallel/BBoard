package com.bello.bboard.Utils;

public class BBConfig {
    private String version = "";

    @Override
    public String toString() {
        return "BBConfig{" +
                "version='" + version + '\'' +
                '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
