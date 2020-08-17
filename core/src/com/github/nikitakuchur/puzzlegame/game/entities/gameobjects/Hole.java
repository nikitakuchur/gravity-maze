package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.nikitakuchur.puzzlegame.game.effects.Effect;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.utils.GameActions;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

public class Hole extends GameObject {

    private String ballName;

    private final Texture texture = new Texture(Gdx.files.internal("game/hole/hole.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    private Effect effect;

    public Hole() {
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

        GameObjectsManager manager = level.getGameObjectsManager();

        Ball ball = manager.find(Ball.class, ballName);
        if (ball != null && getPosition().equals(ball.getPosition())) {
            manager.remove(ball);
            ballName = null;
            effect.start();
        }
        effect.update(delta);
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
     * Adds a ball that can interact with this hole
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
