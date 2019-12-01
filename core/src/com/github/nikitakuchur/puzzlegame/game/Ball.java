package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Ball extends GameObject {

    private static final float ACCELERATION = 200;
    private float speed = 1;

    private boolean isGrounded;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void act(Level level, float delta) {
        super.act(level, delta);

        Vector2 target = findTarget(level);
        Vector2 position = new Vector2(getX(), getY());
        Vector2 direction = target.cpy().sub(position).nor();

        if (position.cpy().sub(target).len() < speed * delta) {
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
    private Vector2 findTarget(Level level) {
        switch (level.getGravityDirection()) {
            case TOP:
                if (checkCollision(level, (int) getX(), (int) getY() + 1)) {
                    return new Vector2((int) getX(), (int) (getY() + 1));
                }
                break;
            case LEFT:
                if (checkCollision(level, (int) Math.ceil(getX()) - 1, (int) getY())) {
                    return new Vector2((int) Math.ceil(getX() - 1), (int) getY());
                }
                break;
            case BOTTOM:
                if (checkCollision(level, (int) getX(), (int) Math.ceil(getY()) - 1)) {
                    return new Vector2((int) getX(), (int) Math.ceil(getY() - 1));
                }
                break;
            case RIGHT:
                if (checkCollision(level, (int) getX() + 1, (int) getY())) {
                    return new Vector2((int) (getX() + 1), (int) getY());
                }
                break;
        }
        isGrounded = true;
        return new Vector2(getX(), getY());
    }

    private boolean checkCollision(Level level, int x, int y) {
        Map map = level.getMap();
        if (map.getCellType(x, y) == CellType.BLOCK) {
            return false;
        }
        for (Ball ball : level.getGameObjects(Ball.class)) {
            if (ball.getX() == x && ball.getY() == y) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.translate(-getParent().getWidth() / 2, -getParent().getHeight() / 2, 0);

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
