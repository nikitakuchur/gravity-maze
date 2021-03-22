package com.triateq.gravitymaze.actors.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Switch extends GameObject {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Ball lastBall;
    private boolean activated;

    @Override
    public void act(float delta) {
        super.act(delta);

        GameObjectStore store = level.getGameObjectStore();
        Ball ball = store.getGameObjects(Ball.class).stream()
                .filter(b -> getPosition().equals(b.getPosition()))
                .findAny()
                .orElse(null);
        if (ball == null) {
            lastBall = null;
            return;
        }
        if (lastBall != ball) {
            lastBall = ball;
            activated = !activated;
            store.getGameObjects(Switchable.class).stream()
                    .filter(s -> s.getSwitch() != null && s.getSwitch().equals(getName()))
                    .forEach(Switchable::onSwitch);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        Vector2 position = getActualPosition();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(getColor());
        shapeRenderer.ellipse(position.x, position.y, getWidth(), getHeight(), 3);

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }
}
