package com.majakkagames.gravitymaze.core.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

import java.util.Random;

public abstract class GameObject extends Group {

    protected Level level;

    protected GameObject() {
        // TODO: this is strange
        Random rand = new Random();
        setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1));
    }

    public void initialize(Level level) {
        this.level = level;
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public void setPosition(Vector2 position){
        setPosition(position.x, position.y);
    }

    public int getLayer() {
        return 0;
    }

    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        parameters.put("name", String.class, getName());
        parameters.put("x", Integer.class, (int) getX());
        parameters.put("y", Integer.class, (int) getY());
        parameters.put("color", Color.class, getColor().cpy());
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        setName(parameters.getValue("name"));
        setX(parameters.<Integer>getValueOrDefault("x", 0));
        setY(parameters.<Integer>getValueOrDefault("y", 0));
        setColor(parameters.getValueOrDefault("color", Color.WHITE.cpy()));
    }
}

