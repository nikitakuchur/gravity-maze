package com.majakkagames.gravitymaze.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.Parameters;
import com.majakkagames.gravitymaze.core.serialization.annotations.Parameter;

public class Background extends GameObject implements Disposable {

    @Parameter
    private Color firstColor;
    @Parameter
    private Color secondColor;

    private boolean[] dirs;
    private float[] ts;
    private final Color[] colors = new Color[4];

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Background() {
        this.firstColor = Color.WHITE;
        this.secondColor = Color.WHITE;
        initAnimation();
    }

    /**
     * Creates a gradient background
     */
    public Background(Color firstColor, Color secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        initAnimation();
    }

    void initAnimation() {
        dirs = new boolean[]{false, false, true, true};
        ts = new float[]{0.5f, 1, 0.5f, 0};
        for (int i = 0; i < colors.length; i++) {
            colors[i] = firstColor.cpy().lerp(secondColor, ts[i]);
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
            colors[i] = firstColor.cpy().lerp(secondColor, ts[i]);
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

    public Color getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(Color firstColor) {
        this.firstColor = firstColor;
    }

    public Color getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(Color secondColor) {
        this.secondColor = secondColor;
    }

    @Override
    public Color getColor() {
        return firstColor.cpy().lerp(secondColor, 0.5f);
    }

    @Override
    public Parameters getParameters() {
        return new Parameters();
    }

    @Override
    public void setParameters(Parameters parameters) {
        initAnimation();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
