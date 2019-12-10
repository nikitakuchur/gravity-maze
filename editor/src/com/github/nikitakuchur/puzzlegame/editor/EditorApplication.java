package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class EditorApplication extends ApplicationAdapter {

    private Stage stage;

    private EditableLevel editableLevel;

    private List<Runnable> listeners = new ArrayList<>();

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        stage.getCamera().position.set(Vector3.Zero);

        editableLevel = new EditableLevel();
        stage.addActor(editableLevel);

        Gdx.input.setInputProcessor(stage);

        listeners.forEach(Runnable::run);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public EditableLevel getEditableLevel() {
        return editableLevel;
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        editableLevel.dispose();
    }

    public void addOnCreateListener(Runnable runnable) {
        listeners.add(runnable);
    }
}
