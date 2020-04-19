package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.Entity;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public abstract class GameObject extends Actor implements Entity {

    /**
     * Updates the game object based on time
     *
     * @param level the current level
     * @param delta time in seconds since the last frame.
     */
    public void act(Level level, float delta) {
        super.act(delta);
    }

    /**
     * Updates width and height
     */
    public void update(Level level) {
        GameMap map = level.getMap();
        setWidth(map.getWidth() / map.getCellsWidth());
        setHeight(map.getHeight() / map.getCellsHeight());
    }

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("name", String.class, getName());
        properties.put("x", int.class, (int) getX());
        properties.put("y", int.class, (int) getY());
        properties.put("color", Color.class, getColor());
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        setName((String) properties.getValue("name"));
        setX((int) properties.getValue("x"));
        setY((int) properties.getValue("y"));
        setColor((Color) properties.getValue("color"));
    }
}