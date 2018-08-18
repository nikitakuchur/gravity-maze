package com.android.game;

import com.android.game.controller.LevelController;
import com.android.game.model.Level;
import com.android.game.view.LevelRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter {

    private Level level;
    private LevelRenderer levelRenderer;
    private LevelController levelController;

    @Override
    public void show() {
        level = new Level();
        levelRenderer = new LevelRenderer(level);
        levelController = new LevelController(level);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        levelController.update();
        levelRenderer.draw();
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
    }

    @Override
    public void dispose() {
    }
}