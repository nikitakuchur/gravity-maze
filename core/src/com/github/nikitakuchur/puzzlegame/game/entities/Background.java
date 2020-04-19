package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public class Background extends Actor implements Entity {

    private Color startColor;
    private Color stopColor;

    private boolean[] dirs = new boolean[4];
    private float[] ts = new float[4];

    private Color[] colors = new Color[4];

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Background() {
        this.startColor = Color.WHITE;
        this.stopColor = Color.WHITE;
        initAnimation();
    }

    /**
     * Creates a gradient background
     */
    public Background(Color startColor, Color stopColor) {
        this.startColor = startColor;
        this.stopColor = stopColor;
        initAnimation();
    }

    void initAnimation() {
        ts[0] = 0.5f;
        ts[1] = 1;
        ts[2] = 0.5f;
        ts[3] = 0;
        dirs[2] = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float speed = 0.1f;
        for(int i = 0; i < colors.length; i++) {
            if (ts[i] >= 1) dirs[i] = false;
            if (ts[i] <= 0) dirs[i] = true;
            if (dirs[i]) ts[i] += speed * delta;
            else ts[i] -= speed * delta;
            colors[i] = startColor.cpy().lerp(stopColor, ts[i]);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                colors[0], colors[1], colors[2], colors[3]);
        shapeRenderer.end();
        batch.begin();
    }

    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public Color getStopColor() {
        return stopColor;
    }

    public void setStopColor(Color stopColor) {
        this.stopColor = stopColor;
    }

    @Override
    public Color getColor() {
        return startColor.cpy().lerp(stopColor, 0.5f);
    }

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("startColor", Color.class, startColor);
        properties.put("stopColor", Color.class, stopColor);
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        startColor = (Color) properties.getValue("startColor");
        stopColor = (Color) properties.getValue("stopColor");
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}