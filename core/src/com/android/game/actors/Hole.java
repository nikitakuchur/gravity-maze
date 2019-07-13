package com.android.game.actors;

import com.android.game.groups.Level;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Hole extends Actor {

    private final Level level;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Creates a new hole
     *
     * @param level the level
     */
    public Hole(Level level) {
        this.level = level;
        setWidth(100);
        setHeight(100);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setColor(getColor());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.translate(-level.getMap().getWidth() / 2, -level.getMap().getHeight() / 2, 0);

        shapeRenderer.ellipse(getX() * getWidth(), getY() * getHeight(),
                getWidth(), getHeight(), 32);

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }
}
