package com.triateq.gravitymaze.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.triateq.gravitymaze.level.Layer;
import com.triateq.gravitymaze.level.Level;
import com.triateq.gravitymaze.physics.EmptyCollider;
import com.triateq.gravitymaze.physics.FullCollider;
import com.triateq.gravitymaze.physics.PhysicalController;
import com.triateq.gravitymaze.physics.PhysicalObject;
import com.triateq.puzzlecore.serialization.Parameter;
import com.triateq.puzzlecore.game.Context;

public class Barrier extends GameObject implements Switchable, PhysicalObject {

    private final TextureRegion textureRegion;

    @Parameter
    private boolean opened;
    @Parameter(name = "switch")
    private String switchName;

    private PhysicalController physicalController;

    public Barrier(Context context) {
        AssetManager assetManager = context.getAssetManager();
        textureRegion = new TextureRegion(assetManager.get("textures/box/box.png", Texture.class));
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        physicalController = new PhysicalController(this);
        physicalController.freeze();
        if (opened) {
            physicalController.setCollider(EmptyCollider.INSTANCE);
        } else {
            physicalController.setCollider(FullCollider.INSTANCE);
        }
    }

    @Override
    public void onSwitch() {
        if (opened) {
            physicalController.setCollider(FullCollider.INSTANCE);
            clearActions();
            addAction(Actions.fadeIn(0.1f));
        } else {
            physicalController.setCollider(EmptyCollider.INSTANCE);
            clearActions();
            addAction(Actions.fadeOut(0.1f));
        }
        opened = !opened;
    }

    @Override
    public String getSwitch() {
        return switchName;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = opened ? getColor().cpy().mul(1, 1, 1, 0) : getColor().cpy().add(0, 0, 0, 1);
        batch.setColor(color);
        Vector2 position = getActualPosition();
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX() / 2, getScaleY() / 2, getRotation());
    }

    @Override
    public Layer getLayer() {
        return Layer.LAYER_3;
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }
}
