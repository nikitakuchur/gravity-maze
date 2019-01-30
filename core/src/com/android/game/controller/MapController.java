package com.android.game.controller;

import com.android.game.model.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class MapController {

    private Map map;

    private float lastMapAngle;
    private Vector2 lastTouchPosition;

    private boolean mapRotating;
    private boolean zoom;

    private float t;

    public MapController(Map map) {
        this.map = map;

        lastTouchPosition = new Vector2();

        mapRotating = false;
        zoom = false;
    }

    /**
     * Updates the map.
     * It's for map animations (rotation, scaling).
     *
     * @param deltaTime the deltaTime
     */
    public void update(float deltaTime) {
        // Zoom out
        if (zoom && t < 1) {
            t += 4*deltaTime;
            map.setScale(scaleAnimation(t));
        } else if (zoom) {
            map.setScale(scaleAnimation(1));
        }

        // Zoom in
        if (!zoom && t > 0) {
            t -= 4*deltaTime;
            map.setScale(scaleAnimation(t));
        } else if (!zoom) {
            map.setScale(scaleAnimation(0));
        }

        // Rotate to closest edge
        if (!mapRotating) {
            float angle = map.getRotation();
            float angleRad = (float) Math.toRadians(angle);
            float speed = 800;

            // Bottom
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) > 0) {
                map.setRotation(angle - (float) Math.sin(angleRad) * speed * deltaTime);
            }
            // Left
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) > 0) {
                map.setRotation(angle + (float) Math.cos(angleRad) * speed * deltaTime);
            }
            // Top
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) < 0) {
                map.setRotation(angle + (float) Math.sin(angleRad) * speed * deltaTime);
            }
            // Right
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) < 0) {
                map.setRotation(angle - (float) Math.cos(angleRad) * speed * deltaTime);
            }

            lastMapAngle = map.getRotation();
        }
    }

    public void startMapRotation(Vector2 position) {
        zoom = true;
        lastTouchPosition.set(position);
        mapRotating = true;
    }

    public void stopMapRotation() {
        zoom = false;
        mapRotating = false;
    }

    public void updateMapRotation(Vector2 position) {
        Vector2 center = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        map.setRotation(lastMapAngle + position.cpy().sub(center).angle(lastTouchPosition.cpy().sub(center)));
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }
}
