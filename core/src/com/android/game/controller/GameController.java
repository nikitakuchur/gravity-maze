package com.android.game.controller;

import com.android.game.model.Button;
import com.android.game.model.Game;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class GameController implements Controller {

    private MapController mapController;

    private ButtonController buttonController;
    private List<Button> buttons;

    public GameController(Game game) {
        mapController = new MapController(game.getMap());

        buttons = game.getButtons();

        if (buttons.size() > 0)
            buttonController = new ButtonController(buttons.get(0));
    }

    @Override
    public void update(float deltaTime) {
        mapController.update(deltaTime);

        for (Button button : buttons) {
            buttonController.setButton(button);
            buttonController.update(deltaTime);
        }
    }

    /**
     * Called when the screen was touched or a mouse button was pressed.
     *
     * @param position the position
     */
    public void touchDown(Vector2 position) {
        for (Button button : buttons) {
            buttonController.setButton(button);
            if (!button.isPressed())
                if (buttonController.checkClick(position))
                    return;
        }

        mapController.startMapRotation(position);
    }

    /**
     * Called when a finger was lifted or a mouse button was released.
     *
     * @param position the position
     */
    public void touchUp(Vector2 position) {
        for (Button button : buttons) {
            buttonController.setButton(button);
            if (button.isPressed())
                if (buttonController.checkClick(position))
                    return;
        }

        mapController.stopMapRotation();
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param position the position
     */
    public void touchDragged(Vector2 position) {
        for (Button button : buttons) {
            if (button.isPressed())
                return;
        }

        mapController.updateMapRotation(position);
    }
}