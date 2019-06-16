package com.android.game.view;

import com.android.game.model.Game;
import com.badlogic.gdx.math.Matrix4;

public class GameRenderer implements Renderer {

    private UIRenderer uiRenderer;
    private MapRenderer mapRenderer;

    /**
     * Creates a new renderer for the game
     *
     * @param game the game
     */
    public GameRenderer(Game game) {
        uiRenderer = new UIRenderer(game.getMap().getScore(), game.getButtons());
        mapRenderer = new MapRenderer(game.getMap());
    }

    @Override
    public void draw(Matrix4 projectionMatrix) {
        mapRenderer.draw(projectionMatrix);
        uiRenderer.draw(projectionMatrix);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

}