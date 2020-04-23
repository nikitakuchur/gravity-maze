package com.github.nikitakuchur.puzzlegame.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.github.nikitakuchur.puzzlegame.utils.LevelLoader;

public class GameScreen extends ScreenAdapter {

    private final Stage stage = new Stage(new ScreenViewport());

    private Level level;
    private final GameUI gameUI = new GameUI(this);

    /**
     * Creates a new game screen
     */
    public GameScreen() {
        stage.getCamera().position.set(Vector3.Zero);
        level = LevelLoader.load(Gdx.files.internal("levels/sample.json"));
        stage.addActor(level);
        stage.addActor(gameUI);
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the level
     *
     * @param level the level
     */
    public void setLevel(Level level) {
        int index = stage.getActors().indexOf(this.level, true);
        stage.getActors().set(index, level);
        this.level.dispose();
        this.level = level;
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
        level.dispose();
        gameUI.dispose();
    }
}