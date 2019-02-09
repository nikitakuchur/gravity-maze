package com.android.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Button {
    private Vector2 position;
    private Vector2 size;
    private String text;

    private Color normalColor;
    private Color pressedColor;

    private boolean pressed;

    public Button() {
        position = new Vector2(0, 0);
        size = new Vector2(120, 40);
        text = "Button";
        normalColor = Color.GRAY;
        pressedColor = Color.DARK_GRAY;
    }

    public Button(Vector2 position, Vector2 size, String text) {
        this.position = position.cpy();
        this.size = size.cpy();
        this.text = text;
        normalColor = Color.GRAY;
        pressedColor = Color.DARK_GRAY;
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

    /**
     * Sets the button normal color
     *
     * @param color the normal color
     */
    public void setNormalColor(Color color) {
        this.normalColor = color.cpy();
    }

    /**
     * @return the normal color
     */
    public Color getNormalColor() {
        return normalColor.cpy();
    }

    /**
     * Sets the button pressed color
     *
     * @param color the pressed color
     */
    public void setPressedColor(Color color) {
        this.pressedColor = color.cpy();
    }

    /**
     * @return the pressed color
     */
    public Color getPressedColor() {
        return pressedColor.cpy();
    }

    /**
     * Sets the button to pressed or unpressed.
     *
     * @param b whether or not the button should be pressed
     */
    public void setPressed(boolean b) {
        pressed = b;
    }

    /**
     * @return true if the button is pressed
     */
    public boolean isPressed() {
        return pressed;
    }
}
