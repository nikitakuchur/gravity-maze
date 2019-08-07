package com.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.puzzlegame.game.Map;

public class EditorScreen extends ScreenAdapter {

    private Stage stage = new Stage(new ScreenViewport());

    private EditableLevel editableLevel = new EditableLevel();
    private EditorUI editorUI = new EditorUI(this);

    /**
     * Creates a new editor screen
     */
    public EditorScreen() {
        stage.getCamera().position.set(Vector3.Zero);
        stage.addActor(editableLevel);
        stage.addActor(editorUI);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Map map = editableLevel.getMap();
        map.setWidth((float) Gdx.graphics.getWidth() / 2);
        map.setHeight((float) Gdx.graphics.getWidth() / (2 * map.getCellsWidth()) * map.getCellsHeight());
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

    /**
     * @return the editable level
     */
    public EditableLevel getEditableLevel() {
        return editableLevel;
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        editableLevel.dispose();
        editorUI.dispose();
    }
}
