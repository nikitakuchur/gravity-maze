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

    private int cellWidth;

    public LevelRenderer(Level level) {
        this.level = level;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();

        cellWidth = Gdx.graphics.getWidth()/level.getWidth();

        setCamera(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getWidth()/2));
    }

    public void setCamera(Vector2 position) {
        this.camera.position.set(position, 0);
        this.camera.update();
    }

    public void draw() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        drawBackground();
        drawMap();
        drawBalls();
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
        for(int i = 0; i < level.getWidth(); i++) {
            for(int j = 0; j < level.getHeight(); j++) {
                if(level.getCellId(j, i) == 1)
                    shapeRenderer.rect(i * cellWidth, j * cellWidth, cellWidth, cellWidth);
            }
        }
        shapeRenderer.end();
    }

    private void drawBalls() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Ball ball: level.getBalls()) {
            shapeRenderer.setColor(ball.getColor());
            shapeRenderer.circle((ball.getPosition().x + 0.5f) * cellWidth ,
                                 (ball.getPosition().y + 0.5f) * cellWidth,
                                 cellWidth/2);
        }
        shapeRenderer.end();
    }
}
