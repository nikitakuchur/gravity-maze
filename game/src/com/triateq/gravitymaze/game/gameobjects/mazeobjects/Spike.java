package com.triateq.gravitymaze.game.gameobjects.mazeobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.core.game.GameObjectStore;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.game.effects.Effect;
import com.triateq.gravitymaze.game.gameobjects.LevelController;

public class Spike extends MazeObject {

    private TextureRegion spikeTextureRegion;
    private TextureRegion centerTextureRegion;

    private LevelController levelController;

    private Effect effect;

    public Spike() {
        setColor(Color.RED.cpy());
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        spikeTextureRegion = new TextureRegion(assetManager.get("textures/spike/spike.png", Texture.class));
        centerTextureRegion = new TextureRegion(assetManager.get("textures/spike/center.png", Texture.class));
        levelController = level.getGameObjectStore().getAnyGameObjectOrThrow(LevelController.class,
                () -> new IllegalStateException("Cannot find the level controller object"));
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
                levelController.endGame(true);
            }
        });
        effect.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = maze.getActualCoords(getX(), getY());
        batch.setColor(Color.WHITE);
        batch.draw(spikeTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(getColor());
        batch.draw(centerTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
               getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        effect.draw(batch);
    }
}
