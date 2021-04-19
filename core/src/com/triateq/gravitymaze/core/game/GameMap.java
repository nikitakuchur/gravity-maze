package com.triateq.gravitymaze.core.game;

import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.core.serialization.Parameters;

/**
 * The game map class, that contains a 2d-array of the cells.
 */
public abstract class GameMap<T> extends GameObject {

    protected T[][] cells;

    /**
     * Returns the cells width.
     */
    public int getCellsWidth() {
        return cells.length;
    }

    /**
     * Returns the cells height.
     */
    public int getCellsHeight() {
        return cells[0].length;
    }

    /**
     * Returns the cell size.
     */
    public float getCellSize() {
        return getWidth() / getCellsWidth();
    }

    public Vector2 getActualCoords(float x, float y) {
        return new Vector2(getX() + x * getCellSize(), getY() + y * getCellSize());
    }

    public Vector2 getActualCoords(Vector2 coords) {
        return getActualCoords(coords.x, coords.y);
    }

    /**
     * Returns the cell.
     *
     * @param x the x-component of the cell position
     * @param y the y-component of the cell position
     * @return the cell
     */
    public T getCellType(int x, int y) {
        if (isOutside(x, y)) {
            throw new IllegalArgumentException("The coordinates out of bounds");
        }
        return cells[x][y];
    }

    /**
     * Sets the type of the cell.
     *
     * @param x the x-component of the cell position
     * @param y the y-component of the cell position
     * @param type the cell type
     */
    public void setCellType(int x, int y, T type) {
        if (!isOutside(x, y)) {
            cells[x][y] = type;
        } else {
            throw new IllegalArgumentException("The coordinates out of bounds");
        }
    }

    /**
     * Returns true if the position is outside the map and false otherwise.
     */
    public boolean isOutside(int x, int y) {
        return x >= cells.length || x < 0 || y >= cells[0].length || y < 0;
    }

    @Override
    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        parameters.put("cells", cells.getClass(), cells.clone());
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        cells = parameters.getValue("cells");
    }
}
