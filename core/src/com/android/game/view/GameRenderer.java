package com.android.game.view;

import com.android.game.model.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class GameRenderer implements Renderer {

    private OrthographicCamera camera;

    private UIRenderer uiRenderer;
    private MapRenderer mapRenderer;

    public GameRenderer(Game game) {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setCameraPosition(new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2));

        uiRenderer = new UIRenderer();
        mapRenderer = new MapRenderer(game.getMap());
    }

    /**
     * Sets the camera position
     *
     * @param position the position
     */
    private void setCameraPosition(Vector2 position) {
        this.camera.position.set(position, 0);
        this.camera.update();
    }

    @Override
    public void draw(Matrix4 projectionMatrix) {
        mapRenderer.draw(camera.combined);
        uiRenderer.draw(camera.combined);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

}