package com.github.nikitakuchur.puzzlegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetScreenCommand;

/**
 * This is the main game class.
 */
public class PuzzleGame extends Game {
    public void create() {
        SetScreenCommand command = new SetScreenCommand(screen -> setScreen(screen));
        command.setScreen(new MainMenuScreen(command, new Stage(new ScreenViewport())));

        command.execute();
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