package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.game.Level;
import com.github.nikitakuchur.puzzlegame.utils.LevelLoader;

public class LevelUtils {

    private LevelUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void load(EditorApplication app) {
        Level level = LevelLoader.load(Gdx.files.internal("levels/sample.json"));
        app.getLevelEditor().setLevel(level);
    }

    public static void save(EditorApplication app) {
        Level level = app.getLevelEditor().getLevel();

        Json json = new Json(JsonWriter.OutputType.json);
        String text = json.prettyPrint(json.toJson(level));

        FileHandle file = Gdx.files.local("levels/sample.json");
        file.writeString(text, false);
    }
}
