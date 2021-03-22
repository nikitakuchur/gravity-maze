package com.triateq.gravitymaze.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.triateq.gravitymaze.core.ui.MenuStack;
import com.triateq.gravitymaze.game.ui.menu.MainMenu;
import com.triateq.gravitymaze.core.game.Context;
import com.triateq.gravitymaze.core.game.GameScreen;

public class MenuScreen extends GameScreen {

    private final Stage stage = new Stage(new ScreenViewport());

    public MenuScreen(Context context) {
        super(context);
        stage.getCamera().position.set(Vector3.Zero);

        MenuStack menuStack = new MenuStack();
        menuStack.push(new MainMenu(getContext(), menuStack));
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClearColor(2.f / 255.f, 79.f / 255.f, 114.f / 255.f, 1);
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
