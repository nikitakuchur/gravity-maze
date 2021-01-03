package com.github.nikitakuchur.puzzlegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.badlogic.gdx.Game;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;

/**
 * This is the main game class.
 */
public class PuzzleGame extends Game {
    public void create() {
        this.setScreen(new MainMenuScreen(this, new Stage(new ScreenViewport())));
//        TO skip menu uncomment this:
//        this.setScreen(new GameScreen());
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