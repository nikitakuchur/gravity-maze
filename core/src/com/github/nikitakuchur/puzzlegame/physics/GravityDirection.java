package com.github.nikitakuchur.puzzlegame.physics;

import com.badlogic.gdx.math.Vector2;

public enum GravityDirection {
    TOP(new Vector2(0, 1)),
    LEFT(new Vector2(-1, 0)),
    BOTTOM(new Vector2(0, -1)),
    RIGHT(new Vector2(1, 0));

    private final Vector2 direction;

    GravityDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Vector2 getDirection() {
        return direction.cpy();
    }
}
