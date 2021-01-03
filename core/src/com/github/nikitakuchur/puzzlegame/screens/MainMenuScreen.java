package com.github.nikitakuchur.puzzlegame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.level.LevelLoader;
import com.github.nikitakuchur.puzzlegame.ui.GameUIActors;
import com.github.nikitakuchur.puzzlegame.ui.MenuType;

import java.io.IOException;

public class MainMenuScreen extends ScreenAdapter {

    private final Stage stage;
    private final GameUIActors gameUIActors;
    private final Game game;

    public MainMenuScreen(Game game, Stage stage) {
        this.game = game;
        stage.getCamera().position.set(Vector3.Zero);
        this.stage = stage;
        this.gameUIActors = new GameUIActors(this, stage);

        gameUIActors.openMenu(MenuType.Main);
    }

    public void setGameScreen() {
        this.game.setScreen(new GameScreen(game));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        gameUIActors.dispose();
    }
}