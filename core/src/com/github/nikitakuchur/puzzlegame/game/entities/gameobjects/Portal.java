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
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public class Portal extends GameObject {

    private String secondPortalName;

    private boolean isLocked;

    private final Texture texture = new Texture(Gdx.files.internal("game/portal/portal.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    private Effect effect;

    private GameObjectsManager manager;

    /**
     * Creates a new portal
     */
    public Portal() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        addAction(Actions.forever(GameActions.bounceAndRotate()));
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        manager = level.getGameObjectsManager();
        effect = new Effect(level)
                .color(getColor())
                .size(5)
                .speed(300)
                .delay(0.5f)
                .useGravity();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Portal secondPortal = manager.find(Portal.class, secondPortalName);
        Vector2 position = getPosition();

        if (secondPortal == null || detectCollision()) return;

        Ball ball = manager.getGameObjects(Ball.class).stream()
                .filter(b -> position.equals(b.getPosition()))
                .findAny()
                .orElse(null);

        if (ball != null && !isLocked && !secondPortal.isLocked) {
            ball.setPosition(secondPortal.getPosition());
            isLocked = true;
            secondPortal.isLocked = true;

            effect.position(position)
                    .direction(ball.getPhysicalController().getVelocity().rotate(180))
                    .start();
            secondPortal.effect.position(secondPortal.getPosition())
                    .direction(ball.getPhysicalController().getVelocity())
                    .start();
        }

        if(ball == null && secondPortal.isFree()) {
            isLocked = false;
            secondPortal.isLocked = false;
        }

        effect.update(delta);
    }

    private boolean detectCollision() {
        Portal secondPortal = manager.find(Portal.class, secondPortalName);

        boolean isFirstBallDetected = manager.getGameObjects(Ball.class).stream()
                .anyMatch(b -> getPosition().equals(b.getPosition()));
        boolean isSecondBallDetected = manager.getGameObjects(Ball.class).stream()
                .anyMatch(b -> secondPortal.getPosition().equals(b.getPosition()));

        return isFirstBallDetected && isSecondBallDetected;
    }

    private boolean isFree() {
        for (Ball ball : manager.getGameObjects(Ball.class)) {
            if (getPosition().equals(ball.getPosition())) {
                return false;
            }
        }
        return true;
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
        secondPortalName = properties.getValue("to");
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
