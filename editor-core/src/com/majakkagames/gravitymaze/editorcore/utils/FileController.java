package com.majakkagames.gravitymaze.editorcore.utils;

import com.badlogic.gdx.Gdx;
import com.majakkagames.gravitymaze.core.game.Context;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.core.serialization.LevelLoader;
import com.majakkagames.gravitymaze.editorcore.LevelEditor;
import com.majakkagames.gravitymaze.editorcore.config.Configurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FileController {

    private String path;

    private final Configurator configurator;
    private final LevelLoader levelLoader;
    private final LevelEditor editor;

    private final List<Consumer<String>> pathChangeListeners = new ArrayList<>();

    public FileController(Configurator configurator, Context context, LevelEditor editor) {
        this.configurator = configurator;
        this.levelLoader = new LevelLoader(context);
        this.editor = editor;
    }

    public void newFile(Level level) {
        editor.getLevelManager().setLevel(level);
        level.initialize();
        resetPath();
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void open(String path) throws IOException {
        Level level = levelLoader.load(Gdx.files.absolute(path));
        configurator.getLevelPreparer().accept(level);
        editor.getLevelManager().setLevel(level);
        level.initialize();
        this.path = path;
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void save() {
        if (path == null) return;
        saveAs(path);
    }

    public void saveAs(String path) {
        levelLoader.save(Gdx.files.absolute(path), editor.getLevelManager().getLevel());
        this.path = path;
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public String getPath() {
        return path;
    }

    public void resetPath() {
        path = null;
    }

    public void addPathChangeListener(Consumer<String> consumer) {
        pathChangeListeners.add(consumer);
    }
}
