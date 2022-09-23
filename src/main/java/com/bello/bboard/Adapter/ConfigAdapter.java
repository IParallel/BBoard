package com.bello.bboard.Adapter;

import com.bello.bboard.Utils.BBConfig;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ConfigAdapter extends TypeAdapter<BBConfig> {

    @Override
    public void write(JsonWriter writer, BBConfig value) throws IOException {
        //write the keys with their respective value
        writer.beginObject();
        writer.name("inputAdapter");
        writer.value(value.getInputAdapter());
        writer.name("outputAdapater");
        writer.value(value.getOutputAdapter());
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
            }
        }
        reader.endObject();
        return bbConfig;
    }
}
