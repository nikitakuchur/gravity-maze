package com.triateq.gravitymaze.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.serialization.Parameter;
import com.triateq.gravitymaze.utils.Context;

public class Switch extends GameObject {

    private final TextureRegion switchTextureRegion;
    private final TextureRegion glowTextureRegion;

    private Ball lastBall;

    @Parameter
    private boolean activated;

    public Switch(Context context) {
        AssetManager assetManager = context.getAssetManager();
        switchTextureRegion = new TextureRegion(assetManager.get("textures/switch/switch.png", Texture.class));
        glowTextureRegion = new TextureRegion(assetManager.get("textures/switch/glow.png", Texture.class));
        setColor(Color.WHITE.cpy());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        GameObjectStore store = level.getGameObjectStore();
        Ball ball = store.getGameObjects(Ball.class).stream()
                .filter(b -> getPosition().equals(b.getPosition()))
                .findAny()
                .orElse(null);
        if (ball == null) {
            lastBall = null;
            return;
        }
        if (lastBall != ball) {
            lastBall = ball;
            activated = !activated;
            store.getGameObjects(Switchable.class).stream()
                    .filter(s -> s.getSwitch() != null && s.getSwitch().equals(getName()))
                    .forEach(Switchable::onSwitch);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (!activated) {
            switchTextureRegion.setRegion(0, 0, 512, 512);
            glowTextureRegion.setRegion(0, 0, 512, 512);
        } else {
            switchTextureRegion.setRegion(512, 0, 512, 512);
            glowTextureRegion.setRegion(512, 0, 512, 512);
        }
        Vector2 position = getActualPosition();
        batch.setColor(getColor());
        batch.draw(switchTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(Color.WHITE);
        batch.draw(glowTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
