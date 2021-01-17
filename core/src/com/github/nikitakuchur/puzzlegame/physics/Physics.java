package com.github.nikitakuchur.puzzlegame.physics;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.cells.CellType;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.GameObjectStore;
import com.github.nikitakuchur.puzzlegame.utils.Direction;

public class Physics {

    private final Level level;

    public Physics(Level level) {
        this.level = level;
    }

    public void update(float delta) {
        GameObjectStore store = level.getGameObjectStore();
        List<PhysicalController> controllers = store.getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .filter(physicalController -> !physicalController.isFrozen())
                .collect(Collectors.toList());
        controllers.forEach(this::doGravity);
        controllers.forEach(controller -> controller.update(delta));
    }

    private void doGravity(PhysicalController controller) {
        Vector2 gravityDirection = level.getGravityDirection().getDirection();
        Vector2 nextPosition = controller.getPrevPosition().add(gravityDirection);
        nextPosition = new Vector2((int) nextPosition.x, (int) nextPosition.y);
        if (detectCollision(level, (int) nextPosition.x, (int) nextPosition.y, level.getGravityDirection())) {
            nextPosition = controller.getPrevPosition();
        }
        controller.setNextPosition(nextPosition);
    }

    public static boolean detectCollision(Level level, int x, int y, Direction direction) {
        Vector2 nextPosition = new Vector2(x, y);

        while (!detectFrozenObject(level, (int) nextPosition.x, (int) nextPosition.y, direction)
                && level.getMap().getCellType((int) nextPosition.x, (int) nextPosition.y) != CellType.FILLED) {
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
}
