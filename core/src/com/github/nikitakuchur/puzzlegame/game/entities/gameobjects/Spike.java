package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.game.effects.Effect;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;

public class Spike extends GameObject {

    private final Texture texture = new Texture(Gdx.files.internal("game/spike.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    private Effect effect;

    public Spike() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        effect = new Effect(level)
                .position(new Vector2(getX(), getY()))
                .count(64)
                .delay(0.8f)
                .useGravity();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        GameObjectsManager manager = level.getGameObjectsManager();
        manager.getGameObjects(Ball.class).forEach(ball -> {
            if (getX() == ball.getX() && getY() == ball.getY()) {
                manager.remove(ball);
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
        batch.setColor(getColor()); // TODO: Fix color
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        effect.draw(batch);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
