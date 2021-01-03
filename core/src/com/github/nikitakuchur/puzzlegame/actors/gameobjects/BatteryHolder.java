package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.level.Layer;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;

public class BatteryHolder extends GameObject {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private GameObjectStore store;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        store = level.getGameObjectStore();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 position = getPosition();

        Battery battery = store.getGameObjects(Battery.class).stream()
                .filter(b -> position.equals(b.getPosition()))
                .findAny()
                .orElse(null);

        if (battery != null) {
            PhysicalObject physicalObject = store.getGameObjects(PhysicalObject.class).stream()
                    .filter(p -> {
                        Vector2 nextPosition = p.getPhysicalController().getPosition().add(level.getGravityDirection().getDirection());
                        return p != battery && position.equals(nextPosition) && !p.getPhysicalController().isMoving();
                    }).findAny()
                    .orElse(null);
            if (physicalObject == null && !battery.getPhysicalController().isFrozen()) {
                battery.getPhysicalController().freeze();
            }
            if (physicalObject != null && battery.getPhysicalController().isFrozen()) {
                battery.getPhysicalController().unfreeze();
            }
        }
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
        shapeRenderer.ellipse(position.x, position.y, getWidth(), getHeight(), 4);

        shapeRenderer.identity();
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public Layer getLayer() {
        return Layer.BACK;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
