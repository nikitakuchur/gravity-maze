package com.triateq.gravitymaze.physics;

import com.triateq.gravitymaze.utils.Direction;

public class EmptyCollider implements Collider {

    public static final Collider INSTANCE = new EmptyCollider();

    private EmptyCollider() {
    }

    @Override
    public boolean checkCollision(PhysicalController physicalController, int x, int y, Direction gravityDirection) {
        return false;
    }
}
