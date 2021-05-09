package com.majakkagames.gravitymaze.core.game;

import com.badlogic.gdx.math.Vector2;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

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

    /**
     * Convert map coords to screen coords.
     *
     * @param x the x-component
     * @param y the y-component
     * @return the screen coords
     */
    public Vector2 toScreenCoords(float x, float y) {
        return new Vector2(getX() + x * getCellSize(), getY() + y * getCellSize());
    }

    /**
     * Convert map coords to screen coords.
     *
     * @param coords the map coords
     * @return the screen coords
     */
    public Vector2 toScreenCoords(Vector2 coords) {
        return toScreenCoords(coords.x, coords.y);
    }

    /**
     * Convert screen coords to map coords.
     *
     * @param x the x-component
     * @param y the y-component
     * @return the map coords
     */
    public Vector2 toMapCoords(float x, float y) {
        return new Vector2((x - getX()) / getCellSize(), (y - getY()) / getCellSize());
    }

    /**
     * Convert screen coords to map coords.
     *
     * @param coords the screen coords
     * @return the map coords
     */
    public Vector2 toMapCoords(Vector2 coords) {
        return toMapCoords(coords.x, coords.y);
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
     * @param x    the x-component of the cell position
     * @param y    the y-component of the cell position
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
    public int getLayer() {
        return 1;
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
