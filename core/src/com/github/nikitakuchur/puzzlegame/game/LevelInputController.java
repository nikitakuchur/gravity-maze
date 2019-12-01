package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class LevelInputController {

    private final Level level;

    private GravityDirection gravityDirection = GravityDirection.BOTTOM;
    private GravityDirection lastGravityDirection = GravityDirection.BOTTOM;

    private float lastAngle;
    private Vector2 lastTouchPosition = new Vector2();

    private boolean zoom;
    private boolean lockRotation;

    private float t;

    public LevelInputController(Level level) {
        this.level = level;
    }

    public void act(float delta) {
        for (GameObject gameObject : level.getGameObjects()) {
            gameObject.act(level, delta);
        }

        // Zoom out
        if (zoom && t < 1) {
            t += 4 * delta;
            level.setScale(scaleAnimation(t));
        } else if (zoom) {
            level.setScale(scaleAnimation(1));
        }

        // Zoom in
        if (!zoom && t > 0) {
            t -= 4 * delta;
            level.setScale(scaleAnimation(t));
        } else if (!zoom) {
            level.setScale(scaleAnimation(0));
        }

        if (!zoom) {
            rotateToClosestEdge(delta);
        }
    }

    private void rotateToClosestEdge(float delta) {
        final float speed = 800;
        float angle = level.getRotation();
        float angleRad = (float) Math.toRadians(angle);

        if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad))) {
            if (Math.cos(angleRad) < 0) {
                level.setRotation(angle + (float) Math.sin(angleRad) * speed * delta);
                gravityDirection = GravityDirection.TOP;
            } else {
                level.setRotation(angle - (float) Math.sin(angleRad) * speed * delta);
                gravityDirection = GravityDirection.BOTTOM;
            }
        }

        if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad))) {
            if (Math.sin(angleRad) > 0) {
                level.setRotation(angle + (float) Math.cos(angleRad) * speed * delta);
                gravityDirection = GravityDirection.LEFT;
            } else {
                level.setRotation(angle - (float) Math.cos(angleRad) * speed * delta);
                gravityDirection = GravityDirection.RIGHT;
            }
        }

        if (lastGravityDirection != gravityDirection) {
            lastGravityDirection = gravityDirection;
            level.setScore(level.getScore() + 1);
        }

        lastAngle = level.getRotation();
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }

    public GravityDirection getGravityDirection() {
        return gravityDirection;
    }

    public InputListener getInputListener() {
        return new LevelInputListener();
    }

    private class LevelInputListener extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (level.getPause() || pointer != 0) return false;

            if (!areBallsGrounded()) {
                lockRotation = true;
                return true;
            }

            lockRotation = false;
            zoom = true;
            lastTouchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            return true;
        }

        private boolean areBallsGrounded() {
            for (Ball ball : level.getGameObjects(Ball.class)) {
                if (!ball.isGrounded()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            lockRotation = false;
            zoom = false;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (lockRotation) return;
            Vector2 center = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());

            float angle = lastAngle + touchPosition.sub(center).angle(lastTouchPosition.cpy().sub(center));

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
}
