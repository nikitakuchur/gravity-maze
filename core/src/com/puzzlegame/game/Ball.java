package com.puzzlegame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Ball extends Actor implements Disposable {

    private final Level level;

    private final float ACCELERATION = 100;
    private float speed = 1;

    private boolean isGrounded;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Creates a new ball
     *
     * @param level the level
     */
    public Ball(Level level) {
        this.level = level;
        setWidth(100);
        setHeight(100);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Vector2 target = findTarget();
        Vector2 position = new Vector2(getX(), getY());
        Vector2 direction = target.cpy().sub(position).nor();

        if (position.cpy().sub(target).len() <  speed * delta) {
            setPosition(target.x, target.y);
            if (isGrounded) {
                speed = 1;
            }
        } else {
            position.add(direction.scl(speed * delta));
            setPosition(position.x, position.y);
            speed += ACCELERATION * delta;
            isGrounded = false;
        }
    }

    /**
     * Finds the target. The target is a final position of the ball movement.
     */
    private Vector2 findTarget() {
        Map map = level.getMap();

        switch (level.getGravityDirection()) {
            case TOP:
                if (map.getCellId((int) getX(), (int) getY() + 1) != 1) {
                    return new Vector2((int) getX(), (int) getY() + 1);
                }
                break;
            case LEFT:
                if (map.getCellId((int) Math.ceil(getX()) - 1, (int) getY()) != 1) {
                    return new Vector2((int) Math.ceil(getX()) - 1, (int) getY());
                }
                break;
            case BOTTOM:
                if (map.getCellId((int) getX(), (int) Math.ceil(getY()) - 1) != 1) {
                    return new Vector2((int) getX(), (int) Math.ceil(getY()) - 1);
                }
                break;
            case RIGHT:
                if (map.getCellId((int) getX() + 1, (int) getY()) != 1) {
                    return new Vector2((int) getX() + 1, (int) getY());
                }
                break;
        }
        isGrounded = true;

        return new Vector2(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.translate(- level.getMap().getWidth() / 2, -level.getMap().getHeight() / 2, 0);

        shapeRenderer.setColor(getColor());
        shapeRenderer.ellipse(getX() * getWidth(), getY() * getHeight(),
                getWidth(), getHeight(), 32);

        shapeRenderer.setColor(Color.WHITE);

        float dw = (getWidth() * 0.7f - getWidth()) / 2;
        float dh = (getHeight() * 0.7f - getHeight()) / 2;
        shapeRenderer.ellipse(getX() * getWidth() - dw, getY() * getHeight() - dh,
                getWidth() * 0.7f, getHeight() * 0.7f, 32);

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    /**
    * @return true if the ball on the ground and false otherwise
    */
    public boolean isGrounded() {
        return isGrounded;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
