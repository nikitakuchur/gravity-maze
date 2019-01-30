package com.android.game.view;

import com.android.game.model.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class GameRenderer {

    private OrthographicCamera camera;

    private MapRenderer mapRenderer;

    public GameRenderer(Game game) {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setCameraPosition(new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getWidth() / 2));

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

    /**
     * Draws the game
     */
    public void draw() {
        mapRenderer.draw(camera.combined);
    }

}