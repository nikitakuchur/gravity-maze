package com.majakkagames.gravitymaze.editorcore;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.majakkagames.gravitymaze.core.assets.AssetLoader;
import com.majakkagames.gravitymaze.core.game.Context;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.editorcore.config.Configurator;
import com.majakkagames.gravitymaze.editorcore.utils.FileController;

public class EditorApplication extends ApplicationAdapter {

    private final Configurator configurator;

    private Context context;

    private Stage stage;

    private LevelEditor levelEditor;
    private GameObjectSelector selector;

    private FileController fileController;

    public EditorApplication(Configurator configurator) {
        this.configurator = configurator;
    }

    @Override
    public void create() {
        AssetManager assetManager = new AssetManager();
        AssetLoader.load(assetManager);
        context = Context.builder().assetManager(assetManager).build();

        stage = new Stage(new ScreenViewport());
        stage.getCamera().position.set(Vector3.Zero);

        Level level = new Level(context);
        configurator.getLevelPreparer().accept(level);
        level.initialize();

        LevelManager levelManager = new LevelManager(configurator, level);
        selector = new GameObjectSelector(levelManager);
        levelManager.addEventHandler(LevelManager.EventType.CHANGED, e -> selector.resetSelection());
        levelEditor = new LevelEditor(configurator, levelManager, selector);

        stage.addActor(levelEditor);
        stage.setKeyboardFocus(levelEditor);

        fileController = new FileController(configurator, context, levelEditor);

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
        selector.draw(stage.getBatch());
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public LevelEditor getLevelEditor() {
        return levelEditor;
    }

    public GameObjectSelector getGameObjectSelector() {
        return selector;
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
        selector.dispose();
        context.getAssetManager().dispose();
    }
}
