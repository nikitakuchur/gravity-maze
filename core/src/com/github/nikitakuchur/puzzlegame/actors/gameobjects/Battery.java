package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.level.Layer;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalController;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;

public class Battery extends GameObject implements PhysicalObject {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private PhysicalController physicalController;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        physicalController = new PhysicalController(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        Vector2 position = getActualPosition();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK.cpy());
        shapeRenderer.rect(position.x, position.y, getWidth(), getHeight());
        shapeRenderer.setColor(getColor());
        float dw = getWidth() / 4.f;
        float dh = getHeight() / 4.f;
        shapeRenderer.rect(position.x + dw, position.y + dh, getWidth() - dw * 2, getHeight() - dh * 2);

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public Layer getLayer() {
        return Layer.FRONT;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }
}
