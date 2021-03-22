package com.triateq.gravitymaze.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.triateq.gravitymaze.editor.utils.FileController;
import com.triateq.puzzlecore.assets.AssetLoader;
import com.triateq.puzzlecore.game.Context;

public class EditorApplication extends ApplicationAdapter {

    private Context context;

    private Stage stage;

    private LevelEditor levelEditor;

    private FileController fileController;

    @Override
    public void create() {
        AssetManager assetManager = new AssetManager();
        AssetLoader.load(assetManager);
        context = Context.builder().assetManager(assetManager).build();

        stage = new Stage(new ScreenViewport());
        stage.getCamera().position.set(Vector3.Zero);

        levelEditor = new LevelEditor(context);
        stage.addActor(levelEditor);
        stage.setKeyboardFocus(levelEditor);

        fileController = new FileController(context, levelEditor);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (delta > 0.05f) delta = 0.05f;
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

    public Context getContext() {
        return context;
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        levelEditor.dispose();
        context.getAssetManager().dispose();
    }
}
