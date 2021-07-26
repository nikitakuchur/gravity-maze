package com.majakkagames.gravitymaze.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.majakkagames.mazecore.game.Level;
import com.majakkagames.gravitymaze.game.physics.PhysicalObject;

import java.util.List;

public class LevelInputListener extends InputListener {

    private final LevelController controller;
    private final Level level;

    private boolean lock;
    private float lastAngle;
    private final Vector2 lastTouchPosition = new Vector2();

    public LevelInputListener(LevelController controller) {
        this.controller = controller;
        this.level = controller.getLevel();
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (!areObjectsGrounded() || level.onPause() || controller.isGameEnded()) {
            lock = true;
            return false;
        }

        lock = false;
        controller.zoomOut();
        lastAngle = level.getRotation();
        lastTouchPosition.set(Gdx.input.getX(), Gdx.input.getY());
        return true;
    }

    private boolean areObjectsGrounded() {
        List<PhysicalObject> physicalObjects = level.getGameObjectStore().getGameObjects(PhysicalObject.class);
        if (physicalObjects.isEmpty()) return false;
        for (PhysicalObject physicalObject : physicalObjects) {
            if (physicalObject.getPhysicalController().isMoving()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        lock = false;
        controller.zoomIn();
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (lock) return;
        Vector2 center = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        float angle = lastAngle - touchPosition.sub(center).angleDeg(lastTouchPosition.cpy().sub(center));

        float max = 600 * Gdx.graphics.getDeltaTime();
        float delta = angleDifference(level.getRotation(), angle);

        if (Math.abs(delta) > max) {
            level.setRotation(level.getRotation() + Math.signum(delta) * max);
            lastAngle = level.getRotation();
            lastTouchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            return;
        }

        level.setRotation(angle);
    }

    private float angleDifference(float alpha, float beta) {
        float diff = (beta - alpha + 180) % 360 - 180;
        return diff < -180 ? diff + 360 : diff;
    }
}
