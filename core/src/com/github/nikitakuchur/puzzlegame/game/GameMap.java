package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.game.cells.Block;
import com.github.nikitakuchur.puzzlegame.game.cells.Cell;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.cells.EmptyCell;

public class Map extends Actor implements Disposable {

    private CellType[][] cells;

    private static final Color CELLS_COLOR = new Color(0.01f, 0.31f, 0.45f, 1);

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Cell block;
    private Cell emptyCell;

    /**
     * Creates a new map
     */
    public Map(CellType[][] cells) {
        this.cells = cells;
        setColor(CELLS_COLOR);

        block = new Block(shapeRenderer, this);
        emptyCell = new EmptyCell(shapeRenderer, this);
    }

    @Override
    public void act(float delta) {
        getParent().setWidth(getWidth());
        getParent().setHeight(getHeight());
        super.act(delta);
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
     * Draws the cells of the level
     */
    private void drawCells() {
        for (int i = 0; i < getCellsWidth() ; i++) {
            for (int j = 0; j < getCellsHeight(); j++) {
                if (getCellType(i, j) == CellType.BLOCK) {
                    block.setX(i);
                    block.setY(j);
                    block.draw();
                } else {
                    emptyCell.setX(i);
                    emptyCell.setY(j);
                    emptyCell.draw();
                }
            }
        }
    }

    /**
     * @return the cells width
     */
    public int getCellsWidth() {
        return cells.length;
    }

    /**
     * @return the cells height
     */
    public int getCellsHeight() {
        return cells[0].length;
    }

    /**
     * Returns the type of the cell
     *
     * @param x the x-component of the cell position
     * @param y the y-component of the cell position
     * @return the cell type
     */
    public CellType getCellType(int x, int y) {
        if (x >= cells.length || x < 0 || y >= cells[0].length || y < 0) {
            return CellType.BLOCK;
        }
        return cells[x][y];
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}