package com.android.game.controller;

import com.android.game.model.Ball;
import com.android.game.model.Level;
import com.android.game.model.Level.GravityDirection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class LevelController implements Controller {

    private Level level;

    private List<BallController> ballControllers;

    private float lastAngle;
    private Vector2 lastTouchPosition;

    private boolean zoom;
    private boolean rotationLock;

    private float t;

    private GravityDirection lastGravityDirection;

    /**
     * Creates a new controller for the level
     *
     * @param level the level
     */
    public LevelController(Level level) {
        this.level = level;

        ballControllers = new ArrayList<>();
        for (Ball ball : level.getBalls())
            ballControllers.add(new BallController(ball, level));

        lastTouchPosition = new Vector2();

        zoom = false;

        lastGravityDirection = GravityDirection.TOP;
    }

    @Override
    public void update(float deltaTime) {
        // Zoom out
        if (zoom && t < 1) {
            t += 4 * deltaTime;
            level.setScale(scaleAnimation(t));
        } else if (zoom) {
            level.setScale(scaleAnimation(1));
        }

        // Zoom in
        if (!zoom && t > 0) {
            t -= 4 * deltaTime;
            level.setScale(scaleAnimation(t));
        } else if (!zoom) {
            level.setScale(scaleAnimation(0));
        }

        // Rotate to closest edge
        if (!zoom) {
            float angle = level.getRotation();
            float angleRad = (float) Math.toRadians(angle);
            float speed = 800;

            // Top
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) < 0) {
                level.setRotation(angle + (float) Math.sin(angleRad) * speed * deltaTime);
                level.setGravityDirection(GravityDirection.TOP);
            }
            // Left
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) < 0) {
                level.setRotation(angle - (float) Math.cos(angleRad) * speed * deltaTime);
                level.setGravityDirection(GravityDirection.LEFT);
            }
            // Bottom
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) > 0) {
                level.setRotation(angle - (float) Math.sin(angleRad) * speed * deltaTime);
                level.setGravityDirection(GravityDirection.BOTTOM);
            }
            // Right
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) > 0) {
                level.setRotation(angle + (float) Math.cos(angleRad) * speed * deltaTime);
                level.setGravityDirection(GravityDirection.RIGHT);
            }

            if (lastGravityDirection != level.getGravityDirection()) {
                lastGravityDirection = level.getGravityDirection();
                level.getScore().add();
            }

            lastAngle = level.getRotation();
        }

        // Balls movement
        if(!zoom)
            updateBalls(deltaTime);
    }

    private void updateBalls(float deltaTime) {
        for (BallController ballController : ballControllers) {
            ballController.update(deltaTime);
        }
    }

    /**
     * Starts level rotation
     *
     * @param position the touch position
     */
    public void startLevelRotation(Vector2 position) {
        if (!areBallsGrounded()) {
            rotationLock = true;
            return;
        }
        rotationLock = false;
        zoom = true;
        lastTouchPosition.set(position);
    }

    /**
     * Stops level rotation
     */
    public void stopLevelRotation() {
        rotationLock = false;
        zoom = false;
    }

    /**
     * Updates level rotation
     */
    public void updateLevelRotation(Vector2 position) {
        if (rotationLock)
            return;
        Vector2 center = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        level.setRotation(lastAngle + position.cpy().sub(center).angle(lastTouchPosition.cpy().sub(center)));
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }

    private boolean areBallsGrounded() {
        for (BallController ballController : ballControllers) {
            if (!ballController.isGrounded())
                return false;
        }
        return true;
    }
}
