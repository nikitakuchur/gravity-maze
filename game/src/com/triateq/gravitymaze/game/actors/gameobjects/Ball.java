package com.triateq.gravitymaze.game.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.game.level.Level;
import com.triateq.gravitymaze.game.physics.PhysicalController;
import com.triateq.gravitymaze.game.physics.PhysicalObject;
import com.triateq.gravitymaze.game.level.Layer;
import com.triateq.gravitymaze.core.game.Context;

public class Ball extends GameObject implements PhysicalObject {

    private final TextureRegion outlineTextureRegion;
    private final TextureRegion ballTextureRegion;

    private PhysicalController physicalController;

    public Ball(Context context) {
        AssetManager assetManager = context.getAssetManager();
        outlineTextureRegion = new TextureRegion(assetManager.get("textures/ball/outline.png", Texture.class));
        ballTextureRegion = new TextureRegion(assetManager.get("textures/ball/ball.png", Texture.class));
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
        batch.setColor(Color.WHITE);
        batch.draw(ballTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(getColor());
        batch.draw(outlineTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
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