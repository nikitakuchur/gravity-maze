package com.puzzlegame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

public class Portal extends GameObject {

    private final Level level;

    private Portal portal;

    private boolean isUsed;

    private Texture texture = new Texture("game/portal.png");
    private TextureRegion textureRegion = new TextureRegion(texture);

    /**
     * Creates a new portal
     *
     * @param level the level
     */
    public Portal(Level level) {
        this.level = level;
        setWidth(100);
        setHeight(100);

        addAction(Actions.repeat(
                RepeatAction.FOREVER, Actions.sequence(
                        Actions.parallel(
                            Actions.rotateBy(-40, 1, Interpolation.linear),
                            Actions.scaleTo(0.9f,0.9f, 1, Interpolation.smooth)),
                        Actions.parallel(
                            Actions.rotateBy(-40, 1, Interpolation.linear),
                            Actions.scaleTo(1,1, 1, Interpolation.smooth)
                        ))));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (portal == null) {
            return;
        }

        if (!isUsed) {
            for (Ball ball : level.getBalls()) {
                if (getX() == ball.getX() && getY() == ball.getY()) {
                    ball.setPosition(portal.getX(), portal.getY());
                    isUsed = true;
                    portal.isUsed = true;
                    return;
                }
            }
        }

        // If the portal is free
        for (Ball ball : level.getBalls()) {
            if ((int) getX() == (int) ball.getX() && (int) getY() == (int) ball.getY()) {
                break;
            }
        }
        isUsed = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX() * getWidth() - level.getMap().getWidth() / 2,
                getY() * getHeight() - level.getMap().getHeight() / 2,
                getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    /**
     * Sets the second portal
     *
     * @param portal the portal
     */
    public void to(Portal portal) {
        this.portal = portal;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
