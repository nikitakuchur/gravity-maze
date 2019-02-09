package com.android.game.controller;

import com.android.game.model.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ButtonController implements Controller {

    private Button button;

    public ButtonController(Button button) {
        this.button = button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public boolean checkClick(Vector2 position) {
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
