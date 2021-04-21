package com.triateq.gravitymaze.game.utils;

import com.badlogic.gdx.Gdx;
import com.triateq.gravitymaze.core.game.Context;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.core.serialization.LevelLoader;
import com.triateq.gravitymaze.game.gameobjects.*;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.MazeObject;
import com.triateq.gravitymaze.game.physics.Physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelBuilder {

    private final Level level;

    private Context context;
    private LevelLoader loader;

    private Background background = new Background();
    private Maze maze = new Maze();
    private final List<MazeObject> mazeObjects = new ArrayList<>();

    public LevelBuilder() {
        this.level = null;
    }

    private LevelBuilder(Level level) {
        this.level = level;
        this.context(level.getContext());
    }

    public LevelBuilder context(Context context) {
        this.context = context;
        if (level != null) {
            loader = new LevelLoader(context);
        }
        return this;
    }

    public LevelBuilder background(Background background) {
        this.background = background;
        return this;
    }

    public LevelBuilder maze(Maze maze) {
        this.maze = maze;
        return this;
    }

    public LevelBuilder add(MazeObject mazeObject) {
        mazeObjects.add(mazeObject);
        return this;
    }

    public static LevelBuilder from(Level level) {
        return new LevelBuilder(level);
    }

    public Level build() {
        Objects.requireNonNull(context);

        Level newLevel;
        if (level == null) {
            newLevel = new Level(context);
            newLevel.getGameObjectStore().add(background);
            newLevel.getGameObjectStore().add(maze);
        } else {
            String json = loader.toJson(level);
            newLevel = loader.fromJson(json);
        }

        newLevel.setWidth(Gdx.graphics.getWidth());
        newLevel.setHeight(Gdx.graphics.getHeight());
        for (MazeObject mazeObject : mazeObjects) {
            newLevel.getGameObjectStore().add(mazeObject);
        }
        newLevel.getGameObjectStore().add(new Gravity());
        newLevel.getGameObjectStore().add(new LevelProperties());
        newLevel.getGameObjectStore().add(new LevelController());
        newLevel.getGameObjectStore().add(new Physics(newLevel));
        newLevel.initialize();
        return newLevel;
    }
}
