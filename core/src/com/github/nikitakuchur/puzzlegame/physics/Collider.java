package com.github.nikitakuchur.puzzlegame.physics;

public interface Collider {
    boolean checkCollision(PhysicalController physicalController, int x, int y, GravityDirection gravityDirection);
}
