package com.android.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hole extends GameObject {

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
    public void act(float delta) {
        for (Ball ball : getBalls()) {
            if (getX() == ball.getX() && getY() == ball.getY()) {
                level.removeActor(ball);
                level.getBalls().remove(ball);
            }
        }
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

    @Override
    public boolean isInteracting(Ball ball) {
        if (getBalls().contains(ball))
            return true;
        return false;
    }
}
