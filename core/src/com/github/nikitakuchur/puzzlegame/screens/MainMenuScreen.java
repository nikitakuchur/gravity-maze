package com.github.nikitakuchur.puzzlegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.ui.menu.MainMenu;
import com.github.nikitakuchur.puzzlegame.utils.Context;

public class MainMenuScreen extends GameScreen {

    private final Stage stage = new Stage(new ScreenViewport());

    public MainMenuScreen(Context context) {
        super(context);
        stage.getCamera().position.set(Vector3.Zero);

        Context mainMenuContext = Context.from(context)
                .gameScreen(this)
                .build();
        MenuStack menuStack = new MenuStack();
        menuStack.push(new MainMenu(mainMenuContext, menuStack));
        stage.addActor(menuStack);
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