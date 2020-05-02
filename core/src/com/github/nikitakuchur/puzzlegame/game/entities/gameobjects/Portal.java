package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.physics.Physics;
import com.github.nikitakuchur.puzzlegame.utils.GameActions;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public class Portal extends GameObject {

    private String secondPortalName;

    private boolean isLock;

    private final Texture texture = new Texture(Gdx.files.internal("game/portal.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    /**
     * Creates a new portal
     */
    public Portal() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        addAction(Actions.forever(GameActions.bounceAndRotate()));
    }

    @Override
    public void act(Level level, float delta) {
        super.act(level, delta);
        GameObjectsManager manager = level.getGameObjectsManager();
        Portal secondPortal = manager.find(Portal.class, secondPortalName);
        Vector2 position = new Vector2(getX(), getY());

        if (secondPortal != null && !isLock && !secondPortal.isLock) {
            for (Ball ball : manager.getGameObjects(Ball.class)) {
                if (position.x == ball.getX() && position.y == ball.getY()) {
                    ball.setPosition(secondPortal.getX(), secondPortal.getY());
                    secondPortal.isLock = true;
                    break;
                }
            }
        }

        if (isFree(manager)) {
            isLock = false;
        }
    }

    private boolean isFree(GameObjectsManager manager) {
        Vector2 position = new Vector2(getX(), getY());
        for (Ball ball : manager.getGameObjects(Ball.class)) {
            if (position.x == ball.getX() && position.y == ball.getY()) {
                return false;
            }
        }
        return true;
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
