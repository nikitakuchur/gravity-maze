package com.android.game.view;

import com.android.game.model.Ball;
import com.android.game.model.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class LevelRenderer {

    private Level level;
    private OrthographicCamera camera;

    private ShapeRenderer shapeRenderer;

    private float cellSize;
    private Vector2 mapPosition;

    public LevelRenderer(Level level) {
        this.level = level;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();

        cellSize = (float)Gdx.graphics.getWidth() /
                (level.getHeight() > level.getWidth() ? level.getHeight() : level.getWidth());

        mapPosition = new Vector2(0, 0);

        // Calculate the map position
        if (level.getWidth() > level.getHeight()) {
            mapPosition.add(0, cellSize * ((float) level.getWidth() / 2 - (float) level.getHeight() / 2));
        } else {
            mapPosition.add(cellSize * ((float) level.getHeight() / 2 - (float) level.getWidth() / 2), 0);
        }

        setCameraPosition(new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getWidth() / 2));
    }

    public void setCameraPosition(Vector2 position) {
        this.camera.position.set(position, 0);
        this.camera.update();
    }

    public void draw() {
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw background
        drawBackground();

        // Rotate and scale the map
        shapeRenderer.translate((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getWidth() / 2, 0);
        shapeRenderer.scale(level.getScale(), level.getScale(), level.getScale());
        shapeRenderer.rotate(0, 0, 1, level.getRotation());
        shapeRenderer.translate(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getWidth() / 2, 0);

        // Draw the map and other things
        drawMap();
        drawBalls();
        shapeRenderer.identity();
    }

    private void drawBackground() {
        Color[] backgroundColor = level.getBackgroundColor();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, (float) Gdx.graphics.getWidth()/2 - (float) Gdx.graphics.getHeight()/2,
                            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                            backgroundColor[0],
                            backgroundColor[1],
                            backgroundColor[2],
                            backgroundColor[3]);
        shapeRenderer.end();
    }

    private void drawMap() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(level.getColor());

        float size = Gdx.graphics.getHeight() > Gdx.graphics.getWidth() ?
                     Gdx.graphics.getHeight() : Gdx.graphics.getWidth();

        // Left
        shapeRenderer.rect(mapPosition.x, mapPosition.y, -size, cellSize * level.getHeight());

        // Right
        shapeRenderer.rect(mapPosition.x + cellSize * level.getWidth(), mapPosition.y,
                size, cellSize * level.getHeight());

        // Top
        shapeRenderer.rect(mapPosition.x - size, mapPosition.y + cellSize * level.getHeight(),
                size * 2 + cellSize * level.getWidth(), size);

        // Bottom
        shapeRenderer.rect(mapPosition.x - size, mapPosition.y,
                size * 2 + cellSize * level.getWidth(), -size);


        float radius = 8;

        // Draw cells
        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                if (level.getCellId(i, j) == 1) {
                    boolean[] cornersInfo = getCornersInfo(i, j, false);

                    roundedRect(mapPosition.x + i * cellSize, mapPosition.y + j * cellSize, cellSize, cellSize, radius,
                            cornersInfo[0], cornersInfo[1], cornersInfo[2], cornersInfo[3]);
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

    private boolean[] getCornersInfo(int x, int y, boolean inside) {
        boolean[] cornersInfo = new boolean[4];

        int b = 0;
        if (inside)
            b = 1;

        cornersInfo[0] = level.getCellId(x - 1, y) == b &&
                level.getCellId(x, y - 1) == b &&
                level.getCellId(x - 1, y - 1) == b;

        cornersInfo[1] = level.getCellId(x + 1, y) == b &&
                level.getCellId(x, y - 1) == b &&
                level.getCellId(x + 1, y - 1) == b;

        cornersInfo[2] = level.getCellId(x + 1, y) == b &&
                level.getCellId(x, y + 1) == b &&
                level.getCellId(x + 1, y + 1) == b;

        cornersInfo[3] = level.getCellId(x - 1, y) == b &&
                level.getCellId(x, y + 1) == b &&
                level.getCellId(x - 1, y + 1) == b;

        return cornersInfo;
    }

    private void roundedRect(float x, float y, float width, float height, float radius,
                             boolean bl, boolean br, boolean tr, boolean tl) {
        int segments = 32;

        if (bl || br || tr || tl) {
            if (width >= height && radius > height / 2)
                radius = height / 2;
            else if (width < height && radius > width / 2)
                radius = width / 2;

            shapeRenderer.rect(x + radius, y + radius, width - 2 * radius, height - 2 * radius);

            shapeRenderer.rect(x, y + radius, radius, height - 2 * radius);
            shapeRenderer.rect(x + radius, y + height - radius, width - 2 * radius, radius);
            shapeRenderer.rect(x + width - radius, y + radius, radius, height - 2 * radius);
            shapeRenderer.rect(x + radius, y, width - 2 * radius, radius);

            if (bl)
                shapeRenderer.arc(x + radius, y + radius, radius, -180, 90, segments);
            else
                shapeRenderer.rect(x, y, radius, radius);

            if (br)
                shapeRenderer.arc(x + width - radius, y + radius, radius, -90, 90, segments);
            else
                shapeRenderer.rect(x + width - radius, y, radius, radius);

            if (tr)
                shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0, 90, segments);
            else
                shapeRenderer.rect(x + width - radius, y + height - radius, radius, radius);

            if (tl)
                shapeRenderer.arc(x + radius, y + height - radius, radius, 90, 90, segments);
            else
                shapeRenderer.rect(x, y + height - radius, radius, radius);
        }
        else
            shapeRenderer.rect(x, y, width, height);
    }

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

    private void drawBalls() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Ball ball: level.getBalls()) {
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