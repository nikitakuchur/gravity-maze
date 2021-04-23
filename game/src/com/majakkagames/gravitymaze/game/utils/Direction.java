package com.majakkagames.gravitymaze.game.utils;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
    TOP(new Vector2(0, 1)),
    LEFT(new Vector2(-1, 0)),
    BOTTOM(new Vector2(0, -1)),
    RIGHT(new Vector2(1, 0));

    private final Vector2 dir;

    Direction(Vector2 dir) {
        this.dir = dir;
    }

    public Vector2 getDirection() {
        return dir.cpy();
    }
}
