package com.android.game.controller;

import com.android.game.model.Ball;
import com.android.game.model.Map;
import com.android.game.model.Map.GravityDirection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MapController implements Controller {

    private Map map;

    private List<BallController> ballControllers;

    private float lastMapAngle;
    private Vector2 lastTouchPosition;

    private boolean isMapRotating;
    private boolean zoom;

    private float t;

    private GravityDirection lastState;

    /**
     * Creates a new controller for the map
     *
     * @param map the map
     */
    public MapController(Map map) {
        this.map = map;

        ballControllers = new ArrayList<>();
        for (Ball ball : map.getBalls())
            ballControllers.add(new BallController(ball, map));

        lastTouchPosition = new Vector2();

        isMapRotating = false;
        zoom = false;

        lastState = GravityDirection.TOP;
    }

    @Override
    public void update(float deltaTime) {
        // Zoom out
        if (zoom && t < 1) {
            t += 4 * deltaTime;
            map.setScale(scaleAnimation(t));
        } else if (zoom) {
            map.setScale(scaleAnimation(1));
        }

        // Zoom in
        if (!zoom && t > 0) {
            t -= 4 * deltaTime;
            map.setScale(scaleAnimation(t));
        } else if (!zoom) {
            map.setScale(scaleAnimation(0));
        }

        // Rotate to closest edge
        if (!isMapRotating) {
            float angle = map.getRotation();
            float angleRad = (float) Math.toRadians(angle);
            float speed = 800;

            // Top
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) < 0) {
                map.setRotation(angle + (float) Math.sin(angleRad) * speed * deltaTime);
                map.setState(GravityDirection.TOP);
            }
            // Left
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) < 0) {
                map.setRotation(angle - (float) Math.cos(angleRad) * speed * deltaTime);
                map.setState(GravityDirection.LEFT);
            }
            // Bottom
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) > 0) {
                map.setRotation(angle - (float) Math.sin(angleRad) * speed * deltaTime);
                map.setState(GravityDirection.BOTTOM);
            }
            // Right
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) > 0) {
                map.setRotation(angle + (float) Math.cos(angleRad) * speed * deltaTime);
                map.setState(GravityDirection.RIGHT);
            }

            if (lastState != map.getState()) {
                lastState = map.getState();
                map.getScore().add();
            }

            lastMapAngle = map.getRotation();
        }

        // Balls movement
        if(!isMapRotating)
            updateBalls(deltaTime);
    }

    private void updateBalls(float deltaTime) {
        for (BallController ballController : ballControllers) {
            ballController.update(deltaTime);
        }
    }

    /**
     * Starts map rotation
     *
     * @param position the touch position
     */
    public void startMapRotation(Vector2 position) {
        zoom = true;
        lastTouchPosition.set(position);
        isMapRotating = true;
    }

    /**
     * Stops map rotation
     */
    public void stopMapRotation() {
        zoom = false;
        isMapRotating = false;
    }

    /**
     * Updates map rotation
     */
    public void updateMapRotation(Vector2 position) {
        Vector2 center = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        map.setRotation(lastMapAngle + position.cpy().sub(center).angle(lastTouchPosition.cpy().sub(center)));
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }
}
