package com.triateq.gravitymaze.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.triateq.gravitymaze.level.Level;
import com.triateq.gravitymaze.level.LevelLoader;
import com.triateq.gravitymaze.ui.MenuStack;
import com.triateq.gravitymaze.ui.level.LevelResult;
import com.triateq.gravitymaze.ui.level.LevelUI;
import com.triateq.gravitymaze.utils.Context;

import java.io.IOException;

public class LevelScreen extends GameScreen {

    private final Stage stage = new Stage(new ScreenViewport());

    private final FileHandle levelFile;

    private final LevelLoader levelLoader;
    private Level level;

    private final MenuStack menuStack;

    /**
     * Creates a new level screen.
     */
    public LevelScreen(Context context, FileHandle levelFile) {
        super(context);
        this.levelFile = levelFile;
        levelLoader = new LevelLoader(context);

        stage.getCamera().position.set(Vector3.Zero);

        try {
            level = levelLoader.load(levelFile);
            level.addGameEndListener(this::showResultMenu);
            stage.addActor(level);
        } catch (IOException e) {
            Gdx.app.error(getClass().getName(), e.getMessage(), e);
        }

        menuStack = new MenuStack();
        menuStack.push(new LevelUI(getContext(), menuStack));
        stage.addActor(menuStack);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    menuStack.peek().back();
                }
                return false;
            }
        });
    }

    /**
     * Returns the level.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Resets the level.
     */
    public void resetLevel() {
        try {
            int index = stage.getActors().indexOf(level, true);
            Level loadedLevel = levelLoader.load(levelFile);
            loadedLevel.addGameEndListener(this::showResultMenu);
            stage.getActors().set(index, loadedLevel);
            level.dispose();
            level = loadedLevel;
        } catch (IOException e) {
            Gdx.app.error(getClass().getName(), e.getMessage(), e);
        }
    }

    public void showResultMenu(int stars) {
        LevelResult resultMenu = new LevelResult(getContext(), menuStack, stars);
        menuStack.push(resultMenu);
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
    }
}