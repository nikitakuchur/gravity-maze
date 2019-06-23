package com.android.game.view;

import com.android.game.model.Game;
import com.badlogic.gdx.math.Matrix4;

public class GameRenderer implements Renderer {

    private UIRenderer uiRenderer;
    private LevelRenderer levelRenderer;

    /**
     * Creates a new renderer for the game
     *
     * @param game the game
     */
    public GameRenderer(Game game) {
        uiRenderer = new UIRenderer(game.getLevel().getScore(), game.getButtons());
        levelRenderer = new LevelRenderer(game.getLevel());
    }

    @Override
    public void draw(Matrix4 projectionMatrix) {
        levelRenderer.draw(projectionMatrix);
        uiRenderer.draw(projectionMatrix);
    }

    @Override
    public void dispose() {
        levelRenderer.dispose();
    }

}