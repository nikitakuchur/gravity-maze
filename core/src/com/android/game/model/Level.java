package com.android.game.model;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

public class Level {

    private int[][] cells =  {{0, 1, 1, 1, 1, 1, 0, 0},
                              {0, 0, 0, 0, 0, 0, 0, 1},
                              {0, 0, 0, 0, 0, 0, 0, 1},
                              {1, 0, 0, 1, 1, 0, 1, 1},
                              {1, 0, 0, 1, 1, 0, 1, 1},
                              {1, 0, 0, 0, 0, 0, 0, 0},
                              {1, 0, 0, 0, 0, 0, 0, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1}};

    private ArrayList<Ball> balls;

    private float rotation;
    private float scale;

    private Color[] backgroundColor = { new Color(0.81f, 0.45f, 0.5f, 1),
                                        new Color(0.89f, 0.59f, 0.46f, 1),
                                        new Color(0.99f, 0.73f, 0.4f, 1),
                                        new Color(0.89f, 0.59f, 0.46f, 1)};
    private Color color = new Color(0.19f, 0.29f, 0.37f, 1);

    public Level() {
        balls = new ArrayList<Ball>();
        balls.add(new Ball(new Vector2(1,1), Color.BLUE));
        balls.add(new Ball(new Vector2(6,6), Color.RED));

        scale = 1;
    }

    public int getWidth() {
        return cells.length;
    }

    public int getHeight() {
        return cells[0].length;
    }

    public int getCellId(int x, int y) {
        if ( x >= cells.length || x < 0 || y >= cells[0].length || y < 0)
            return 1;
        return cells[x][y];
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

    public Color[] getBackgroundColor() {
        return new Color[]{backgroundColor[0].cpy(),
                           backgroundColor[1].cpy(),
                           backgroundColor[2].cpy(),
                           backgroundColor[3].cpy()};
    }

    public Color getColor() {
        return color.cpy();
    }
}