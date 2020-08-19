package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.game.cells.BlockRenderer;
import com.github.nikitakuchur.puzzlegame.game.cells.CellRenderer;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.cells.EmptyCellRenderer;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

import java.util.Arrays;

/**
 * The game map class, that contains a 2d-array of the cells.
 * The cell can be a filled block or an empty block.
 */
public class GameMap extends Actor implements Parameterizable, Disposable {

    private CellType[][] cells;

    private static final Color CELLS_COLOR = Color.valueOf("#024f72");

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final CellRenderer block = new BlockRenderer(shapeRenderer, this);
    private final CellRenderer emptyCellRenderer = new EmptyCellRenderer(shapeRenderer, this);

    public GameMap() {
        this(8, 8);
    }

    /**
     * Creates a new map.
     */
    public GameMap(CellType[][] cells) {
        this.cells = cells;
        setColor(CELLS_COLOR);
    }

    /**
     * Creates a new empty map.
     */
    public GameMap(int width, int height) {
        cells = new CellType[width][height];
        for (CellType[] row: cells) {
            Arrays.fill(row, CellType.EMPTY);
        }
        setColor(CELLS_COLOR);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.translate(-getWidth() / 2, -getHeight() / 2, 0);
        shapeRenderer.setColor(getColor());
        drawBorders();
        drawCells();
        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    private void drawBorders() {
        float max = Math.max(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
        shapeRenderer.rect(getX(), getY(), -max, getHeight()); // Left
        shapeRenderer.rect(getX() + getWidth() , getY(), max, getHeight()); // Right
        shapeRenderer.rect(getX() - max, getY() + getHeight(), max * 2 + getWidth(), max); // Top
        shapeRenderer.rect(getX() - max, getY(), max * 2 + getWidth(), -max); // Bottom
    }

    /**
     * Draws the cells of the level.
     */
    private void drawCells() {
        for (int i = 0; i < getCellsWidth() ; i++) {
            for (int j = 0; j < getCellsHeight(); j++) {
                if (getCellType(i, j) == CellType.FILLED) {
                    block.setX(i);
                    block.setY(j);
                    block.draw();
                } else {
                    emptyCellRenderer.setX(i);
                    emptyCellRenderer.setY(j);
                    emptyCellRenderer.draw();
                }
            }
        }
    }

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
     * Returns the type of the cell.
     *
     * @param x the x-component of the cell position
     * @param y the y-component of the cell position
     * @return the cell type
     */
    public CellType getCellType(int x, int y) {
        if (x >= cells.length || x < 0 || y >= cells[0].length || y < 0) {
            return CellType.FILLED;
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
    public void setCellType(int x, int y, CellType type) {
        if (x >= 0 && x < cells.length && y >= 0 && y < cells[0].length) {
             cells[x][y] = type;
        }
    }

    /**
     * Returns true if the cell is a filled block and false otherwise.
     */
    public boolean isFilled(int x, int y) {
        return getCellType(x, y) == CellType.FILLED;
    }

    /**
     * Returns true if the cell is an empty block and false otherwise.
     */
    public boolean isEmpty(int x, int y) {
        return getCellType(x, y) == CellType.EMPTY;
    }

    @Override
    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        parameters.put("color", Color.class, getColor());
        parameters.put("cells", CellType[][].class, cells);
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        setColor(parameters.getValue("color"));
        cells = parameters.getValue("cells");
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
