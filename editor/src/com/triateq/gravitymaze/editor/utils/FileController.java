package com.triateq.gravitymaze.editor.utils;

import com.badlogic.gdx.Gdx;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.editor.LevelEditor;
import com.triateq.gravitymaze.core.serialization.LevelLoader;
import com.triateq.gravitymaze.core.game.Context;
import com.triateq.gravitymaze.game.gameobjects.Maze;
import com.triateq.gravitymaze.game.utils.LevelBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FileController {

    private String path;

    private final Context context;
    private final LevelLoader levelLoader;
    private final LevelEditor editor;

    private final List<Consumer<String>> pathChangeListeners = new ArrayList<>();

    public FileController(Context context, LevelEditor editor) {
        this.context = context;
        this.levelLoader = new LevelLoader(context);
        this.editor = editor;
    }

    public void newFile(int width, int height) {
        path = null;
        Level level = new LevelBuilder().maze(new Maze(width, height))
                .context(context)
                .build();
        editor.setLevel(level);
        pathChangeListeners.forEach(listener -> listener.accept(path));
    }

    public void open(String path) throws IOException {
        Level level = LevelBuilder.from(levelLoader.load(Gdx.files.absolute(path))).build();
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
