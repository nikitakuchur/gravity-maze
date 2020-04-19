package com.github.nikitakuchur.puzzlegame.game.cells;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;

public abstract class Cell {

    protected enum Corner {
        BOTTOM_LEFT, BOTTOM_RIGHT, TOP_RIGHT, TOP_LEFT
    }

    protected static final int SEGMENTS = 32;
    protected static final float RADIUS_COEFFICIENT = 8;

    private final ShapeRenderer shapeRenderer;
    private final GameMap map;

    private int x;
    private int y;

    public Cell(ShapeRenderer shapeRenderer, GameMap map) {
        this.shapeRenderer = shapeRenderer;
        this.map = map;
    }

    public abstract void draw();

    protected boolean isCellEmpty(int dx, int dy) {
        return getMap().getCellType(getX() + dx, getY() + dy) == CellType.EMPTY;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public GameMap getMap() {
        return map;
    }

    public float getRadius() {
        return getWidth() /  RADIUS_COEFFICIENT;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getWidth() {
        return map.getWidth() / map.getCellsWidth();
    }

    public float getHeight() {
        return map.getHeight() / map.getCellsHeight();
    }
}
