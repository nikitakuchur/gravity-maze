package com.android.game.controller;

import com.android.game.model.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class LevelController {

    private Level level;

    private float lastMapAngle;
    private Vector2 lastTouchPosition;

    public LevelController(Level level) {
        this.level = level;

        lastTouchPosition = new Vector2();
    }

    public void update() {
    }

    public void handle(Vector2 touchPosition) {
        Vector2 center = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        level.setRotation(lastMapAngle + touchPosition.cpy().sub(center).angle(lastTouchPosition.cpy().sub(center)));
    }

    public void setLastMapAngle(float angle) {
        this.lastMapAngle = angle;
    }

    public void setLastTouchPosition(Vector2 touchPosition) {
        lastTouchPosition.set(touchPosition);
    }
}