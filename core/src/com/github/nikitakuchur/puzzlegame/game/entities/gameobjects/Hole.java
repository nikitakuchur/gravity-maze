package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.utils.GameActions;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public class Hole extends GameObject {

    private String ballName;

    private final Texture texture = new Texture(Gdx.files.internal("game/hole.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    public Hole() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        addAction(Actions.forever(Actions.timeScale(0.4f, GameActions.bounceAndRotate())));
    }

    @Override
    public void act(Level level, float delta) {
        super.act(level, delta);

        GameObjectsManager manager = level.getGameObjectsManager();

        if (ballName == null) return;
        Ball ball = manager.find(Ball.class, ballName);

        if (getX() == ball.getX() && getY() == ball.getY()) {
            manager.remove(ball);
            ballName = null;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        batch.draw(textureRegion, getX() * getWidth() - getParent().getWidth() / 2,
                getY() * getHeight() - getParent().getHeight() / 2,
                getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    /**
     * Adds a ball that can interact with this hole
     */
    public void setBall(String name) {
        ballName = name;
    }

    @Override
    public Properties getProperties() {
        Properties properties = super.getProperties();
        properties.put("ball", String.class, ballName);
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        ballName = (String) properties.getValue("ball");
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
