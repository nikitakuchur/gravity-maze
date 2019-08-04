package com.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Map extends Actor implements Disposable {

    private int[][] cells =  {{0, 1, 1, 1, 1, 1, 0, 0},
                              {0, 0, 0, 0, 0, 0, 0, 1},
                              {0, 0, 0, 0, 0, 0, 0, 1},
                              {0, 0, 0, 1, 1, 0, 0, 1},
                              {1, 0, 0, 1, 1, 0, 0, 1},
                              {1, 0, 0, 0, 0, 0, 0, 0},
                              {1, 0, 0, 0, 0, 0, 0, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1},
                              {1, 1, 0, 0, 0, 0, 1, 1}};

    public static final Color COLOR = new Color(0.01f, 0.31f, 0.45f, 1);

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Creates a new map
     */
    public Map() {
        setColor(COLOR);
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

        // Draw the level
        drawCells();

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    /**
     * Draws the cells of the level
     */
    private void drawCells() {
        shapeRenderer.setColor(getColor());

        int w = getCellsWidth();
        int h = getCellsHeight();

        float cellWidth = getWidth() / w;
        float celHeight = getHeight() / h;

        float max = Math.max(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());

        // Left
        shapeRenderer.rect(getX(), getY(), -max, getHeight());

        // Right
        shapeRenderer.rect(getX() + getWidth() , getY(), max, getHeight());

        // Top
        shapeRenderer.rect(getX() - max, getY() + getHeight(), max * 2 + getWidth(), max);

        // Bottom
        shapeRenderer.rect(getX() - max, getY(), max * 2 + getWidth(), -max);


        float radius = getWidth() / (8 * w);

        // Draw cells
        for (int i = 0; i < w ; i++) {
            for (int j = 0; j < h; j++) {
                if (getCellId(i, j) == 1) {
                    boolean[] cornersInfo = getCornersInfo(i, j, false);

                    roundedRect(new Vector2(getX() + i * cellWidth, getY() + j * celHeight),
                            new Vector2(cellWidth, celHeight), radius, cornersInfo);
                } else {
                    boolean[] cornersInfo = getCornersInfo(i, j, true);

                    if (cornersInfo[0])
                        roundedInsideCorner(new Vector2(getX() + i * cellWidth, getY() + j * celHeight),
                                radius, 0);
                    if (cornersInfo[1])
                        roundedInsideCorner(new Vector2(getX() + (i + 1) * cellWidth, getY() + j * celHeight),
                                radius, 1);
                    if (cornersInfo[2])
                        roundedInsideCorner(new Vector2(getX() + (i + 1) * cellWidth, getY() + (j + 1) * celHeight),
                                radius, 2);
                    if (cornersInfo[3])
                        roundedInsideCorner(new Vector2(getX() + i * cellWidth, getY() + (j + 1) * celHeight),
                                radius, 3);
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
     * @param inside is it an inside corner?
     * @return the array of booleans
     */
    private boolean[] getCornersInfo(int x, int y, boolean inside) {
        boolean[] cornersInfo = new boolean[4];

        int b = inside ? 1 : 0;

        cornersInfo[0] = getCellId(x - 1, y) == b &&
                getCellId(x, y - 1) == b;

        cornersInfo[1] = getCellId(x + 1, y) == b &&
                getCellId(x, y - 1) == b;

        cornersInfo[2] = getCellId(x + 1, y) == b &&
                getCellId(x, y + 1) == b;

        cornersInfo[3] = getCellId(x - 1, y) == b &&
                getCellId(x, y + 1) == b;

        if (!inside) {
            cornersInfo[0] = cornersInfo[0] && getCellId(x - 1, y - 1) == b;
            cornersInfo[1] = cornersInfo[1] &&  getCellId(x + 1, y - 1) == b;
            cornersInfo[2] = cornersInfo[2] && getCellId(x + 1, y + 1) == b;
            cornersInfo[3] = cornersInfo[3] && getCellId(x - 1, y + 1) == b;
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
        int segments = 32;

        if (isRounded[0] || isRounded[1] || isRounded[2] || isRounded[3]) {
            if (size.x >= size.y && radius > size.y / 2)
                radius = size.y / 2;
            else if (size.x < size.y && radius > size.x / 2)
                radius = size.x / 2;

            shapeRenderer.rect(position.x + radius, position.y + radius,
                    size.x - 2 * radius, size.y - 2 * radius);

            shapeRenderer.rect(position.x, position.y + radius, radius, size.y - 2 * radius);
            shapeRenderer.rect(position.x + radius, position.y + size.y - radius, size.x - 2 * radius, radius);
            shapeRenderer.rect(position.x + size.x - radius, position.y + radius, radius, size.y - 2 * radius);
            shapeRenderer.rect(position.x + radius, position.y, size.x - 2 * radius, radius);

            if (isRounded[0])
                shapeRenderer.arc(position.x + radius, position.y + radius, radius, -180, 90, segments);
            else
                shapeRenderer.rect(position.x, position.y, radius, radius);

            if (isRounded[1])
                shapeRenderer.arc(position.x + size.x - radius, position.y + radius, radius, -90, 90, segments);
            else
                shapeRenderer.rect(position.x + size.x - radius, position.y, radius, radius);

            if (isRounded[2])
                shapeRenderer.arc(position.x + size.x - radius, position.y + size.y - radius, radius, 0, 90, segments);
            else
                shapeRenderer.rect(position.x + size.x - radius, position.y + size.y - radius, radius, radius);

            if (isRounded[3])
                shapeRenderer.arc(position.x + radius, position.y + size.y - radius, radius, 90, 90, segments);
            else
                shapeRenderer.rect(position.x, position.y + size.y - radius, radius, radius);
        }
        else
            shapeRenderer.rect(position.x, position.y, size.x, size.y);
    }

    /**
     * Draws a rounded inside corner
     *
     * @param position the position
     * @param radius the radius of the corner
     * @param cornerNumber the number of the corner
     */
    private void roundedInsideCorner(Vector2 position, float radius, int cornerNumber) {
        int segments = 32;

        float[] vertices = new float[(segments + 1) * 2];

        float t = cornerNumber * (float) Math.PI / 2;

        for(int i = 0; i <= segments * 2; i += 2, t += Math.PI / (2 * segments)) {
            vertices[i] = position.x - radius * (float) Math.cos(t);
            vertices[i + 1] = position.y - radius * (float) Math.sin(t);

            if (cornerNumber == 0 || cornerNumber == 3)
                vertices[i] += radius;
            else
                vertices[i] -= radius;

            if (cornerNumber == 0 || cornerNumber == 1)
                vertices[i + 1] += radius;
            else
                vertices[i + 1] -= radius;
        }

        // Draw triangles
        for (int i = 0; i < vertices.length - 2; i += 2) {
            shapeRenderer.triangle(vertices[i], vertices[i + 1],
                    vertices[i + 2], vertices[i + 3],
                    position.x, position.y);
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
     * Sets the id for the given cell
     *
     * @param x the x-component of the cell position
     * @param y the y-component of the cell position
     * @param id the id
     */
    public void setCellId(int x, int y, int id) {
        if (x >= cells.length || x < 0 || y >= cells[0].length || y < 0) {
            return;
        }
        cells[x][y] = id;
    }

    /**
     * Returns the id of the cell
     *
     * @param x the x-component of the cell position
     * @param y the y-component of the cell position
     * @return the id
     */
    public int getCellId(int x, int y) {
        if (x >= cells.length || x < 0 || y >= cells[0].length || y < 0) {
            return 1;
        }
        return cells[x][y];
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
