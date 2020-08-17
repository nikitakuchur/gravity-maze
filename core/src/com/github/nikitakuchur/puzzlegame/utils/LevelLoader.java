package com.github.nikitakuchur.puzzlegame.utils;

import com.badlogic.gdx.files.FileHandle;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.Parameterizable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LevelLoader {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(Parameterizable.class, new Parameterizable.Serializer())
            .setPrettyPrinting()
            .create();

    private LevelLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static Level load(FileHandle handle) {
        return GSON.fromJson(handle.readString(), Level.class);
    }

    public static void save(FileHandle handle, Level level) {
        String json = GSON.toJson(level);
        handle.writeString(json, false);
    }

    public static String toJson(Level level) {
        return GSON.toJson(level);
    }

    public static Level fromJson(String json) {
        return GSON.fromJson(json, Level.class);
    }
}
