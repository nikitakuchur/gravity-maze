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

    private float cellWidth;

    public LevelRenderer(Level level) {
        this.level = level;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();

        cellWidth = (float)Gdx.graphics.getWidth()/level.getWidth();

        setCameraPosition(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getWidth()/2));
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
        shapeRenderer.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getWidth()/2, 0);
        shapeRenderer.scale(level.getScale(), level.getScale(), level.getScale());
        shapeRenderer.rotate(0, 0, 1, level.getRotation());
        shapeRenderer.translate(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getWidth()/2, 0);

        // Draw the map and other things
        drawMap();
        drawBalls();

        shapeRenderer.identity();
    }

    private void drawBackground() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, Gdx.graphics.getWidth()/2 - Gdx.graphics.getHeight()/2,
                            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                            new Color(0.81f, 0.45f, 0.5f, 1),
                            new Color(0.89f, 0.59f, 0.46f, 1),
                            new Color(0.99f, 0.73f, 0.4f, 1),
                            new Color(0.89f, 0.59f, 0.46f, 1));
        shapeRenderer.end();
    }

    private void drawMap() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.19f, 0.29f, 0.37f, 1));

        // Left
        shapeRenderer.rect(0, 0, -cellWidth * level.getHeight(), cellWidth * level.getHeight());

        // Right
        shapeRenderer.rect(cellWidth * level.getHeight(), 0,
                cellWidth * level.getHeight(), cellWidth * level.getHeight());

        // Top
        shapeRenderer.rect(-cellWidth * level.getHeight(), cellWidth * level.getHeight(),
                cellWidth * level.getHeight() * 3, cellWidth * level.getHeight());

        // Bottom
        shapeRenderer.rect(-cellWidth * level.getHeight(), 0,
                cellWidth * level.getHeight() * 3, -cellWidth * level.getHeight());

        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                if (level.getCellId(j, i) == 1)
                    shapeRenderer.rect(i * cellWidth, j * cellWidth, cellWidth, cellWidth);
            }
        }
        shapeRenderer.end();
    }

    private void drawBalls() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Ball ball: level.getBalls()) {
            shapeRenderer.setColor(ball.getColor());
            shapeRenderer.circle((ball.getPosition().x + 0.5f) * cellWidth,
                                 (ball.getPosition().y + 0.5f) * cellWidth,
                                 cellWidth/2, 32);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle((ball.getPosition().x + 0.5f) * cellWidth,
                                 (ball.getPosition().y + 0.5f) * cellWidth,
                                 cellWidth/2.8f, 32);
        }
        shapeRenderer.end();
    }
}