package com.github.nikitakuchur.puzzlegame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;

public abstract class GameScreen extends ScreenAdapter {

    private final Game game;

    protected GameScreen(Game game) {
        this.game = game;
    }

    /**
     * Returns the game.
     */
    public Game getGame() {
        return game;
    }
}
