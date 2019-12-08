package com.github.nikitakuchur.puzzlegame.game.gameobjects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.game.Level;
import com.github.nikitakuchur.puzzlegame.game.GameMap;
import com.github.nikitakuchur.puzzlegame.utils.JsonUtils;

import java.util.Optional;

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
        GameMap map = level.getMap();
        setWidth(map.getWidth() / map.getCellsWidth());
        setHeight(map.getHeight() / map.getCellsHeight());
    }

    /**
     * Restores data from json
     */
    public void restore(JsonValue json) {
        Optional.ofNullable(JsonUtils.getString(json, "name")).ifPresent(this::setName);
        Optional.ofNullable(JsonUtils.getInt(json, "x")).ifPresent(this::setX);
        Optional.ofNullable(JsonUtils.getInt(json, "y")).ifPresent(this::setY);
        Optional.ofNullable(JsonUtils.getColor(json, "color")).ifPresent(this::setColor);
    }
}
