package com.android.game.model;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

public class Level {

    private int[][] map =   {{0, 1, 1, 1, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1, 0, 1, 1},
                            {1, 0, 0, 1, 1, 0, 1, 1},
                            {1, 0, 0, 0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0, 0, 0, 1},
                            {1, 1, 0, 0, 0, 0, 1, 1}};

    private ArrayList<Ball> balls;

    public Level() {
        balls = new ArrayList<Ball>();
        balls.add(new Ball(new Vector2(1,1), Color.BLUE));
        balls.add(new Ball(new Vector2(6,6), Color.RED));
    }

    public int getWidth() {
        return map[0].length;
    }

    public int getHeight() {
        return map.length;
    }

    public int getCellId(int x, int y) {
        return map[map.length - 1 - x][y];
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }
}
