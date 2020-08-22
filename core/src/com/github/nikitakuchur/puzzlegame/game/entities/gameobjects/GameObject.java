package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.game.entities.Parameterizable;
import com.github.nikitakuchur.puzzlegame.utils.Layer;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

public abstract class GameObject extends Actor implements Parameterizable, Disposable {

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
    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        parameters.put("name", String.class, getName());
        parameters.put("x", Integer.class, (int) getX());
        parameters.put("y", Integer.class, (int) getY());
        parameters.put("color", Color.class, getColor().cpy());
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        setName(parameters.getValue("name"));
        setX(validateX(parameters.<Integer>getValue("x")));
        setY(validateY(parameters.<Integer>getValue("y")));
        setColor(parameters.getValue("color"));
    }

    private int validateX(int x) {
        if (level != null && level.getMap().isOutside(x, (int) getY())) {
            return (int) getX();
        }
        return x;
    }

    private int validateY(int y) {
        if (level != null && level.getMap().isOutside((int) getX(), y)) {
            return (int) getY();
        }
        return y;
    }
}
