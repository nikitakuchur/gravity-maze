package com.triateq.gravitymaze.core.serialization;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.core.game.Context;
import com.triateq.gravitymaze.core.serialization.annotations.Serializer;

import java.io.IOException;

public class LevelLoader {

    private final Gson gson;

    public LevelLoader(Context context) {
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(GameObject.class, new Serializer(context))
                .registerTypeHierarchyAdapter(GameObject[].class, new JsonArraySerializer<GameObject>())
                .setPrettyPrinting()
                .create();
    }

    public Level load(FileHandle handle) throws IOException {
        try {
            return gson.fromJson(handle.readString(), Level.class);
        } catch (JsonSyntaxException e) {
            throw new IOException("Cannot load " + handle.path(), e);
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
