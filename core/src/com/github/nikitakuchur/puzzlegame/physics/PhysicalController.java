package com.github.nikitakuchur.puzzlegame.physics;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;

public class PhysicalController {

    private static final float ACCELERATION = 1;
    private Vector2 velocity = Vector2.Zero.cpy();

    private final GameObject gameObject;

    private Vector2 prevPosition;
    private Vector2 nextPosition;

    private PhysicalObject collisionObject;

    public PhysicalController(GameObject gameObject) {
        this.gameObject = gameObject;
        prevPosition = new Vector2(gameObject.getX(), gameObject.getY());
        nextPosition = new Vector2(gameObject.getX(), gameObject.getY());
    }

    public void update(Level level, float delta) {
        Vector2 position = new Vector2(gameObject.getX(), gameObject.getY());
        Vector2 direction = nextPosition.cpy().sub(position).nor();

        if (collisionObject != null) {
            velocity = collisionObject.getPhysicalController().velocity;
        } else if (direction.isZero()) {
            velocity = Vector2.Zero.cpy();
        }

        if (position.cpy().dst(nextPosition) < velocity.len()) {
            getGameObject().setPosition((int) nextPosition.x, (int) nextPosition.y);
            prevPosition = nextPosition.cpy();
        } else {
            velocity.add(direction.cpy().scl(ACCELERATION * delta));
            position.add(velocity);
            getGameObject().setPosition(position.x, position.y);
        }
    }

    public void calcNextPosition(Level level) {
        Vector2 position = new Vector2(getGameObject().getX(), getGameObject().getY());
        if (position.dst(prevPosition) >= 1) {
            prevPosition = new Vector2(Math.round(position.x), Math.round(position.y));
        }
        nextPosition = nextPosition(level);
    }

    private Vector2 nextPosition(Level level) {
        Vector2 gravityDirection = level.getGravityDirection().getDirection();
        Vector2 vec = prevPosition.cpy().add(gravityDirection);
        vec = new Vector2((int) vec.x, (int) vec.y);

        if (detectCollision(level, (int) vec.x, (int) vec.y)) {
            return new Vector2(Math.round(prevPosition.x), Math.round(prevPosition.y));
        }

        return vec;
    }

    private boolean detectCollision(Level level, int x, int y) {
        Vector2 position = new Vector2(x, y);
        GameMap map = level.getMap();
        if (map.getCellType(x, y) == CellType.BLOCK) {
            return true;
        }
        collisionObject = null;
        List<PhysicalObject> physicalObjects = level.getGameObjectsManager().getGameObjects(PhysicalObject.class);
        for (PhysicalObject physicalObject : physicalObjects) {
            if (physicalObject == getGameObject()) continue;
            Vector2 objNextPosition = physicalObject.getPhysicalController().nextPosition;
            if (position.dst(objNextPosition) < 1) {
                collisionObject = physicalObject;
                return true;
            }
        }
        return false;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public boolean isMoving() {
        return !velocity.isZero();
    }
}
