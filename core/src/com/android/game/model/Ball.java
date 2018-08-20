package com.android.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Ball {

    private Vector2 position;
    private Color color;

    public Ball() {
    }

    public Ball(Vector2 position, Color color) {
        setPosition(position);
        setColor(color);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public void setPosition(Vector2 position) {
        this.position = position.cpy();
    }

    public void setColor(Color color) {
        this.color = color.cpy();
    }
}
