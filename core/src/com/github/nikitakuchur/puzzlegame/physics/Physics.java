package com.github.nikitakuchur.puzzlegame.physics;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectsManager;

public class Physics {

    private final Level level;

    public Physics(Level level) {
        this.level = level;
    }

    public void update(float delta) {
        GameObjectsManager manager = level.getGameObjectsManager();
        List<PhysicalController> controllers = manager.getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .collect(Collectors.toList());
        controllers.forEach(controller -> controller.update(delta));
        controllers.forEach(this::doGravity);
        controllers.forEach(this::detectCollision);
    }

    private void doGravity(PhysicalController controller) {
        Vector2 gravityDirection = level.getGravityDirection().getDirection();
        Vector2 nextPosition = controller.getPrevPosition().add(gravityDirection);
        nextPosition = new Vector2((int) nextPosition.x, (int) nextPosition.y);
        if (detectCollisionWithMap(level.getMap(), (int) nextPosition.x, (int) nextPosition.y)) {
            nextPosition = controller.getPrevPosition();
        }
        controller.setNextPosition(nextPosition);
    }

    private boolean detectCollisionWithMap(GameMap map, int x, int y) {
        return map.getCellType(x, y) == CellType.BLOCK;
    }

    private void detectCollision(PhysicalController mainController) {
        GameObjectsManager manager = level.getGameObjectsManager();
        if (mainController.isMoving()) return;
        List<PhysicalController> controllers = manager.getGameObjects(PhysicalObject.class).stream()
                .map(PhysicalObject::getPhysicalController)
                .collect(Collectors.toList());
        for (PhysicalController controller : controllers) {
            if (controller == mainController) continue;
            if (controller.getNextPosition().equals(mainController.getPosition())) {
                controller.setNextPosition(controller.getPrevPosition());
            }
        }
    }
}
