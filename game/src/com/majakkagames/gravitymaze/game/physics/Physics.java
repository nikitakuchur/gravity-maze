package com.majakkagames.gravitymaze.game.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.core.serialization.annotations.Transient;
import com.majakkagames.gravitymaze.game.gameobjects.Maze;
import com.majakkagames.gravitymaze.game.gameobjects.Gravity;
import com.majakkagames.gravitymaze.game.cells.CellType;
import com.majakkagames.gravitymaze.game.utils.Direction;

@Transient
public class Physics extends GameObject {

    private final Level level;

    private final List<PhysicsListener> listeners = new ArrayList<>();

    public Physics(Level level) {
        this.level = level;
    }

    @Override
    public void act(float delta) {
        GameObjectStore store = level.getGameObjectStore();
        List<PhysicalController<?>> controllers = store.getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .filter(physicalController -> !physicalController.isFrozen())
                .collect(Collectors.toList());
        controllers.forEach(this::doGravity);
        controllers.forEach(controller -> controller.update(delta));
    }

    private void doGravity(PhysicalController<?> controller) {
        Gravity gravity = level.getGameObjectStore()
                .getAnyGameObjectOrThrow(Gravity.class, () -> new IllegalStateException("Cannot find the gravity object"));

        Vector2 gravityDirection = gravity.getGravityDirection().getDirection();
        Vector2 nextPosition = controller.getPrevPosition().add(gravityDirection);
        nextPosition = new Vector2((int) nextPosition.x, (int) nextPosition.y);
        if (detectCollision(level, (int) nextPosition.x, (int) nextPosition.y, gravity.getGravityDirection())) {
            nextPosition = controller.getPrevPosition();
            if (controller.isMoving()) {
                listeners.forEach(listener -> listener.onCollisionDetected(controller));
            }
        }
        controller.setNextPosition(nextPosition);
    }

    public static boolean detectCollision(Level level, int x, int y, Direction direction) {
        Vector2 nextPosition = new Vector2(x, y);

        Maze maze = level.getGameObjectStore()
                .getAnyGameObjectOrThrow(Maze.class, () -> new IllegalStateException("Cannot find the game map object"));

        while (!detectFrozenObject(level, (int) nextPosition.x, (int) nextPosition.y, direction)
                && maze.getCellType((int) nextPosition.x, (int) nextPosition.y) != CellType.FILLED) {
            if (!detectPhysicalObject(level, (int) nextPosition.x, (int) nextPosition.y, direction)) {
                return false;
            }
            nextPosition.add(direction.getDirection());
        }
        return true;
    }

    private static boolean detectFrozenObject(Level level, int x, int y, Direction direction) {
        return level.getGameObjectStore().getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .filter(PhysicalController::isFrozen)
                .anyMatch(controller -> controller.getCollider().checkCollision(controller, x, y, direction));
    }

    private static boolean detectPhysicalObject(Level level, int x, int y, Direction direction) {
        return level.getGameObjectStore().getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .anyMatch(controller -> controller.getCollider().checkCollision(controller, x, y, direction));
    }

    public void addPhysicsListener(PhysicsListener listener) {
        listeners.add(listener);
    }
}
