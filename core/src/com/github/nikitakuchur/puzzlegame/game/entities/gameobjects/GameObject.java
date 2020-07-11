package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.Entity;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.utils.Layer;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public abstract class GameObject extends Actor implements Entity {

    protected Level level;

    public GameObject() {
        Random rand = new Random();
        setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1));
    }

    public void initialize(Level level) {
        this.level = level;
    }

    /**
     * Updates width and height
     */
    public void update() {
        GameMap map = level.getMap();
        setWidth(map.getWidth() / map.getCellsWidth());
        setHeight(map.getHeight() / map.getCellsHeight());
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
    }

    public Layer getLayer() {
        return Layer.MIDDLE;
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public void setPosition(Vector2 position){
        setPosition(position.x, position.y);
    }

    /**
     * @return the actual position of the gameObject
     */
    public Vector2 getActualPosition() {
        return new Vector2(getX() * getWidth() - getParent().getWidth() / 2,
                getY() * getHeight() - getParent().getHeight() / 2);
    }

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("name", String.class, getName());
        properties.put("x", Integer.class, (int) getX());
        properties.put("y", Integer.class, (int) getY());
        properties.put("color", Color.class, getColor());
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        setName(properties.getValue("name"));
        setX(properties.<Integer>getValue("x"));
        setY(properties.<Integer>getValue("y"));
        setColor(properties.getValue("color"));
    }
}
