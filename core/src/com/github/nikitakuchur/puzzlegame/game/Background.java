package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.utils.JsonUtils;

import java.util.Optional;

public class Background extends Actor implements Json.Serializable, Disposable {

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
    public void write(Json json) {
        json.writeValue("startColor", startColor.toString());
        json.writeValue("stopColor", stopColor.toString());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        Optional.ofNullable(JsonUtils.getColor(jsonData, "startColor")).ifPresent(this::setStartColor);
        Optional.ofNullable(JsonUtils.getColor(jsonData, "stopColor")).ifPresent(this::setStopColor);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
