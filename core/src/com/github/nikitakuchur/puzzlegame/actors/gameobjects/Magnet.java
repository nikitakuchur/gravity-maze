package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.level.Layer;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalController;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;
import com.github.nikitakuchur.puzzlegame.physics.Physics;
import com.github.nikitakuchur.puzzlegame.utils.GameActions;

public class Magnet extends GameObject implements Disposable {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Action bounceAction;

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

        PhysicalObject pickedUpObject = store.getGameObjects(PhysicalObject.class).stream()
                .filter(p -> position.equals(p.getPhysicalController().getPosition()))
                .findAny()
                .orElse(null);

        if (pickedUpObject != null) {
            boolean hasUpperObject = store.getGameObjects(PhysicalObject.class).stream()
                    .anyMatch(p -> {
                        PhysicalController controller = p.getPhysicalController();
                        Vector2 nextPosition = controller.getPosition().add(level.getGravityDirection().getDirection());
                        return p != pickedUpObject && position.equals(nextPosition) && !controller.isMoving() && !controller.isFrozen();
                    });
            if (!hasUpperObject && !pickedUpObject.getPhysicalController().isFrozen()) {
                pickedUpObject.getPhysicalController().freeze();
                pickedUpObject.getPhysicalController().setVelocity(Vector2.Zero);
                addBounceAction(pickedUpObject);
            }
            if (hasUpperObject && pickedUpObject.getPhysicalController().isFrozen()) {
                pickedUpObject.getPhysicalController().unfreeze();
                removeBounceAction(pickedUpObject);
            }
        }
    }

    private void addBounceAction(PhysicalObject physicalObject) {
        bounceAction = Actions.forever(GameActions.bounce());
        ((GameObject) physicalObject).addAction(bounceAction);
    }

    private void removeBounceAction(PhysicalObject physicalObject) {
        if (bounceAction != null) {
            ((GameObject) physicalObject).removeAction(bounceAction);
            ((GameObject) physicalObject).setScale(1.f);
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
        return Layer.LAYER_0;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
