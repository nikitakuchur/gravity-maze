package com.triateq.gravitymaze.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.core.game.GameObject;

public class PhysicalController<T extends GameObject & PhysicalObject> {

    private static final float ACCELERATION = 2f;
    private Vector2 velocity = Vector2.Zero.cpy();

    private final T physicalObject;

    private Collider collider = FullCollider.INSTANCE;

    private Vector2 prevPosition;
    private Vector2 nextPosition;

    private boolean frozen;

    public PhysicalController(T physicalObject) {
        this.physicalObject = physicalObject;
        prevPosition = getPosition();
        nextPosition = getPosition();
    }

    public void update(float delta) {
        Vector2 position = getPosition();
        Vector2 direction = nextPosition.cpy().sub(prevPosition).nor();

        if (direction.isZero()) {
            velocity = Vector2.Zero.cpy();
            setPosition(prevPosition);
            return;
        }

        velocity.add(direction.cpy().scl(ACCELERATION * delta));

        if (position.cpy().dst(nextPosition) <= velocity.len()) {
            setPosition(nextPosition);
        } else {
            position.add(velocity.cpy());
            setPosition(position);
        }
    }

    public boolean isMoving() {
        return !velocity.isZero();
    }

    public Vector2 getPosition() {
        return physicalObject.getPosition();
    }

    public void setPosition(Vector2 position) {
        physicalObject.setPosition(position);
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity.cpy();
    }

    public Vector2 getPrevPosition() {
        Vector2 position = getPosition();
        if (position.dst(prevPosition) >= 1) {
            prevPosition = new Vector2(Math.round(position.x), Math.round(position.y));
        }
        return prevPosition.cpy();
    }

    public Vector2 getNextPosition() {
        return nextPosition.cpy();
    }

    public void setNextPosition(Vector2 nextPosition) {
        this.nextPosition = nextPosition.cpy();
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }
}
