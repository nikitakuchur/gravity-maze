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

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Background() {
        this.startColor = Color.WHITE;
        this.stopColor = Color.WHITE;
    }

    /**
     * Creates a gradient background
     */
    public Background(Color startColor, Color stopColor) {
        this.startColor = startColor;
        this.stopColor = stopColor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Color midColor = startColor.cpy().lerp(stopColor, 0.5f);
        shapeRenderer.rect(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                midColor, stopColor, midColor, startColor);
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
        properties.put("startColor", String.class, startColor.toString());
        properties.put("stopColor", String.class, stopColor.toString());
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        startColor = Color.valueOf((String) properties.getValueOrDefault("startColor", startColor.toString()));
        stopColor = Color.valueOf((String) properties.getValueOrDefault("stopColor", stopColor.toString()));
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
