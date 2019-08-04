package com.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class Level extends Group implements Disposable {

    public enum GravityDirection {
        TOP, LEFT, BOTTOM, RIGHT
    }

    private Background background = new Background();
    private Map map = new Map();
    private List<GameObject> gameObjects = new ArrayList<>();
    private List<Ball> balls = new ArrayList<>();

    private int score;

    private GravityDirection gravityDirection = GravityDirection.BOTTOM;

    private float lastAngle;
    private Vector2 lastTouchPosition = new Vector2();

    private boolean zoom;
    private boolean lockRotation;

    private float t;

    private GravityDirection lastGravityDirection = GravityDirection.BOTTOM;

    private boolean pause;
    private boolean fillScreen;

    /**
     * Creates a new level
     */
    public Level() {
        this.addActor(background);
        map.setWidth(100);
        map.setHeight(map.getWidth() / map.getCellsWidth() * map.getCellsHeight());
        this.addActor(map);

        // Holes
        Hole blueHole = new Hole();
        blueHole.setColor(0.14f, 0.35f, 0.76f, 1);
        blueHole.setPosition(9, 2);
        gameObjects.add(blueHole);

        Hole pinkHole = new Hole();
        pinkHole.setColor(0.86f, 0.34f, 0.68f, 1);
        pinkHole.setPosition(11, 5);
        gameObjects.add(pinkHole);

        // Portals
        Portal portalOne = new Portal();
        portalOne.setPosition(6, 6);
        gameObjects.add(portalOne);

        Portal portalTwo = new Portal();
        portalTwo.setPosition(11, 2);
        gameObjects.add(portalTwo);

        portalOne.to(portalTwo);
        portalTwo.to(portalOne);

        // Add game objects to level group
        for (Actor gameObject : gameObjects) {
            this.addActor(gameObject);
        }

        // Balls
        Ball blueBall = new Ball();
        blueBall.setColor(blueHole.getColor());
        blueBall.setPosition(0, 0);
        balls.add(blueBall);

        Ball pinkBall = new Ball();
        pinkBall.setColor(pinkHole.getColor());
        pinkBall.setPosition(4, 1);
        balls.add(pinkBall);

        // Add balls to level group
        for (Ball ball : balls) {
            this.addActor(ball);
        }

        blueHole.addBall(blueBall);
        pinkHole.addBall(pinkBall);

        addListener(new LevelInputListener());
    }

    @Override
    public void act(float delta) {
        if (fillScreen) {
            map.setWidth(Gdx.graphics.getWidth());
            map.setHeight(Gdx.graphics.getWidth() / map.getCellsWidth() * map.getCellsHeight());
        }
        super.act(delta);
        for (Ball ball : balls) {
            ball.act(this, delta);
        }
        for (GameObject gameObject : gameObjects) {
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

        // Rotate to closest edge
        if (!zoom) {
            float angle = getRotation();
            float angleRad = (float) Math.toRadians(angle);
            float speed = 800;

            // Top
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) < 0) {
                setRotation(angle + (float) Math.sin(angleRad) * speed * delta);
                gravityDirection = GravityDirection.TOP;
            }

            // Left
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) > 0) {
                setRotation(angle + (float) Math.cos(angleRad) * speed * delta);
                gravityDirection = GravityDirection.LEFT;
            }

            // Bottom
            if (Math.abs(Math.cos(angleRad)) >= Math.abs(Math.sin(angleRad)) && Math.cos(angleRad) > 0) {
                setRotation(angle - (float) Math.sin(angleRad) * speed * delta);
                gravityDirection = GravityDirection.BOTTOM;
            }

            // Right
            if (Math.abs(Math.sin(angleRad)) >= Math.abs(Math.cos(angleRad)) && Math.sin(angleRad) < 0) {
                setRotation(angle - (float) Math.cos(angleRad) * speed * delta);
                gravityDirection = GravityDirection.RIGHT;
            }

            if (lastGravityDirection != gravityDirection) {
                lastGravityDirection = gravityDirection;
                score++;
            }

            lastAngle = getRotation();
        }
    }

    private float scaleAnimation(float t) {
        return 0.294f * ((float) Math.cos((float) Math.PI * t) - 1) / 2 + 1;
    }

    /**
     * Pauses the level
     *
     * @param pause
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void fillScreen(boolean fillScreen) {
        this.fillScreen = fillScreen;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (pause)
            return null;
        return this;
    }

    /**
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    /**
     * @return the list of game objects
     */
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * Removes the game object from the level
     *
     * @param gameObject the game object
     */
    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
        removeActor(gameObject);
        gameObject.dispose();
    }

    /**
     * @return the list of the balls
     */
    public List<Ball> getBalls() {
        return balls;
    }

    /**
     * Removes the ball from the level
     *
     * @param ball the ball
     */
    public void removeBall(Ball ball) {
        balls.remove(ball);
        removeActor(ball);
        ball.dispose();
    }

    /**
     * @return the score
     */
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

        for (Ball ball : balls) {
            ball.dispose();
        }
        for (GameObject gameObject : gameObjects) {
            gameObject.dispose();
        }
    }

    private class LevelInputListener extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (pointer != 0)
                return false;

            if (!ballsAreGrounded()) {
                lockRotation = true;
                return true;
            }

            lockRotation = false;
            zoom = true;
            lastTouchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            return true;
        }

        private boolean ballsAreGrounded() {
            for (Ball ball : balls) {
                if (!ball.isGrounded())
                    return false;
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
            if (lockRotation)
                return;
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
