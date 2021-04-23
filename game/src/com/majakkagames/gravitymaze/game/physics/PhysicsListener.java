package com.majakkagames.gravitymaze.game.physics;

public interface PhysicsListener {
    void onCollisionDetected(PhysicalController<?> controller);
}
