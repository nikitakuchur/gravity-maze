package com.puzzlegame.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject extends Actor implements Disposable {

    /**
     * Updates the game object based on time
     *
     * @param level the current level
     * @param delta time in seconds since the last frame.
     */
    public void act(Level level, float delta) {
        update(level);
        super.act(delta);
    }

    /**
     * Updates width and height
     */
    private void update(Level level) {
        Map map = level.getMap();
        setWidth(map.getWidth() / map.getCellsWidth());
        setHeight(map.getHeight() / map.getCellsHeight());
    }
}
