package com.github.nikitakuchur.puzzlegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.nikitakuchur.puzzlegame.ui.actors.MainMenuGroup;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetScreenCommand;

public class MainMenuScreen extends ScreenAdapter {
    private final Stage stage;

    public MainMenuScreen(SetScreenCommand command, Stage stage) {
        stage.getCamera().position.set(Vector3.Zero);
        this.stage = stage;
        stage.addActor(new MainMenuGroup(command));
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
    }
}