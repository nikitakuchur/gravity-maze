package com.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Background extends Actor implements Disposable {

    private Color[] backgroundColor = {new Color(0.29f, 0.58f, 0.75f, 1),
                                       new Color(0.58f, 0.5f, 0.76f, 1),
                                       new Color(0.32f, 0.58f, 0.75f, 1),
                                       new Color(0.03f, 0.66f, 0.73f, 1)};

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

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
