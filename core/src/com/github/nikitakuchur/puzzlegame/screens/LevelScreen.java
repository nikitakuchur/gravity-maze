package com.github.nikitakuchur.puzzlegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.level.LevelLoader;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.ui.level.LevelUI;
import com.github.nikitakuchur.puzzlegame.utils.Context;

import java.io.IOException;

public class LevelScreen extends GameScreen {

    private final Stage stage = new Stage(new ScreenViewport());

    private final FileHandle levelFile;

    private final LevelLoader levelLoader;
    private Level level;

    /**
     * Creates a new level screen.
     */
    public LevelScreen(Context context, FileHandle levelFile) {
        super(context);
        this.levelFile = levelFile;
        levelLoader = new LevelLoader(context);

        stage.getCamera().position.set(Vector3.Zero);

        try {
            this.level = levelLoader.load(levelFile);
            stage.addActor(level);
        } catch (IOException e) {
            Gdx.app.error(getClass().getName(), e.getMessage(), e);
        }

        Context gameUiContext = Context.from(context)
                .gameScreen(this)
                .build();
        MenuStack menuStack = new MenuStack();
        menuStack.push(new LevelUI(gameUiContext, menuStack));
        stage.addActor(menuStack);
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
            int index = stage.getActors().indexOf(this.level, true);
            Level loadedLevel = levelLoader.load(levelFile);
            stage.getActors().set(index, loadedLevel);
            this.level.dispose();
            this.level = loadedLevel;
        } catch (IOException e) {
            Gdx.app.error(getClass().getName(), e.getMessage(), e);
        }
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
