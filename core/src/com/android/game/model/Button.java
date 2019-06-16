package com.android.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Button {

    private Vector2 position;
    private Vector2 size;
    private String text;

    private Color color;
    private Color pressedColor;

    private boolean isPressed;

    private Event event;

    /**
     * Creates a new button
     */
    public Button() {
        position = new Vector2(0, 0);
        size = new Vector2(120, 40);
        text = "Button";
        color = Color.GRAY;
        pressedColor = Color.DARK_GRAY;
    }

    /**
     * Creates a new button
     *
     * @param position the position of the button
     * @param size the size of the button
     * @param text the text to show in the button
     */
    public Button(Vector2 position, Vector2 size, String text) {
        this.position = position.cpy();
        this.size = size.cpy();
        this.text = text;
        color = Color.GRAY;
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
     * Sets the text to show in the button
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
     * Sets the button color
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color.cpy();
    }

    /**
     * @return the button color
     */
    public Color getColor() {
        return color.cpy();
    }

    /**
     * Sets the button pressed color
     *
     * @param color the color
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
     * Sets the button to pressed or unpressed
     *
     * @param b whether or not the button should be pressed
     */
    public void setPressed(boolean b) {
        isPressed = b;
    }

    /**
     * @return true if the button is pressed
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Sets the event on the button
     *
     * @param event the event
     */
    public void setOnAction(Event event) {
        this.event = event;
    }

    /**
     * @return the button event
     */
    public Event getOnAction() {
        return event;
    }
}
