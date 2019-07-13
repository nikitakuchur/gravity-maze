package com.android.game.screens;

import com.android.game.groups.GameScreenUI;
import com.android.game.groups.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;

public class GameScreen implements Screen {

    private Stage stage = new Stage();

    private Level level = new Level();
    private GameScreenUI gameScreenUI = new GameScreenUI(this);

    /**
     * Creates a new game screen
     */
    public GameScreen() {
        level.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

        stage.addActor(level);
        stage.addActor(gameScreenUI);
    }

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the level
     *
     * @param level the level
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
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
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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
        gameScreenUI.dispose();
    }
}