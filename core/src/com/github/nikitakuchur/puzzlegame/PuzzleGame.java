package com.github.nikitakuchur.puzzlegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;

/**
 * This is the main game class.
 */
public class PuzzleGame extends Game {

    public void create() {
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (delta > 0.05f) delta = 0.05f;
        screen.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        FontGenerator.dispose();
    }
}
