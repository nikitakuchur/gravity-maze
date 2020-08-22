package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

public class Background extends Actor implements Parameterizable, Disposable {

    private Color startColor;
    private Color stopColor;

    private boolean[] dirs;
    private float[] ts;
    private final Color[] colors = new Color[4];

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

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
        dirs = new boolean[]{false, false, true, true};
        ts = new float[]{0.5f, 1, 0.5f, 0};
        for (int i = 0; i < colors.length; i++) {
            colors[i] = startColor.cpy().lerp(stopColor, ts[i]);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float speed = 0.1f;
        for (int i = 0; i < colors.length; i++) {
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
    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        parameters.put("startColor", Color.class, startColor.cpy());
        parameters.put("stopColor", Color.class, stopColor.cpy());
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        startColor = parameters.getValue("startColor");
        stopColor = parameters.getValue("stopColor");
        initAnimation();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
