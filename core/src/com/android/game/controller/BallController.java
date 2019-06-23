package com.android.game.controller;

import com.android.game.model.Ball;
import com.android.game.model.Level;
import com.badlogic.gdx.math.Vector2;

public class BallController implements Controller{

    private Ball ball;
    private Level level;

    private final float SPEED = 6;
    private final float ACCELERATION = 0.4f;
    private float da = 0;

    private boolean isGrounded;

    /**
     * Creates a controller for the ball
     *
     * @param ball the button
     * @param level the level
     */
    public BallController(Ball ball, Level level) {
        this.ball = ball;
        this.level = level;
    }

    /**
     * @return the ball
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
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
        switch (level.getGravityDirection()) {
            case BOTTOM:
                for (int i = (int) ball.getPosition().y; i >= 0; i--) {
                    if (level.getCellId((int) ball.getPosition().x, i) != 0) {
                        return new Vector2((int) ball.getPosition().x, i + 1);
                    }
                }
                return new Vector2((int) ball.getPosition().x, 0);
            case RIGHT:
                for (int i = (int) ball.getPosition().x; i >= 0; i--) {
                    if (level.getCellId(i, (int) ball.getPosition().y) != 0) {
                        return new Vector2(i + 1, (int) ball.getPosition().y);
                    }
                }
                return new Vector2(0, (int) ball.getPosition().y);
            case TOP:
                for (int i = (int) ball.getPosition().y; i < level.getHeight(); i++) {
                    if (level.getCellId((int) ball.getPosition().x, i) != 0) {
                        return new Vector2((int) (int) ball.getPosition().x, i - 1);
                    }
                }
                return new Vector2((int) ball.getPosition().x, level.getHeight() - 1);
            case LEFT:
                for (int i = (int) ball.getPosition().x; i < level.getWidth(); i++) {
                    if (level.getCellId(i, (int) ball.getPosition().y) != 0) {
                        return new Vector2(i - 1, (int) ball.getPosition().y);
                    }
                }
                return new Vector2(level.getWidth() - 1, (int) ball.getPosition().y);
        }
        return ball.getPosition();
    }

    /**
     * @return true if the ball on the ground
     */
    public boolean isGrounded() {
        return isGrounded;
    }
}
