package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.badlogic.gdx.Gdx;
import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.utils.LevelLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FileController {

    private String path;

    private final LevelEditor editor;

    private final List<Consumer<String>> pathChangeListeners = new ArrayList<>();

    public FileController(LevelEditor editor) {
        this.editor = editor;
    }

    public void newFile() {
        path = null;
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void open(String path) {
        Level level = LevelLoader.load(Gdx.files.absolute(path));
        editor.setLevel(level);
        this.path = path;
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void save() {
        if (path == null) return;
        saveAs(path);
    }

    public void saveAs(String path) {
        LevelLoader.save(Gdx.files.absolute(path), editor.getLevel());
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
