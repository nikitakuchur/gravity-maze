package com.triateq.gravitymaze.physics;

import com.triateq.gravitymaze.utils.Direction;

public interface Collider {
    boolean checkCollision(PhysicalController physicalController, int x, int y, Direction gravityDirection);
}
