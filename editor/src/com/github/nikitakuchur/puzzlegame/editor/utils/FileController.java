package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.utils.LevelLoader;

import java.util.ArrayList;
import java.util.List;

public class FileController {

    private String path;
    private final LevelEditor editor;

    private List<Runnable> pathChangeListeners = new ArrayList<>();

    public FileController(LevelEditor editor) {
        this.editor = editor;
    }

    public String getPath() {
        return path;
    }

    public void newFile() {
        path = null;
        pathChangeListeners.forEach(Runnable::run);
    }

    public void open(String path) {
        Level level = LevelLoader.load(Gdx.files.absolute(path));
        editor.setLevel(level);
        this.path = path;
        pathChangeListeners.forEach(Runnable::run);
    }

    public void save() {
        if (path == null) return;
        saveAs(path);
    }

    public void saveAs(String path) {
        Level level = editor.getLevel();

        Json json = new Json(JsonWriter.OutputType.json);
        String text = json.prettyPrint(json.toJson(level));

        FileHandle file = Gdx.files.absolute(path);
        file.writeString(text, false);
        this.path = path;
        pathChangeListeners.forEach(Runnable::run);
    }

    public void addPathChangeListener(Runnable runnable) {
        pathChangeListeners.add(runnable);
    }
}
