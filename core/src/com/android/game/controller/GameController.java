package com.android.game.controller;

import com.android.game.model.Button;
import com.android.game.model.Event;
import com.android.game.model.Game;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameController implements Controller {

    private LevelController levelController;

    private List<ButtonController> buttonControllers;

    /**
     * Creates a new controller for the game
     *
     * @param game the game
     */
    public GameController(Game game) {
        levelController = new LevelController(game.getLevel());

        buttonControllers = new ArrayList<>();
        for (Button button : game.getButtons())
            buttonControllers.add(new ButtonController(button));
    }

    @Override
    public void update(float deltaTime) {
        levelController.update(deltaTime);

        for (ButtonController buttonController : buttonControllers) {
            buttonController.update(deltaTime);
        }
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param position the position
     */
    public void touchDown(Vector2 position) {
        for (ButtonController buttonController : buttonControllers) {
            Button button = buttonController.getButton();
            if (!button.isPressed() && buttonController.click(position))
                return;
        }
        levelController.startLevelRotation(position);
    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param position the position
     */
    public void touchUp(Vector2 position) {
        for (ButtonController buttonController : buttonControllers) {
            Button button = buttonController.getButton();
            if (button.isPressed() && buttonController.click(position)) {
                Optional.of(button).map(Button::getOnAction).ifPresent(Event::handle);
                return;
            }
        }
        levelController.stopLevelRotation();
    }

    /**
     * Called when a finger or the mouse was dragged
     *
     * @param position the position
     */
    public void touchDragged(Vector2 position) {
        for (ButtonController buttonController : buttonControllers) {
            Button button = buttonController.getButton();
            if (button.isPressed())
                return;
        }
        levelController.updateLevelRotation(position);
    }
}