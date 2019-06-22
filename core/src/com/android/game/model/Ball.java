package com.android.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Ball {

    private Vector2 position;
    private Color color;

    /**
     * Creates a new ball
     */
    public Ball() {
        position = new Vector2();
        color = new Color();
    }

    /**
     * Creates a new ball
     *
     * @param position the position of the ball
     * @param color the color of the ball
     */
    public Ball(Vector2 position, Color color) {
        setPosition(position);
        setColor(color);
    }

    /**
     * Sets the position of the ball
     *
     * @param position the position
     */
    public void setPosition(Vector2 position) {
        this.position = position.cpy();
    }

    /**
     * @return the position of the ball
     */
    public Vector2 getPosition() {
        return position.cpy();
    }

    /**
     * Sets the color of the ball
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color.cpy();
    }

    /**
     * @return the color of the ball
     */
    public Color getColor() {
        return color.cpy();
    }
}
