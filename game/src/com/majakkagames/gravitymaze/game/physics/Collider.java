package com.majakkagames.gravitymaze.game.physics;

import com.majakkagames.gravitymaze.game.utils.Direction;

public interface Collider {
    boolean checkCollision(PhysicalController physicalController, int x, int y, Direction gravityDirection);
}
