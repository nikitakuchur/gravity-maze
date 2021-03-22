package com.triateq.gravitymaze.editor.utils;

import com.badlogic.gdx.Gdx;
import com.triateq.gravitymaze.editor.LevelEditor;
import com.triateq.gravitymaze.level.Level;
import com.triateq.gravitymaze.level.LevelLoader;
import com.triateq.puzzlecore.game.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FileController {

    private String path;

    private final LevelLoader levelLoader;
    private final LevelEditor editor;

    private final List<Consumer<String>> pathChangeListeners = new ArrayList<>();

    public FileController(Context context, LevelEditor editor) {
        levelLoader = new LevelLoader(context);
        this.editor = editor;
    }

    public void newFile() {
        path = null;
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void open(String path) throws IOException {
        Level level = levelLoader.load(Gdx.files.absolute(path));
        editor.setLevel(level);
        this.path = path;
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void save() {
        if (path == null) return;
        saveAs(path);
    }

    public void saveAs(String path) {
        levelLoader.save(Gdx.files.absolute(path), editor.getLevel());
        this.path = path;
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void addPathChangeListener(Consumer<String> consumer) {
        pathChangeListeners.add(consumer);
    }

    public void clearPathChangeListeners() {
        pathChangeListeners.clear();
    }
}
