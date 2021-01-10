package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.level.Layer;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalController;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;
import com.github.nikitakuchur.puzzlegame.utils.Context;

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
        return Layer.FRONT;
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }
}
