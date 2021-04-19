package com.triateq.gravitymaze.game.gameobjects.mazeobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.game.gameobjects.Maze;

public abstract class MazeObject extends GameObject {

    protected AssetManager assetManager;
    protected Maze maze;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        assetManager = level.getContext().getAssetManager();
        maze = level.getGameObjectStore().getAnyGameObjectOrThrow(Maze.class,
                () -> new IllegalStateException("Cannot find the maze object"));
        setWidth(maze.getCellSize());
        setHeight(maze.getCellSize());
        setOrigin(getWidth() / 2, getHeight() / 2);
    }
}
