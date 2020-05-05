package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalController;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;
import com.github.nikitakuchur.puzzlegame.utils.Layer;

public class Ball extends GameObject implements PhysicalObject {

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
        shapeRenderer.setColor(getColor());
        shapeRenderer.ellipse(position.x, position.y, getWidth(), getHeight(), 32);

        shapeRenderer.setColor(Color.WHITE);

        float dw = (getWidth() * 0.7f - getWidth()) / 2;
        float dh = (getHeight() * 0.7f - getHeight()) / 2;
        shapeRenderer.ellipse(position.x - dw, position.y - dh,
                getWidth() * 0.7f, getHeight() * 0.7f, 32);

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public Layer getLayer() {
        return Layer.FRONT;
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
