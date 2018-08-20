package com.android.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Ball {

    private Vector2 position;
    private Color color;

    public Ball() {
        position = new Vector2();
        color = new Color();
    }

    public Ball(Vector2 position, Color color) {
        setPosition(position);
        setColor(color);
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Color getColor() {
        return color.cpy();
    }

    public void setPosition(Vector2 position) {
        this.position = position.cpy();
    }

    public void setColor(Color color) {
        this.color = color.cpy();
    }
}
