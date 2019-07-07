package com.android.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Actor {

    private Color[] backgroundColor = {new Color(0.81f, 0.45f, 0.5f, 1),
                                       new Color(0.89f, 0.59f, 0.46f, 1),
                                       new Color(0.99f, 0.73f, 0.4f, 1),
                                       new Color(0.89f, 0.59f, 0.46f, 1)};

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Creates a new background
     */
    public Background() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                backgroundColor[0],
                backgroundColor[1],
                backgroundColor[2],
                backgroundColor[3]);
        shapeRenderer.end();
        batch.begin();
    }

    /**
     * Releases all resources of this object
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}
