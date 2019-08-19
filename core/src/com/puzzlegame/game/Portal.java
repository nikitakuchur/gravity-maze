package com.puzzlegame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

public class Portal extends GameObject {

    private Portal secondPortal;

    private boolean isUsed;

    private Texture texture = new Texture("game/portal.png");
    private TextureRegion textureRegion = new TextureRegion(texture);

    /**
     * Creates a new portal
     */
    public Portal() {
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

        if (secondPortal == null) return;

        if (!isUsed) {
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
     * @param portal the portal
     */
    public void to(Portal portal) {
        this.secondPortal = portal;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
