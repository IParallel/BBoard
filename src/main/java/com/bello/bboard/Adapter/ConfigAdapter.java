package com.bello.bboard.Adapter;

import com.bello.bboard.Utils.BBConfig;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

public class ConfigAdapter extends TypeAdapter<BBConfig> {

    @Override
    public void write(JsonWriter writer, BBConfig value) throws IOException {
        //write the keys with their respective value
        writer.beginObject();
        writer.name("inputAdapter");
        writer.value(value.getInputAdapter());
        writer.name("outputAdapter");
        writer.value(value.getOutputAdapter());
        writer.name("hotkeys");
        writer.value(new Gson().toJson(value.getHotkeys()));
        writer.name("volume");
        writer.value(value.getVolume());
        writer.endObject();
    }

    @Override
    public BBConfig read(JsonReader reader) throws IOException {
        BBConfig bbConfig = new BBConfig();
        reader.beginObject();
        String fieldname = null;

        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                //get the current token
                fieldname = reader.nextName();
            }

            switch (fieldname) {
                case "outputAdapter" -> bbConfig.setOutputAdapter(reader.nextString());
                case "inputAdapter" -> bbConfig.setInputAdapter(reader.nextString());
                case "hotkeys" -> bbConfig.setHotkeys(new Gson().fromJson(reader.nextString(), List.class));
                case "volume" -> bbConfig.setVolume(reader.nextInt());
            }
        }
        reader.endObject();
        return bbConfig;
    }
}
