package com.triateq.gravitymaze.game.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.game.effects.Effect;
import com.triateq.gravitymaze.game.level.Level;
import com.triateq.gravitymaze.core.game.Context;

public class Spike extends GameObject {

    private final TextureRegion spikeTextureRegion;
    private final TextureRegion centerTextureRegion;

    private Effect effect;

    public Spike(Context context) {
        AssetManager assetManager = context.getAssetManager();
        spikeTextureRegion = new TextureRegion(assetManager.get("textures/spike/spike.png", Texture.class));
        centerTextureRegion = new TextureRegion(assetManager.get("textures/spike/center.png", Texture.class));
        setColor(Color.RED.cpy());
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        effect = new Effect(level)
                .position(getPosition())
                .count(64)
                .delay(0.8f)
                .useGravity();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        GameObjectStore store = level.getGameObjectStore();
        store.getGameObjects(Ball.class).forEach(ball -> {
            if (getPosition().equals(ball.getPosition())) {
                store.remove(ball);
                effect.color(ball.getColor()).start();
                level.endGame(true);
            }
        });
        effect.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = getActualPosition();
        batch.setColor(Color.WHITE);
        batch.draw(spikeTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(getColor());
        batch.draw(centerTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
               getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        effect.draw(batch);
    }
}
