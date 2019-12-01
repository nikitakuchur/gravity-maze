package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level extends Group implements Disposable {

    public enum GravityDirection {
        TOP, LEFT, BOTTOM, RIGHT
    }

    private final Background background;
    private final Map map;

    private int score;

    private GravityDirection gravityDirection = GravityDirection.BOTTOM;

    private float lastAngle;
    private Vector2 lastTouchPosition = new Vector2();

    private boolean zoom;
    private boolean lockRotation;

    private float t;

    private GravityDirection lastGravityDirection = GravityDirection.BOTTOM;

    private boolean pause;

    private Level(Background background, Map map, List<GameObject> gameObjects) {
        this.background = background;
        this.addActor(background);

        this.map = map;
        map.setWidth(100);
        map.setHeight(map.getWidth() / map.getCellsWidth() * map.getCellsHeight());
        this.addActor(map);

        gameObjects.forEach(this::addActor);

        addListener(new LevelInputListener());
    }

    @Override
    public void act(float delta) {
        map.setWidth(Gdx.graphics.getWidth());
        map.setHeight(Gdx.graphics.getWidth() / (float) map.getCellsWidth() * map.getCellsHeight());

        super.act(delta);

        for (GameObject gameObject : getGameObjects()) {
            gameObject.act(this, delta);
        }

        // Zoom out
        if (zoom && t < 1) {
            t += 4 * delta;
            setScale(scaleAnimation(t));
        } else if (zoom) {
            setScale(scaleAnimation(1));
        }

        // Zoom in
        if (!zoom && t > 0) {
            t -= 4 * delta;
            setScale(scaleAnimation(t));
        } else if (!zoom) {
            setScale(scaleAnimation(0));
        }

        if (!zoom) {
            rotateToClosestEdge(delta);
        }
    }

    private void rotateToClosestEdge(float delta) {
        final float speed = 800;

        float angle = getRotation();
        float angleRad = (float) Math.toRadians(angle);

        if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad))) {
            if (Math.cos(angleRad) < 0) {
                setRotation(angle + (float) Math.sin(angleRad) * speed * delta);
                gravityDirection = GravityDirection.TOP;
            } else {
                setRotation(angle - (float) Math.sin(angleRad) * speed * delta);
                gravityDirection = GravityDirection.BOTTOM;
            }
        }

        if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad))) {
            if (Math.sin(angleRad) > 0) {
                setRotation(angle + (float) Math.cos(angleRad) * speed * delta);
                gravityDirection = GravityDirection.LEFT;
            } else {
                setRotation(angle - (float) Math.cos(angleRad) * speed * delta);
                gravityDirection = GravityDirection.RIGHT;
            }
        }

        if (lastGravityDirection != gravityDirection) {
            lastGravityDirection = gravityDirection;
            score++;
        }

        lastAngle = getRotation();
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isPaused() {
        return pause;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return this;
    }

    public Map getMap() {
        return map;
    }

    public List<GameObject> getGameObjects() {
        return getGameObjects(GameObject.class);
    }

    public <T extends GameObject> List<T> getGameObjects(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Actor actor : getChildren()) {
            T gameObject = actor.firstAscendant(type);
            if (gameObject != null) {
                result.add(gameObject);
            }
        }
        return result;
    }

    public int getScore() {
        return score;
    }

    /**
     * @return the gravity direction of the level
     */
    public GravityDirection getGravityDirection() {
        return gravityDirection;
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();

        for (GameObject gameObject : getGameObjects(GameObject.class)) {
            gameObject.dispose();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Background background;
        private Map map;
        private List<GameObject> gameObjects = new ArrayList<>();

        public Builder background(Background background) {
            this.background = background;
            return this;
        }

        public Builder map(Map map) {
            this.map = map;
            return this;
        }

        public Builder addGameObject(GameObject gameObject) {
            gameObjects.add(gameObject);
            return this;
        }

        public Builder addGameObjects(GameObject... gameObjects) {
            this.gameObjects.addAll(Arrays.asList(gameObjects));
            return this;
        }

        public Level build() {
            return new Level(background, map, gameObjects);
        }
    }

    private class LevelInputListener extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (pause || pointer != 0) return false;

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
            for (Ball ball : getGameObjects(Ball.class)) {
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
            float delta = angleDifference(getRotation(), angle);

            if (Math.abs(delta) > max) {
                setRotation(getRotation() + Math.signum(delta) * max);
                lastAngle = getRotation();
                lastTouchPosition.set(Gdx.input.getX(), Gdx.input.getY());
                return;
            }

            setRotation(angle);
        }

        private float angleDifference(float alpha, float beta) {
            float diff = (beta - alpha + 180) % 360 - 180;
            return diff < -180 ? diff + 360 : diff;
        }
    }
}
