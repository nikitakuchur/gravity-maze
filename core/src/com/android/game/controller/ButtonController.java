package com.android.game.controller;

import com.android.game.model.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ButtonController implements Controller {

    private Button button;

    /**
     * Creates a controller for the button
     *
     * @param button the button
     */
    public ButtonController(Button button) {
        this.button = button;
    }

    /**
     * @return the button
     */
    public Button getButton() {
        return button;
    }

    /**
     * Checks click on the button
     *
     * @param position the position of the click
     * @return true if the button can be pressed
     */
    public boolean click(Vector2 position) {
        if (button.getPosition().x <= position.x &&
            button.getPosition().x + button.getSize().x >= position.x &&
            button.getPosition().y <= Gdx.graphics.getHeight() - position.y &&
            button.getPosition().y + button.getSize().y >= Gdx.graphics.getHeight() - position.y) {
            button.setPressed(!button.isPressed());
            return true;
        } else {
            button.setPressed(false);
            return false;
        }
    }

    @Override
    public void update(float deltaTime) {
    }
}
