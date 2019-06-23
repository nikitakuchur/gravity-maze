package com.android.game.controller;

import com.android.game.model.Ball;
import com.android.game.model.Map;
import com.badlogic.gdx.math.Vector2;

public class BallController implements Controller{

    private Ball ball;
    private Map map;

    private final float SPEED = 6;
    private final float ACCELERATION = 0.4f;
    private float da = 0;

    private boolean isGrounded;

    /**
     * Creates a controller for the ball
     *
     * @param ball the button
     * @param map the map
     */
    public BallController(Ball ball, Map map) {
        this.ball = ball;
        this.map = map;
    }

    /**
     * @return the ball
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    @Override
    public void update(float deltaTime) {
        Vector2 target = findTarget();
        Vector2 direction = findTarget().cpy().sub(ball.getPosition()).nor();

        if (ball.getPosition().sub(target).len() < (SPEED + da) * deltaTime) {
            ball.setPosition(target);
            da = 0;
            isGrounded = true;
        } else {
            ball.setPosition(ball.getPosition().add(direction.scl((SPEED + da) * deltaTime)));
            da += ACCELERATION;
            isGrounded = false;
        }
    }

    /**
     * Finds the target. The target is a final position of the ball movement.
     */
    private Vector2 findTarget() {
        switch (map.getState()) {
            case BOTTOM:
                for (int i = (int) ball.getPosition().y; i >= 0; i--) {
                    if (map.getCellId((int) ball.getPosition().x, i) != 0) {
                        return new Vector2((int) ball.getPosition().x, i + 1);
                    }
                }
                return new Vector2((int) ball.getPosition().x, 0);
            case RIGHT:
                for (int i = (int) ball.getPosition().x; i >= 0; i--) {
                    if (map.getCellId(i, (int) ball.getPosition().y) != 0) {
                        return new Vector2(i + 1, (int) ball.getPosition().y);
                    }
                }
                return new Vector2(0, (int) ball.getPosition().y);
            case TOP:
                for (int i = (int) ball.getPosition().y; i < map.getHeight(); i++) {
                    if (map.getCellId((int) ball.getPosition().x, i) != 0) {
                        return new Vector2((int) (int) ball.getPosition().x, i - 1);
                    }
                }
                return new Vector2((int) ball.getPosition().x, map.getHeight() - 1);
            case LEFT:
                for (int i = (int) ball.getPosition().x; i < map.getWidth(); i++) {
                    if (map.getCellId(i, (int) ball.getPosition().y) != 0) {
                        return new Vector2(i - 1, (int) ball.getPosition().y);
                    }
                }
                return new Vector2(map.getWidth() - 1, (int) ball.getPosition().y);
        }
        return ball.getPosition();
    }

    public boolean isGrounded() {
        return isGrounded;
    }
}
