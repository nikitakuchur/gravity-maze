package com.triateq.gravitymaze.game.physics;

public interface PhysicsListener {
    void onCollisionDetected(PhysicalController<?> controller);
}
