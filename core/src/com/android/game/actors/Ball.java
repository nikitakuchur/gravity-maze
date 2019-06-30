package com.android.game.actors;

import com.android.game.groups.Level;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ball extends Actor {

    private final Level level;

    private ShapeRenderer shapeRenderer;

    private final float ACCELERATION = 1;
    private float speed = 1;

    private boolean isGrounded;

    /**
     * Creates a new ball
     *
     * @param level the level
     */
    public Ball(Level level) {
        this.level = level;
        setWidth(100);
        setHeight(100);
        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Creates a new ball
     *
     * @param level the level
     * @param color the color of the ball
     */
    public Ball(Level level, Color color) {
        this(level);
        setColor(color);
    }

    @Override
    public void act(float delta) {
        Vector2 target = findTarget();
        Vector2 position = new Vector2(getX(), getY());
        Vector2 direction = target.cpy().sub(position).nor();

        if (position.cpy().sub(target).len() <  speed * delta) {
            setPosition(target.x, target.y);
            speed = 1;
            isGrounded = true;
        } else {
            position.add(direction.scl(speed * delta));
            setPosition(position.x, position.y);
            speed += ACCELERATION;
            isGrounded = false;
        }
    }

    /**
     * Finds the target. The target is a final position of the ball movement.
     */
    private Vector2 findTarget() {
        Map map = level.getMap();
        int w = map.getCells().length;
        int h = map.getCells()[0].length;

        switch (level.getGravityDirection()) {
            case TOP:
                for (int i = (int) getY(); i < h; i++) {
                    if (map.getCellId((int) getX(), i) != 0) {
                        return new Vector2((int) (int) getX(), i - 1);
                    }
                }
                return new Vector2((int) getX(), h - 1);
            case LEFT:
                for (int i = (int) getX(); i >= 0; i--) {
                    if (map.getCellId(i, (int) getY()) != 0) {
                        return new Vector2(i + 1, (int) getY());
                    }
                }
                return new Vector2(0, (int) getY());
            case BOTTOM:
                for (int i = (int) getY(); i >= 0; i--) {
                    if (map.getCellId((int) getX(), i) != 0) {
                        return new Vector2((int) getX(), i + 1);
                    }
                }
                return new Vector2((int) getX(), 0);
            case RIGHT:
                for (int i = (int) getX(); i < w; i++) {
                    if (map.getCellId(i, (int) getY()) != 0) {
                        return new Vector2(i - 1, (int) getY());
                    }
                }
                return new Vector2(w - 1, (int) getY());
        }
        return new Vector2(getX(), getY());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setColor(getColor());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        Map map = level.getMap();

        shapeRenderer.translate(getParent().getX() - map.getWidth() / 2, getParent().getY() - map.getHeight() / 2, 0);

        // Rotate and scale the ball
        shapeRenderer.translate(map.getWidth() / 2, map.getHeight() / 2, 0);
        shapeRenderer.scale(getParent().getScaleX() * getScaleX(), getParent().getScaleY() * getScaleY(), 0);
        shapeRenderer.rotate(0, 0, 1, getParent().getRotation() + getRotation());
        shapeRenderer.translate(-map.getWidth() / 2, -map.getHeight() / 2, 0);

        shapeRenderer.ellipse(getX() * getWidth(), getY() * getHeight(),
                getWidth(), getHeight(), 32);

        shapeRenderer.setColor(0.95f, 0.95f, 0.95f, 1);

        float dw = (getWidth() * 0.7f - getWidth()) / 2;
        float dh = (getHeight() * 0.7f - getHeight()) / 2;
        shapeRenderer.ellipse(getX() * getWidth() - dw, getY() * getHeight() - dh,
                getWidth() * 0.7f, getHeight() * 0.7f, 32);

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    /**
    * @return true if the ball on the ground
    */
    public boolean isGrounded() {
        return isGrounded;
    }

    /**
     * Releases all resources of this object
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}
