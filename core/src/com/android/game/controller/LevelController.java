package com.android.game.controller;

import com.android.game.model.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class LevelController {

    private Level level;

    private float lastMapAngle;
    private Vector2 lastTouchPosition;

    private boolean mapRotating;
    private boolean zoom;

    private float t;

    public LevelController(Level level) {
        this.level = level;

        lastTouchPosition = new Vector2();

        mapRotating = false;
        zoom = false;
    }

    /**
     * Updates everything
     *
     * @param deltaTime the deltaTime
     */
    public void update(float deltaTime) {
        updateMap(deltaTime);
    }

    /**
     * Updates the map.
     * It's for map animations (rotation, scaling).
     *
     * @param deltaTime the deltaTime
     */
    private void updateMap(float deltaTime) {
        if (zoom && t < 1) {
            t += 4*deltaTime;
            level.setScale(scaleAnimation(t));
        } else if (zoom) {
            level.setScale(scaleAnimation(1));
        }

        if (!zoom && t > 0) {
            t -= 4*deltaTime;
            level.setScale(scaleAnimation(t));
        } else if (!zoom) {
            level.setScale(scaleAnimation(0));
        }

        if (!mapRotating) {
            float angle = level.getRotation();
            float angleRad = (float) Math.toRadians(angle);
            float speed = 800;

            // Bottom
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) > 0) {
                level.setRotation(angle - (float) Math.sin(angleRad) * speed * deltaTime);
            }
            // Left
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) > 0) {
                level.setRotation(angle + (float) Math.cos(angleRad) * speed * deltaTime);
            }
            // Top
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) < 0) {
                level.setRotation(angle + (float) Math.sin(angleRad) * speed * deltaTime);
            }
            // Right
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) < 0) {
                level.setRotation(angle - (float) Math.cos(angleRad) * speed * deltaTime);
            }

            lastMapAngle = level.getRotation();
        }
    }

    /**
     * Start map rotation
     *
     * @param touchPosition the touch position
     */
    public void startMapRotation(Vector2 touchPosition) {
        zoom = true;
        lastTouchPosition.set(touchPosition);
        mapRotating = true;
    }

    /**
     * Updates map rotation
     *
     * @param touchPosition the touch position
     */
    public void updateMapRotation(Vector2 touchPosition) {
        Vector2 center = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        level.setRotation(lastMapAngle + touchPosition.cpy().sub(center).angle(lastTouchPosition.cpy().sub(center)));
    }

    /**
     * Stops map rotation
     */
    public void stopMapRotation() {
        zoom = false;
        mapRotating = false;
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }
}