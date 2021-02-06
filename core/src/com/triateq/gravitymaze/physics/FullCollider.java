package com.triateq.gravitymaze.physics;

import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.utils.Direction;

public class FullCollider implements Collider {

    public static final Collider INSTANCE = new FullCollider();

    private FullCollider() {
    }

    @Override
    public boolean checkCollision(PhysicalController physicalController, int x, int y, Direction gravityDirection) {
        Vector2 prev = physicalController.getPrevPosition();
        return prev.x == x && prev.y == y;
    }
}