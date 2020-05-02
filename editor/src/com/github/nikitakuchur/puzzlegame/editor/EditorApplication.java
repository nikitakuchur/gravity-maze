package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.editor.utils.FileController;

public class EditorApplication extends ApplicationAdapter {

    private Stage stage;

    private LevelEditor levelEditor;

    private FileController fileController;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        stage.getCamera().position.set(Vector3.Zero);

        levelEditor = new LevelEditor();
        stage.addActor(levelEditor);
        stage.setKeyboardFocus(levelEditor);

        fileController = new FileController(levelEditor);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (delta > 1) delta = 1;
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public LevelEditor getLevelEditor() {
        return levelEditor;
    }

    public FileController getFileController() {
        return fileController;
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        levelEditor.dispose();
    }
}
