package com.triateq.gravitymaze.game.physics;

import com.triateq.gravitymaze.game.utils.Direction;

public interface Collider {
    boolean checkCollision(PhysicalController physicalController, int x, int y, Direction gravityDirection);
}
