package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.effects.Effect;
import com.github.nikitakuchur.puzzlegame.level.Level;

public class Spike extends GameObject {

    private final Texture spikeTexture = new Texture(Gdx.files.internal("game/spike/spike.png"), true);
    private final Texture centerTexture = new Texture(Gdx.files.internal("game/spike/center.png"), true);

    private final TextureRegion spikeTextureRegion = new TextureRegion(spikeTexture);
    private final TextureRegion centerTextureRegion = new TextureRegion(centerTexture);

    private Effect effect;

    public Spike() {
        spikeTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        centerTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
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
                level.endGame();
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

    @Override
    public void dispose() {
        spikeTexture.dispose();
        centerTexture.dispose();
    }
}
