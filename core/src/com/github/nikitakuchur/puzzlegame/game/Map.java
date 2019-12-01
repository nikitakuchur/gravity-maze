package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Map extends Actor implements Disposable {

    private CellType[][] cells;

    private static final Color CELLS_COLOR = new Color(0.01f, 0.31f, 0.45f, 1);

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Creates a new map
     */
    public Map(CellType[][] cells) {
        this.cells = cells;
        setColor(CELLS_COLOR);
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
        drawRects();
        drawCells();
        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    private void drawRects() {
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
        int w = getCellsWidth();
        int h = getCellsHeight();

        float cellWidth = getWidth() / w;
        float celHeight = getHeight() / h;

        float radius = getWidth() / (8 * w);

        // Draw cells
        for (int i = 0; i < w ; i++) {
            for (int j = 0; j < h; j++) {
                if (getCellType(i, j) == CellType.BLOCK) {
                    boolean[] cornersInfo = resolveCornersInfo(i, j, false);

                    roundedRect(new Vector2(getX() + i * cellWidth, getY() + j * celHeight),
                            new Vector2(cellWidth, celHeight), radius, cornersInfo);
                } else {
                    boolean[] cornersInfo = resolveCornersInfo(i, j, true);

                    emptyRect(new Vector2(getX() + i * cellWidth, getY() + j * celHeight),
                            new Vector2(cellWidth, celHeight), radius, cornersInfo);
                }
            }
        }
    }

    /**
     * Returns an array that contains information whether
     * you need to draw rounded corners in the given cell.
     *
     * @param x the x-component of the position
     * @param y the y-component of the position
     * @param inside is it an inside corner or outside?
     * @return the array of booleans
     */
    private boolean[] resolveCornersInfo(int x, int y, boolean inside) {
        boolean[] cornersInfo = new boolean[4];

        CellType b = inside ? CellType.BLOCK : CellType.EMPTY;

        cornersInfo[0] = getCellType(x - 1, y) == b && getCellType(x, y - 1) == b;
        cornersInfo[1] = getCellType(x + 1, y) == b && getCellType(x, y - 1) == b;
        cornersInfo[2] = getCellType(x + 1, y) == b && getCellType(x, y + 1) == b;
        cornersInfo[3] = getCellType(x - 1, y) == b && getCellType(x, y + 1) == b;

        if (!inside) {
            cornersInfo[0] = cornersInfo[0] && getCellType(x - 1, y - 1) == b;
            cornersInfo[1] = cornersInfo[1] &&  getCellType(x + 1, y - 1) == b;
            cornersInfo[2] = cornersInfo[2] && getCellType(x + 1, y + 1) == b;
            cornersInfo[3] = cornersInfo[3] && getCellType(x - 1, y + 1) == b;
        }

        return cornersInfo;
    }

    /**
     * Draws a rounded rectangle
     *
     * @param position the position
     * @param size the size
     * @param radius the radius of the corner
     * @param isRounded if the corner is rounded, then isRounded[cornerIndex] = true
     */
    private void roundedRect(Vector2 position, Vector2 size, float radius, boolean[] isRounded) {
        final int segments = 32;

        if (size.x >= size.y && radius > size.y / 2) {
            radius = size.y / 2;
        } else if (size.x < size.y && radius > size.x / 2) {
            radius = size.x / 2;
        }

        shapeRenderer.rect(position.x + radius, position.y + radius,
                size.x - 2 * radius, size.y - 2 * radius);

        shapeRenderer.rect(position.x, position.y + radius, radius, size.y - 2 * radius);
        shapeRenderer.rect(position.x + radius, position.y + size.y - radius, size.x - 2 * radius, radius);
        shapeRenderer.rect(position.x + size.x - radius, position.y + radius, radius, size.y - 2 * radius);
        shapeRenderer.rect(position.x + radius, position.y, size.x - 2 * radius, radius);

        if (isRounded[0]) {
            shapeRenderer.arc(position.x + radius, position.y + radius, radius, -180, 90, segments);
        } else {
            shapeRenderer.rect(position.x, position.y, radius, radius);
        }

        if (isRounded[1]) {
            shapeRenderer.arc(position.x + size.x - radius, position.y + radius, radius, -90, 90, segments);
        } else {
            shapeRenderer.rect(position.x + size.x - radius, position.y, radius, radius);
        }

        if (isRounded[2]) {
            shapeRenderer.arc(position.x + size.x - radius, position.y + size.y - radius, radius, 0, 90, segments);
        } else {
            shapeRenderer.rect(position.x + size.x - radius, position.y + size.y - radius, radius, radius);
        }

        if (isRounded[3]) {
            shapeRenderer.arc(position.x + radius, position.y + size.y - radius, radius, 90, 90, segments);
        } else {
            shapeRenderer.rect(position.x, position.y + size.y - radius, radius, radius);
        }
    }

    /**
     * Draws an empty rectangle
     *
     * @param position the position
     * @param size the size
     * @param radius the radius of the corner
     * @param isRounded if the corner is rounded, then isRounded[cornerIndex] = true
     */
    private void emptyRect(Vector2 position, Vector2 size, float radius, boolean[] isRounded) {
        if (isRounded[0]) {
            roundedInsideCorner(new Vector2(position.x, position.y), radius, 0);
        }
        if (isRounded[1]) {
            roundedInsideCorner(new Vector2(position.x + size.x, position.y), radius, 1);
        }
        if (isRounded[2]) {
            roundedInsideCorner(new Vector2(position.x + size.x, position.y + size.y), radius, 2);
        }
        if (isRounded[3]) {
            roundedInsideCorner(new Vector2(position.x, position.y + size.y), radius, 3);
        }
    }

    /**
     * Draws a rounded inside corner
     *
     * @param position the position
     * @param radius the radius of the corner
     * @param cornerNumber the number of the corner
     */
    private void roundedInsideCorner(Vector2 position, float radius, int cornerNumber) {
        final int segments = 32;

        float[] vertices = new float[(segments + 1) * 2];

        float t = cornerNumber * (float) Math.PI / 2;

        for(int i = 0; i <= segments * 2; i += 2, t += Math.PI / (2 * segments)) {
            vertices[i] = position.x - radius * (float) Math.cos(t);
            vertices[i + 1] = position.y - radius * (float) Math.sin(t);

            if (cornerNumber == 0 || cornerNumber == 3) {
                vertices[i] += radius;
            } else {
                vertices[i] -= radius;
            }

            if (cornerNumber == 0 || cornerNumber == 1) {
                vertices[i + 1] += radius;
            } else {
                vertices[i + 1] -= radius;
            }
        }

        // Draw triangles
        for (int i = 0; i < vertices.length - 2; i += 2) {
            shapeRenderer.triangle(vertices[i], vertices[i + 1], vertices[i + 2], vertices[i + 3], position.x, position.y);
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

    /**
     * Sets the type for the given cell
     *
     * @param x the x-component of the cell position
     * @param y the y-component of the cell position
     * @param type the cell type
     */
    public void setCellType(int x, int y, CellType type) {
        if (x >= cells.length || x < 0 || y >= cells[0].length || y < 0) {
            return;
        }
        cells[x][y] = type;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}