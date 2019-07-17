package com.android.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Teleport extends GameObject {

    private final Level level;

    private Teleport teleport;

    private boolean isUsed;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Creates a new teleport
     *
     * @param level the level
     */
    public Teleport(Level level) {
        this.level = level;
        setWidth(100);
        setHeight(100);
    }

    @Override
    public void act(float delta) {
        if (teleport == null) {
            return;
        }

        if (!isUsed) {
            for (Ball ball : level.getBalls()) {
                if (getX() == ball.getX() && getY() == ball.getY()) {
                    ball.setPosition(teleport.getX(), teleport.getY());
                    isUsed = true;
                    teleport.isUsed = true;
                    return;
                }
            }
        }

        // If the teleport is free
        for (Ball ball : level.getBalls()) {
            if ((int) getX() == (int) ball.getX() && (int) getY() == (int) ball.getY()) {
                return;
            }
        }
        isUsed = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.translate(-level.getMap().getWidth() / 2, -level.getMap().getHeight() / 2, 0);
        shapeRenderer.rect(getX() * getWidth(), getY() * getHeight(), getWidth(), getHeight());

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    /**
     * Sets the second teleport
     *
     * @param teleport the teleport
     */
    public void to(Teleport teleport) {
        this.teleport = teleport;
    }

    @Override
    public boolean isInteracting(Ball ball) {
        return !isUsed;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
