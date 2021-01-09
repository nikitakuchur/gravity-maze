package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.nikitakuchur.puzzlegame.effects.Effect;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.utils.GameActions;
import com.github.nikitakuchur.puzzlegame.serialization.Parameters;

public class Cup extends GameObject {

    private String ballName;

    private final Texture texture = new Texture(Gdx.files.internal("game/cup/cup.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    private Effect effect;

    private boolean destroy;

    public Cup() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        addAction(Actions.forever(Actions.timeScale(0.4f, GameActions.bounceAndRotate())));
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        effect = new Effect(level)
                .color(getColor())
                .position(getPosition())
                .delay(0.8f)
                .useGravity();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        GameObjectStore store = level.getGameObjectStore();

        Ball ball = store.find(Ball.class, ballName);
        if (ball != null && getPosition().equals(ball.getPosition())) {
            store.remove(ball);
            ballName = null;
            destroy = true;
            clearActions();
            addAction(GameActions.shrink());
            effect.start();
        }
        effect.update(delta);
        if (destroy && !effect.isPlaying()) {
            store.remove(this);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        Vector2 position = getActualPosition();
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        effect.draw(batch);
    }

    /**
     * Adds a ball that can interact with this cup.
     */
    public void setBall(String name) {
        ballName = name;
    }

    @Override
    public Parameters getParameters() {
        Parameters parameters = super.getParameters();
        parameters.put("ball", String.class, ballName);
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        super.setParameters(parameters);
        ballName = parameters.getValue("ball");
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
