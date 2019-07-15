package com.android.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    private boolean rotationLock;

    private float t;

    private GravityDirection lastGravityDirection = GravityDirection.BOTTOM;

    /**
     * Creates a new level
     */
    public Level() {
        this.addActor(background);
        this.addActor(map);

        // Holes
        Hole blueHole = new Hole(this);
        blueHole.setColor(Color.BLUE);
        blueHole.setPosition(9, 2);
        gameObjects.add(blueHole);

        Hole redHole = new Hole(this);
        redHole.setColor(Color.RED);
        redHole.setPosition(11, 5);
        gameObjects.add(redHole);

        // Teleports
        Teleport teleportOne = new Teleport(this);
        teleportOne.setPosition(6, 6);
        gameObjects.add(teleportOne);

        Teleport teleportTwo = new Teleport(this);
        teleportTwo.setPosition(11, 2);
        gameObjects.add(teleportTwo);

        teleportOne.to(teleportTwo);
        teleportTwo.to(teleportOne);

        // Add game objects to level group
        for (GameObject gameObject : gameObjects) {
            gameObject.setWidth(map.getWidth() / map.getCellsWidth());
            gameObject.setHeight(map.getHeight() / map.getCellsHeight());
            this.addActor(gameObject);
        }

        // Balls
        Ball blueBall = new Ball(this);
        blueBall.setColor(Color.BLUE);
        blueBall.setPosition(0, 0);
        balls.add(blueBall);

        Ball redBall = new Ball(this);
        redBall.setColor(Color.RED);
        redBall.setPosition(4, 1);
        balls.add(redBall);

        // Add balls to level group
        for (Ball ball : balls) {
            ball.setWidth(map.getWidth() / map.getCellsWidth());
            ball.setHeight(map.getHeight() / map.getCellsHeight());
            this.addActor(ball);
        }

        blueHole.addBall(blueBall);
        redHole.addBall(redBall);

        addListener(new LevelInputListener());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

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

    @Override
    public Actor hit(float x, float y, boolean touchable) {
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

        for (Ball ball : balls)
            ball.dispose();

        for (GameObject gameObject : gameObjects)
            gameObject.dispose();
    }

    public class LevelInputListener extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (pointer != 0)
                return false;

            if (!ballsAreGrounded()) {
                rotationLock = true;
                return true;
            }
            
            rotationLock = false;
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
            rotationLock = false;
            zoom = false;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (rotationLock)
                return;
            Vector2 center = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            setRotation(lastAngle + touchPosition.sub(center).angle(lastTouchPosition.cpy().sub(center)));
        }
    }

}
