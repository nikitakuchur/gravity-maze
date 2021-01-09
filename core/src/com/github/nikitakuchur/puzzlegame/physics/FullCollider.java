package com.github.nikitakuchur.puzzlegame.physics;

import com.badlogic.gdx.math.Vector2;

public class FullCollider implements Collider {

    public static final Collider INSTANCE = new FullCollider();

    private FullCollider() {
    }

    @Override
    public boolean checkCollision(PhysicalController physicalController, int x, int y, GravityDirection gravityDirection) {
        Vector2 prev = physicalController.getPrevPosition();
        return prev.x == x && prev.y == y;
    }
}
