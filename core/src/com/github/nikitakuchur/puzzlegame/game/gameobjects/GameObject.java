package com.github.nikitakuchur.puzzlegame.game.gameobjects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.game.Level;
import com.github.nikitakuchur.puzzlegame.game.GameMap;
import com.github.nikitakuchur.puzzlegame.utils.JsonUtils;

import java.util.Optional;

public abstract class GameObject extends Actor implements Json.Serializable, Disposable {

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

    @Override
    public void write(Json json) {
        json.writeValue("name", getName());
        json.writeValue("x", (int) getX());
        json.writeValue("y", (int) getY());
        json.writeValue("color", getColor().toString());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        Optional.ofNullable(JsonUtils.getString(jsonData, "name")).ifPresent(this::setName);
        Optional.ofNullable(JsonUtils.getInt(jsonData, "x")).ifPresent(this::setX);
        Optional.ofNullable(JsonUtils.getInt(jsonData, "y")).ifPresent(this::setY);
        Optional.ofNullable(JsonUtils.getColor(jsonData, "color")).ifPresent(this::setColor);
    }
}
