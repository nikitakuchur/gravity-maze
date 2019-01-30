package com.android.game.view;

import com.android.game.model.Ball;
import com.android.game.model.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class MapRenderer {

    private Map map;

    private ShapeRenderer shapeRenderer;

    private float cellSize;
    private Vector2 mapPosition;

    public MapRenderer(Map map) {
        this.map = map;

        shapeRenderer = new ShapeRenderer();

        cellSize = (float)Gdx.graphics.getWidth() /
                (map.getHeight() > map.getWidth() ? map.getHeight() : map.getWidth());

        mapPosition = new Vector2(0, 0);

        // Calculate the map position
        if (map.getWidth() > map.getHeight()) {
            mapPosition.add(0, cellSize * ((float) map.getWidth() / 2 - (float) map.getHeight() / 2));
        } else {
            mapPosition.add(cellSize * ((float) map.getHeight() / 2 - (float) map.getWidth() / 2), 0);
        }
    }

    public void draw(Matrix4 projectionMatrix) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);

        // Draw background
        drawBackground();

        // Rotate and scale the level
        shapeRenderer.translate((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getWidth() / 2, 0);
        shapeRenderer.scale(map.getScale(), map.getScale(), map.getScale());
        shapeRenderer.rotate(0, 0, 1, map.getRotation());
        shapeRenderer.translate(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getWidth() / 2, 0);

        // Draw the map
        drawCells();
        drawBalls();

        shapeRenderer.identity();
    }

    /**
     * Draws the background
     */
    private void drawBackground() {
        Color[] backgroundColor = map.getBackgroundColor();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, (float) Gdx.graphics.getWidth()/2 - (float) Gdx.graphics.getHeight()/2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                backgroundColor[0],
                backgroundColor[1],
                backgroundColor[2],
                backgroundColor[3]);
        shapeRenderer.end();
    }

    /**
     * Draws the cells of the map
     */
    private void drawCells() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(map.getColor());

        float size = Gdx.graphics.getHeight() > Gdx.graphics.getWidth() ?
                Gdx.graphics.getHeight() : Gdx.graphics.getWidth();

        // Left
        shapeRenderer.rect(mapPosition.x, mapPosition.y, -size, cellSize * map.getHeight());

        // Right
        shapeRenderer.rect(mapPosition.x + cellSize * map.getWidth(), mapPosition.y,
                size, cellSize * map.getHeight());

        // Top
        shapeRenderer.rect(mapPosition.x - size, mapPosition.y + cellSize * map.getHeight(),
                size * 2 + cellSize * map.getWidth(), size);

        // Bottom
        shapeRenderer.rect(mapPosition.x - size, mapPosition.y,
                size * 2 + cellSize * map.getWidth(), -size);


        float radius = 8;

        // Draw cells
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (map.getCellId(i, j) == 1) {
                    boolean[] cornersInfo = getCornersInfo(i, j, false);

                    roundedRect(new Vector2(mapPosition.x + i * cellSize, mapPosition.y + j * cellSize),
                                new Vector2(cellSize, cellSize), radius, cornersInfo);
                } else {
                    boolean[] cornersInfo = getCornersInfo(i, j, true);

                    if (cornersInfo[0])
                        roundedInsideCorner(mapPosition.x + i * cellSize,
                                mapPosition.y + j * cellSize,
                                radius, 0);
                    if (cornersInfo[1])
                        roundedInsideCorner(mapPosition.x + (i + 1) * cellSize,
                                mapPosition.y + j * cellSize,
                                radius, 1);
                    if (cornersInfo[2])
                        roundedInsideCorner(mapPosition.x + (i + 1) * cellSize,
                                mapPosition.y + (j + 1) * cellSize,
                                radius, 2);
                    if (cornersInfo[3])
                        roundedInsideCorner(mapPosition.x + i * cellSize,
                                mapPosition.y + (j + 1) * cellSize,
                                radius, 3);
                }
            }
        }
        shapeRenderer.end();
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

        int b = 0;
        if (inside)
            b = 1;

        cornersInfo[0] = map.getCellId(x - 1, y) == b &&
                map.getCellId(x, y - 1) == b &&
                map.getCellId(x - 1, y - 1) == b;

        cornersInfo[1] = map.getCellId(x + 1, y) == b &&
                map.getCellId(x, y - 1) == b &&
                map.getCellId(x + 1, y - 1) == b;

        cornersInfo[2] = map.getCellId(x + 1, y) == b &&
                map.getCellId(x, y + 1) == b &&
                map.getCellId(x + 1, y + 1) == b;

        cornersInfo[3] = map.getCellId(x - 1, y) == b &&
                map.getCellId(x, y + 1) == b &&
                map.getCellId(x - 1, y + 1) == b;

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
     * @param x the x-component of the position
     * @param y the y-component of the position
     * @param radius the radius of the corner
     * @param cornerNumber the number of the corner
     */
    private void roundedInsideCorner(float x, float y, float radius, int cornerNumber) {
        int segments = 32;

        float[] vertices = new float[(segments + 1) * 2];

        float t = cornerNumber * (float) Math.PI / 2;

        for(int i = 0; i <= segments * 2; i += 2, t += Math.PI / (2 * segments)) {
            vertices[i] = x - radius * (float) Math.cos(t);
            vertices[i + 1] = y - radius * (float) Math.sin(t);

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
                    x, y);
        }
    }

    /**
     * Draws the balls
     */
    private void drawBalls() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Ball ball: map.getBalls()) {
            shapeRenderer.setColor(ball.getColor());
            shapeRenderer.circle(mapPosition.x + (ball.getPosition().x + 0.5f) * cellSize,
                    mapPosition.y + (ball.getPosition().y + 0.5f) * cellSize,
                    cellSize/2, 32);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(mapPosition.x + (ball.getPosition().x + 0.5f) * cellSize,
                    mapPosition.y + (ball.getPosition().y + 0.5f) * cellSize,
                    cellSize/2.8f, 32);
        }
        shapeRenderer.end();
    }
}
