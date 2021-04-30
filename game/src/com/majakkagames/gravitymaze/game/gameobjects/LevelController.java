package com.majakkagames.gravitymaze.game.gameobjects;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.majakkagames.gravitymaze.core.events.Event;
import com.majakkagames.gravitymaze.core.events.EventHandler;
import com.majakkagames.gravitymaze.core.events.EventHandlerManager;
import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.core.serialization.annotations.Transient;
import com.majakkagames.gravitymaze.game.gameobjects.mazeobjects.Ball;
import com.majakkagames.gravitymaze.game.physics.PhysicalController;
import com.majakkagames.gravitymaze.game.physics.Physics;
import com.majakkagames.gravitymaze.game.utils.Direction;

@Transient
public class LevelController extends GameObject {

    private Level level;

    private GameObjectStore store;
    private Gravity gravity;
    private LevelProperties properties;

    private Direction lastGravityDirection = Direction.BOTTOM;

    private boolean zoomOut;

    private float t;

    private boolean gameEnded;

    private final EventHandlerManager<EventType, LevelEvent> eventHandlerManager = new EventHandlerManager<>();

    @Override
    public void initialize(Level level) {
        this.level = level;
        level.addListener(new LevelInputListener(this));

        store = level.getGameObjectStore();
        gravity = store.getAnyGameObject(Gravity.class);
        properties = store.getAnyGameObject(LevelProperties.class);

        Physics physics = store.getAnyGameObject(Physics.class);
        float indent = (level.getWidth() + level.getHeight()) / 120.f;
        physics.addEventHandler(Physics.EventType.COLLISION_DETECTED, event -> {
            PhysicalController<?> controller = event.getPhysicalController();
            level.addAction(Actions.moveTo(0, -indent * controller.getVelocity().len(), 0.05f));
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Zoom out
        if (zoomOut && t < 1) {
            t += 4 * delta;
            level.setScale(scaleAnimation(t));
        } else if (zoomOut) {
            level.setScale(scaleAnimation(1));
        }

        // Zoom in
        if (!zoomOut && t > 0) {
            t -= 4 * delta;
            level.setScale(scaleAnimation(t));
        } else if (!zoomOut) {
            level.setScale(scaleAnimation(0));
        }

        if (!zoomOut) {
            rotateToClosestEdge(delta);
        }

        // Back to the center
        if (level.getY() < 0) {
            float speed = (level.getWidth() + level.getHeight()) / 15.f;
            level.setPosition(0, level.getY() + speed * delta);
        } else {
            level.setY(0);
        }

        if (!gameEnded && store.getGameObjects(Ball.class).isEmpty()) {
            endGame(false);
        }
    }

    public void zoomOut() {
        zoomOut = true;
    }

    public void zoomIn() {
        zoomOut = false;
    }

    private void rotateToClosestEdge(float delta) {
        final float speed = 800;
        float angle = level.getRotation();
        float angleRad = (float) Math.toRadians(angle);

        if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad))) {
            if (Math.cos(angleRad) < 0) {
                level.setRotation(angle + (float) Math.sin(angleRad) * speed * delta);
                gravity.setGravityDirection(Direction.TOP);
            } else {
                level.setRotation(angle - (float) Math.sin(angleRad) * speed * delta);
                gravity.setGravityDirection(Direction.BOTTOM);
            }
        }

        if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad))) {
            if (Math.sin(angleRad) > 0) {
                level.setRotation(angle + (float) Math.cos(angleRad) * speed * delta);
                gravity.setGravityDirection(Direction.LEFT);
            } else {
                level.setRotation(angle - (float) Math.cos(angleRad) * speed * delta);
                gravity.setGravityDirection(Direction.RIGHT);
            }
        }

        if (lastGravityDirection != gravity.getGravityDirection()) {
            lastGravityDirection = gravity.getGravityDirection();
            properties.setMoves(properties.getMoves() + 1);
        }
    }

    public void endGame(boolean failed) {
        gameEnded = true;
        LevelEvent event = new LevelEvent(level);
        eventHandlerManager.fire(failed ? EventType.FAILED : EventType.PASSED, event);
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public Level getLevel() {
        return level;
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }

    public void addEventHandler(EventType type, EventHandler<LevelEvent> handler) {
        eventHandlerManager.add(type, handler);
    }


    public enum EventType {
        PASSED, FAILED
    }

    public static class LevelEvent implements Event {

        private final Level level;

        public LevelEvent(Level level) {
            this.level = level;
        }

        public Level getLevel() {
            return level;
        }
    }
}
