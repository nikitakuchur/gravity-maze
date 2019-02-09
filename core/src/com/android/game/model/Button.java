package com.android.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Button {
    private Vector2 position;
    private Vector2 size;
    private String text;

    private Color color;

    public Button() {
        position = new Vector2(0, 0);
        size = new Vector2(100, 100);
        text = "Button";
        color = Color.WHITE;
    }

    public Button(Vector2 position, Vector2 size, String text) {
        this.position = position.cpy();
        this.size = size.cpy();
        this.text = text;
        color = Color.WHITE;
    }

    /**
     * Sets the position of the button
     *
     * @param position the position
     */
    public void setPosition(Vector2 position) {
        this.position = position.cpy();
    }

    /**
     * @return the position of the button
     */
    public Vector2 getPosition() {
        return position.cpy();
    }

    /**
     * Sets the size of the button
     *
     * @param size the size
     */
    public void setSize(Vector2 size) {
        this.size = size.cpy();
    }

    /**
     * @return the size of the button
     */
    public Vector2 getSize() {
        return size.cpy();
    }

    /**
     * Sets the button text
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the button text
     */
    public String getText() {
        return text;
    }

    public void setColor(Color color) {
        this.color = color.cpy();
    }

    public Color getColor() {
        return color.cpy();
    }
}
