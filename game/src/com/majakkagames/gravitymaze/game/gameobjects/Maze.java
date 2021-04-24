package com.majakkagames.gravitymaze.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.majakkagames.gravitymaze.core.game.GameMap;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.game.cells.FilledCellRenderer;
import com.majakkagames.gravitymaze.game.cells.CellRenderer;
import com.majakkagames.gravitymaze.game.cells.CellType;
import com.majakkagames.gravitymaze.game.cells.EmptyCellRenderer;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

import java.util.Arrays;

/**
 * The maze map class, that contains a 2d-array of the cells.
 * The cell can be a filled block or an empty block.
 */
public class Maze extends GameMap<CellType> implements Disposable {

    private static final Color DEFAULT_COLOR = Color.valueOf("#024f72");

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final CellRenderer block = new FilledCellRenderer(shapeRenderer, this);
    private final CellRenderer emptyCellRenderer = new EmptyCellRenderer(shapeRenderer, this);

    public Maze() {
        this(8, 8);
    }

    /**
     * Creates a new map.
     */
    public Maze(CellType[][] cells) {
        this.cells = cells;
        setColor(DEFAULT_COLOR);
    }

    /**
     * Creates a new empty map.
     */
    public Maze(int width, int height) {
        cells = new CellType[width][height];
        for (CellType[] row : cells) {
            Arrays.fill(row, CellType.EMPTY);
        }
        setColor(DEFAULT_COLOR);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        updateSize();
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setColor(getColor());
        drawBorders();
        drawCells();
        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    private void updateSize() {
        float w = level.getWidth();
        if (w > level.getHeight()) {
            w = level.getHeight();
        }
        setWidth(w);
        setHeight(w / getCellsWidth() * getCellsHeight());
        setX(-getWidth() / 2);
        setY(-getHeight() / 2);
    }

    private void drawBorders() {
        float max = Math.max(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
        float offset = 0.1f; // We need this offset to avoid graphical artifacts without MSAA
        shapeRenderer.rect(getX(), getY() - offset, -max, getHeight() + 2 * offset); // Left
        shapeRenderer.rect(getX() + getWidth(), getY() - offset, max, getHeight() + 2 * offset); // Right
        shapeRenderer.rect(getX() - max, getY() + getHeight(), max * 2 + getWidth(), max); // Top
        shapeRenderer.rect(getX() - max, getY(), max * 2 + getWidth(), -max); // Bottom
    }

    /**
     * Draws the cells of the level.
     */
    private void drawCells() {
        for (int i = 0; i < getCellsWidth(); i++) {
            for (int j = 0; j < getCellsHeight(); j++) {
                if (isFilled(i, j)) {
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

    @Override
    public CellType getCellType(int x, int y) {
        if (isOutside(x, y)) {
            return CellType.FILLED;
        }
        return super.getCellType(x, y);
    }

    @Override
    public void setCellType(int x, int y, CellType type) {
        if (isOutside(x, y)) return;
        super.setCellType(x, y, type);
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
        Parameters parameters = super.getParameters();
        parameters.put("color", Color.class, getColor().cpy());
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        setColor(parameters.getValue("color"));
        super.setParameters(parameters);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
