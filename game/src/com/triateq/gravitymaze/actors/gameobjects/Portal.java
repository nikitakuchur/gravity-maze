package com.triateq.gravitymaze.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.triateq.gravitymaze.effects.Effect;
import com.triateq.gravitymaze.level.Level;
import com.triateq.gravitymaze.physics.PhysicalController;
import com.triateq.gravitymaze.physics.PhysicalObject;
import com.triateq.puzzlecore.serialization.Parameter;
import com.triateq.puzzlecore.game.Context;
import com.triateq.gravitymaze.utils.GameActions;

public class Portal extends GameObject {

    private final TextureRegion textureRegion;

    @Parameter(name = "to")
    private String secondPortalName;

    private boolean locked;

    private Effect effect;

    private GameObjectStore store;

    /**
     * Creates a new portal.
     */
    public Portal(Context context) {
        AssetManager assetManager = context.getAssetManager();
        textureRegion = new TextureRegion(assetManager.get("textures/portal/portal.png", Texture.class));
        addAction(Actions.forever(GameActions.bounceAndRotate()));
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        store = level.getGameObjectStore();
        effect = new Effect(level)
                .color(getColor())
                .size(5)
                .speed(300)
                .delay(0.5f)
                .useGravity();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Portal secondPortal = store.find(Portal.class, secondPortalName);
        Vector2 position = getPosition();

        if (secondPortal == null || detectCollision()) return;

        PhysicalObject physicalObject = store.getGameObjects(PhysicalObject.class).stream()
                .filter(obj -> position.equals(obj.getPhysicalController().getPosition()))
                .findAny()
                .orElse(null);

        if (physicalObject != null && !locked && !secondPortal.locked) {
            PhysicalController physicalController = physicalObject.getPhysicalController();
            physicalController.setPosition(secondPortal.getPosition());
            locked = true;
            secondPortal.locked = true;

            effect.position(position)
                    .direction(physicalController.getVelocity().rotateDeg(180))
                    .start();
            secondPortal.effect.position(secondPortal.getPosition())
                    .direction(physicalController.getVelocity())
                    .start();
        }

        if (physicalObject == null && secondPortal.isFree()) {
            locked = false;
            secondPortal.locked = false;
        }

        effect.update(delta);
    }

    private boolean detectCollision() {
        Portal secondPortal = store.find(Portal.class, secondPortalName);

        boolean firstObjectDetected = store.getGameObjects(PhysicalObject.class).stream()
                .anyMatch(p -> getPosition().equals(p.getPhysicalController().getPosition()));
        boolean secondObjectDetected = store.getGameObjects(PhysicalObject.class).stream()
                .anyMatch(p -> secondPortal.getPosition().equals(p.getPhysicalController().getPosition()));

        return firstObjectDetected && secondObjectDetected;
    }

    private boolean isFree() {
        for (PhysicalObject physicalObject : store.getGameObjects(PhysicalObject.class)) {
            if (getPosition().equals(physicalObject.getPhysicalController().getPosition())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        Vector2 position = getActualPosition();
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        effect.draw(batch);
    }

    /**
     * Sets the second portal.
     *
     * @param name the second portal name
     */
    public void to(String name) {
        this.secondPortalName = name;
    }
}
