package com.github.nikitakuchur.puzzlegame.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.game.Level;

public class LevelLoader {

    private LevelLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static Level load(FileHandle handle) {
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonValue = jsonReader.parse(handle);
        Json json = new Json();
        return json.readValue(Level.class, jsonValue);
    }
}
