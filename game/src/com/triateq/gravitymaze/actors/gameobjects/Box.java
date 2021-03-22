package com.triateq.gravitymaze.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.level.Layer;
import com.triateq.gravitymaze.level.Level;
import com.triateq.gravitymaze.physics.PhysicalController;
import com.triateq.gravitymaze.physics.PhysicalObject;
import com.triateq.puzzlecore.game.Context;

public class Box extends GameObject implements PhysicalObject {

    private final TextureRegion textureRegion;

    private PhysicalController physicalController;

    public Box(Context context) {
        AssetManager assetManager = context.getAssetManager();
        textureRegion = new TextureRegion(assetManager.get("textures/box/box.png", Texture.class));
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        physicalController = new PhysicalController(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = getActualPosition();
        batch.setColor(getColor());
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public Layer getLayer() {
        return Layer.LAYER_2;
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }
}
