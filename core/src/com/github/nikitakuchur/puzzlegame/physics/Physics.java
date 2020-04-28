package com.github.nikitakuchur.puzzlegame.physics;

import java.util.List;
import java.util.stream.Collectors;

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
        controllers.forEach(controller -> controller.calcNextPosition(level));
        controllers.forEach(controller -> controller.update(level, delta));
    }
}
