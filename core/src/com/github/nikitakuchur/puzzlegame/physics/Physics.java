package com.github.nikitakuchur.puzzlegame.physics;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectManager;

public class Physics {

    private final Level level;

    public Physics(Level level) {
        this.level = level;
    }

    public void update(float delta) {
        GameObjectManager manager = level.getGameObjectManager();
        List<PhysicalController> controllers = manager.getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .collect(Collectors.toList());
        controllers.forEach(this::doGravity);
        controllers.forEach(controller -> controller.update(delta));
    }

    private void doGravity(PhysicalController controller) {
        Vector2 gravityDirection = level.getGravityDirection().getDirection();
        Vector2 nextPosition = controller.getPrevPosition().add(gravityDirection);
        nextPosition = new Vector2((int) nextPosition.x, (int) nextPosition.y);
        if (detectCollision(level, (int) nextPosition.x, (int) nextPosition.y)) {
            nextPosition = controller.getPrevPosition();
        }
        controller.setNextPosition(nextPosition);
    }

    public static boolean detectCollision(Level level, int x, int y) {
        Vector2 nextPosition = new Vector2(x, y);
        while (level.getMap().getCellType((int) nextPosition.x, (int) nextPosition.y) != CellType.FILLED) {
            if (!detectPhysicalObject(level, (int) nextPosition.x, (int) nextPosition.y)) {
                return false;
            }
            nextPosition.add(level.getGravityDirection().getDirection());
        }
        return true;
    }

    private static boolean detectPhysicalObject(Level level, int x, int y) {
        return level.getGameObjectManager().getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .map(PhysicalController::getPrevPosition)
                .anyMatch(prev -> prev.x == x && prev.y == y);
    }
}
