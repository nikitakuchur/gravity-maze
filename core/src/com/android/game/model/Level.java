package com.android.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Level {

    public enum GravityDirection {
        TOP, LEFT, BOTTOM, RIGHT
    }

    private int[][] cells =  {{0, 1, 1, 1, 1, 1, 0, 0},
                              {0, 0, 0, 0, 0, 0, 0, 1},
                              {0, 0, 0, 0, 0, 0, 0, 1},
                              {0, 0, 0, 1, 1, 0, 1, 1},
                              {1, 0, 0, 1, 1, 0, 1, 1},
                              {1, 0, 0, 0, 0, 0, 0, 0},
                              {1, 0, 0, 0, 0, 0, 0, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1}};

    private List<Ball> balls;

    private GravityDirection gravityDirection;
    private Score score;

    private float rotation;
    private float scale;

    private Color[] backgroundColor = {new Color(0.81f, 0.45f, 0.5f, 1),
            new Color(0.89f, 0.59f, 0.46f, 1),
            new Color(0.99f, 0.73f, 0.4f, 1),
            new Color(0.89f, 0.59f, 0.46f, 1)};

    private Color color = new Color(0.17f, 0.28f, 0.37f, 1);

    /**
     * Creates a new level
     */
    public Level() {
        balls = new ArrayList<Ball>();
        balls.add(new Ball(new Vector2(1, 0), Color.BLUE));
        balls.add(new Ball(new Vector2(6, 1), Color.RED));

        gravityDirection = GravityDirection.BOTTOM;
        score = new Score();

        rotation = 0;
        scale = 1;
    }

    /**
     * @return the width of the level
     */
    public int getWidth() {
        return cells.length;
    }

    /**
     * @return the height of the level
     */
    public int getHeight() {
        return cells[0].length;
    }

    /**
     * Returns the id of the cell
     *
     * @param x the x-component of the position
     * @param y the y-component of the position
     * @return the id
     */
    public int getCellId(int x, int y) {
        if (x >= cells.length || x < 0 || y >= cells[0].length || y < 0) {
            return 1;
        }
        return cells[x][y];
    }

    /**
     * @return the list of the balls
     */
    public List<Ball> getBalls() {
        return balls;
    }

    /**
     * Sets the gravity direction
     *
     * @param gravityDirection the state
     */
    public void setGravityDirection(GravityDirection gravityDirection) {
        this.gravityDirection = gravityDirection;
    }

    /**
     * @return the gravity direction
     */
    public GravityDirection getGravityDirection() {
        return gravityDirection;
    }

    /**
     * Sets the score
     *
     * @param score the score
     */
    public void setScore(Score score) {
        this.score = score;
    }

    /**
     * @return the score
     */
    public Score getScore() {
        return score;
    }

    /**
     * Sets the rotation
     *
     * @param angle the angle
     */
    public void setRotation(float angle) {
        rotation = angle;
    }

    /**
     * @return the rotation
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Sets the scale
     *
     * @param scale the scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * @return the scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Returns the background colors
     * 0 - bottom left
     * 1 - bottom right
     * 2 - top right
     * 3 - top left
     *
     * @return the array of the colors
     */
    public Color[] getBackgroundColor() {
        return new Color[]{backgroundColor[0].cpy(),
                backgroundColor[1].cpy(),
                backgroundColor[2].cpy(),
                backgroundColor[3].cpy()};
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color.cpy();
    }

    /**
     * Resets the level
     */
    public void reset() {
        score.setValue(gravityDirection == GravityDirection.BOTTOM ? 0 : -1);
        gravityDirection = GravityDirection.BOTTOM;
        rotation = 0;
    }
}
