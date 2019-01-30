package com.android.game.controller;

import com.android.game.model.Game;
import com.badlogic.gdx.math.Vector2;

public class GameController {

    private MapController mapController;

    public GameController(Game game) {
        mapController = new MapController(game.getMap());

    }

    /**
     * Updates the game
     *
     * @param deltaTime the deltaTime
     */
    public void update(float deltaTime) {
        mapController.update(deltaTime);
    }

    /**
     * Called when the screen was touched or a mouse button was pressed.
     *
     * @param position the position
     */
    public void touchDown(Vector2 position) {
        mapController.startMapRotation(position);
    }

    /**
     * Called when a finger was lifted or a mouse button was released.
     *
     * @param position the position
     */
    public void touchUp(Vector2 position) {
        mapController.stopMapRotation();
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param position the position
     */
    public void touchDragged(Vector2 position) {
        mapController.updateMapRotation(position);
    }
}