package com.github.nikitakuchur.puzzlegame.physics;

public class EmptyCollider implements Collider {

    public static final Collider INSTANCE = new EmptyCollider();

    private EmptyCollider() {
    }

    @Override
    public boolean checkCollision(PhysicalController physicalController, int x, int y, GravityDirection gravityDirection) {
        return false;
    }
}
