package com.puzzlegame.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Hole extends GameObject {

    private final Level level;

    private List<Ball> balls = new ArrayList<>();

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
        super.act(delta);

        for (Ball ball : balls) {
            if (getX() == ball.getX() && getY() == ball.getY()) {
                level.removeBall(ball);
                balls.remove(ball);
                break;
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

    /**
     * Adds a ball that can interact with this hole
     *
     * @param ball the ball
     */
    public void addBall(Ball ball) {
        balls.add(ball);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
