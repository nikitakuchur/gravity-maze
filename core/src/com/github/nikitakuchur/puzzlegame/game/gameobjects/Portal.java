package com.github.nikitakuchur.puzzlegame.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.github.nikitakuchur.puzzlegame.game.Level;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public class Portal extends GameObject {

    private String secondPortalName;

    private boolean isUsed;

    private Texture texture = new Texture(Gdx.files.internal("game/portal.png"), true);
    private TextureRegion textureRegion = new TextureRegion(texture);

    /**
     * Creates a new portal
     */
    public Portal() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        addAction(Actions.repeat(
                RepeatAction.FOREVER, Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(-40, 1, Interpolation.linear),
                                Actions.scaleTo(0.9f, 0.9f, 1, Interpolation.smooth)),
                        Actions.parallel(
                                Actions.rotateBy(-40, 1, Interpolation.linear),
                                Actions.scaleTo(1, 1, 1, Interpolation.smooth)
                        ))));
    }

    @Override
    public void act(Level level, float delta) {
        super.act(level, delta);

        if (secondPortalName == null) return;
        Portal secondPortal = level.findActor(secondPortalName);

        if (!isUsed && !secondPortal.isUsed) {
            for (Ball ball : level.getGameObjects(Ball.class)) {
                if (getX() == ball.getX() && getY() == ball.getY()) {
                    ball.setPosition(secondPortal.getX(), secondPortal.getY());
                    isUsed = true;
                    secondPortal.isUsed = true;
                    return;
                }
            }
        }

        // If the portal is free
        for (Ball ball : level.getGameObjects(Ball.class)) {
            if ((int) getX() == (int) ball.getX() && (int) getY() == (int) ball.getY()) {
                return;
            }
        }
        isUsed = false;
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
     * Sets the second portal
     *
     * @param name the second portal name
     */
    public void to(String name) {
        this.secondPortalName = name;
    }

    @Override
    public Properties getProperties() {
        Properties properties = super.getProperties();
        properties.put("to", String.class, secondPortalName);
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        secondPortalName = (String) properties.getValue("to");
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
