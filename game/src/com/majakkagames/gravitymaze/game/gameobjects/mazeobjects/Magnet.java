package com.majakkagames.gravitymaze.game.gameobjects.mazeobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.game.gameobjects.Gravity;
import com.majakkagames.gravitymaze.game.physics.PhysicalController;
import com.majakkagames.gravitymaze.game.physics.PhysicalObject;
import com.majakkagames.gravitymaze.game.utils.GameActions;

public class Magnet extends MazeObject {

    private static final float ANIMATION_SPEED = 15.f;
    private static final int FRAMES_NUMBER = 9;

    private TextureRegion textureRegion;

    private Action bounceAction;

    private GameObjectStore store;

    private float frame;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        textureRegion = new TextureRegion(assetManager.get("textures/magnet/magnet.png", Texture.class));
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
            boolean hasUpperObject = hasUpperObject(position);
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

        frame += ANIMATION_SPEED * delta;
        if (frame >= FRAMES_NUMBER) frame = 0;
    }

    private boolean hasUpperObject(Vector2 position) {
        Gravity gravity = store.getAnyGameObject(Gravity.class);
        if (gravity == null) {
            throw new IllegalStateException("Cannot find the gravity object");
        }

        Vector2 dir = gravity.getGravityDirection().getDirection();
        Vector2 upperPos = position.cpy().sub(dir);

        boolean hasObject = store.getGameObjects(PhysicalObject.class).stream()
                .anyMatch(p -> {
                    PhysicalController<?> controller = p.getPhysicalController();
                    return controller.getPosition().equals(upperPos) && !controller.isMoving() && !controller.isFrozen();
                });

        boolean hasMagnet = store.getGameObjects(Magnet.class).stream()
                .anyMatch(m -> m.getPosition().equals(upperPos));

        if (hasObject && hasMagnet) {
            return hasUpperObject(upperPos);
        }
        return hasObject;
    }

    private void addBounceAction(PhysicalObject physicalObject) {
        bounceAction = Actions.forever(GameActions.bounce());
        ((Actor) physicalObject).addAction(bounceAction);
    }

    private void removeBounceAction(PhysicalObject physicalObject) {
        if (bounceAction != null) {
            ((Actor) physicalObject).removeAction(bounceAction);
            ((Actor) physicalObject).setScale(1.f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        Vector2 position = maze.getActualCoords(getX(), getY());
        textureRegion.setRegion((int) frame * 512, 0, 512, 512);
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX() * 1.2f, getScaleY() * 1.2f, getRotation());
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
