package com.github.nikitakuchur.puzzlegame.physics;

import com.github.nikitakuchur.puzzlegame.utils.Direction;

public interface Collider {
    boolean checkCollision(PhysicalController physicalController, int x, int y, Direction gravityDirection);
}
