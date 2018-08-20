package com.android.game.model;

public class Level {

    private int[][] map =   {{0, 1, 1, 1, 1, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1, 0, 1, 1},
                            {1, 0, 0, 1, 1, 0, 1, 1},
                            {1, 0, 0, 0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0, 0, 0, 1},
                            {1, 1, 0, 0, 0, 0, 1, 1}};

    public Level() {
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
}
