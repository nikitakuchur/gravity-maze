package com.majakkagames.gravitymaze.game.gameobjects.mazeobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.core.serialization.annotations.Parameter;

public class Switch extends MazeObject {

    private TextureRegion switchTextureRegion;
    private TextureRegion glowTextureRegion;

    private Ball lastBall;

    @Parameter
    private boolean activated;

    public Switch() {
        setColor(Color.WHITE.cpy());
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        switchTextureRegion = new TextureRegion(assetManager.get("textures/switch/switch.png", Texture.class));
        glowTextureRegion = new TextureRegion(assetManager.get("textures/switch/glow.png", Texture.class));
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
        Vector2 position = maze.getActualCoords(getX(), getY());
        batch.setColor(getColor());
        batch.draw(switchTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(Color.WHITE);
        batch.draw(glowTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
