package com.android.game.model;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

public class Level {

    private int[][] map =  {{0, 1, 1, 1, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1, 0, 1, 1},
                            {1, 0, 0, 1, 1, 0, 1, 1},
                            {1, 0, 0, 0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0, 0, 0, 1},
                            {1, 1, 0, 0, 0, 0, 1, 1}};

    private ArrayList<Ball> balls;

    private float rotation;
    private float scale;

    public Level() {
        balls = new ArrayList<Ball>();
        balls.add(new Ball(new Vector2(1,1), Color.BLUE));
        balls.add(new Ball(new Vector2(6,6), Color.RED));

        scale = 1;
    }

    public int getWidth() {
        return map[0].length;
    }

    public int getHeight() {
        return map.length;
    }

    public int getCellId(int x, int y) {
        if ( x >= map.length || x < 0 || y >= map.length || y < 0)
            return 1;
        return map[map.length - 1 - y][x];
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }
}