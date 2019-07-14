package com.android.game.groups;

import com.android.game.actors.Background;
import com.android.game.actors.Ball;
import com.android.game.actors.Hole;
import com.android.game.actors.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;

import java.util.ArrayList;
import java.util.List;

public class Level extends Group {

    public enum GravityDirection {
        TOP, LEFT, BOTTOM, RIGHT
    }

    private Background background = new Background();
    private Map map = new Map();
    private List<Hole> holes = new ArrayList<>();
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

        Hole blueHole = new Hole(this);
        blueHole.setColor(Color.BLUE);
        blueHole.setPosition(9, 2);
        holes.add(blueHole);

        for (Hole hole : holes) {
            hole.setWidth(map.getWidth() / map.getCellsWidth());
            hole.setHeight(map.getHeight() / map.getCellsHeight());
            this.addActor(hole);
        }

        Ball blueBall = new Ball(this);
        blueBall.setColor(Color.BLUE);
        blueBall.setPosition(0, 0);
        balls.add(blueBall);

        Ball redBall = new Ball(this);
        redBall.setColor(Color.RED);
        redBall.setPosition(4, 1);
        balls.add(redBall);

        for (Ball ball : balls) {
            ball.setWidth(map.getWidth() / map.getCellsWidth());
            ball.setHeight(map.getHeight() / map.getCellsHeight());
            this.addActor(ball);
        }

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
     * @return the list of the balls
     */
    public List<Ball> getBalls() {
        return balls;
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

    /**
     * Releases all resources of this object
     */
    public void dispose() {
        background.dispose();
        map.dispose();
        for (Ball ball : balls)
            ball.dispose();
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
