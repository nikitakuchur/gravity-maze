package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.utils.Properties;
import com.github.nikitakuchur.puzzlegame.utils.PropertiesHolder;

public class Background extends Actor implements PropertiesHolder, Disposable {

    private Color startColor;
    private Color stopColor;

    private boolean[] dirs = new boolean[4];
    private float[] ts = new float[4];

    Color[] colors = new Color[4];

    private final float ANIMATION_SPEED = 0.1f;

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
        for(int i = 0; i < colors.length; i++) {
            if (ts[i] >= 1) dirs[i] = false;
            if (ts[i] <= 0) dirs[i] = true;
            if (dirs[i]) ts[i] += ANIMATION_SPEED * delta;
            else ts[i] -= ANIMATION_SPEED * delta;
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
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("startColor", Color.class, startColor);
        properties.put("stopColor", Color.class, stopColor);
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        startColor =  (Color) properties.getValue("startColor");
        stopColor = (Color) properties.getValue("stopColor");
        initAnimation();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
