package com.triateq.gravitymaze.level;

import com.badlogic.gdx.files.FileHandle;
import com.triateq.gravitymaze.serialization.Parameterizable;
import com.triateq.gravitymaze.serialization.Serializer;
import com.triateq.gravitymaze.utils.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

public class LevelLoader {

    private final Gson gson;

    public LevelLoader(Context context) {
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Parameterizable.class, new Serializer(context))
                .setPrettyPrinting()
                .create();
    }

    public Level load(FileHandle handle) throws IOException {
        try {
            return gson.fromJson(handle.readString(), Level.class);
        } catch (JsonSyntaxException e) {
            throw new IOException("Cannot load this file.", e);
        }
    }

    public void save(FileHandle handle, Level level) {
        String json = gson.toJson(level);
        handle.writeString(json, false);
    }

    public String toJson(Level level) {
        return gson.toJson(level);
    }

    public Level fromJson(String json) {
        return gson.fromJson(json, Level.class);
    }
}
