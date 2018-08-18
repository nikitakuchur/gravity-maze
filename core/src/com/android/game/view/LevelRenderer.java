package com.android.game.view;

import com.android.game.model.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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

        setCamera( Gdx.graphics.getWidth()/2, Gdx.graphics.getWidth()/2);
    }

    public void setCamera(float x, float y) {
        this.camera.position.set(x, y, 0);
        this.camera.update();
    }

    public void draw() {
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        for(int i = 0; i < level.getWidth(); i++)
        {
            for(int j = 0; j < level.getHeight(); j++)
            {
                if(level.getCellId(j, i) == 1)
                    shapeRenderer.rect(i * cellWidth, j * cellWidth, cellWidth, cellWidth);
            }
        }
        shapeRenderer.end();
    }
}
