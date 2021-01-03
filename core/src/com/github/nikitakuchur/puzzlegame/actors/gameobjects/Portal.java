package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.nikitakuchur.puzzlegame.effects.Effect;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;
import com.github.nikitakuchur.puzzlegame.utils.GameActions;
import com.github.nikitakuchur.puzzlegame.serialization.Parameters;

public class Portal extends GameObject {

    private String secondPortalName;

    private boolean locked;

    private final Texture texture = new Texture(Gdx.files.internal("game/portal/portal.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    private Effect effect;

    private GameObjectStore store;

    /**
     * Creates a new portal.
     */
    public Portal() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        addAction(Actions.forever(GameActions.bounceAndRotate()));
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        store = level.getGameObjectStore();
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
        Portal secondPortal = store.find(Portal.class, secondPortalName);
        Vector2 position = getPosition();

        if (secondPortal == null || detectCollision()) return;

        GameObject physicalObject = store.getGameObjects(PhysicalObject.class).stream()
                .map(GameObject.class::cast) // We can cast because GameObjectStore stores only game objects
                .filter(p -> position.equals(p.getPosition()))
                .findAny()
                .orElse(null);

        if (physicalObject != null && !locked && !secondPortal.locked) {
            physicalObject.setPosition(secondPortal.getPosition());
            locked = true;
            secondPortal.locked = true;

            effect.position(position)
                    .direction(((PhysicalObject) physicalObject).getPhysicalController().getVelocity().rotate(180))
                    .start();
            secondPortal.effect.position(secondPortal.getPosition())
                    .direction(((PhysicalObject) physicalObject).getPhysicalController().getVelocity())
                    .start();
        }

        if (physicalObject == null && secondPortal.isFree()) {
            locked = false;
            secondPortal.locked = false;
        }

        effect.update(delta);
    }

    private boolean detectCollision() {
        Portal secondPortal = store.find(Portal.class, secondPortalName);

        boolean firstBallDetected = store.getGameObjects(Ball.class).stream()
                .anyMatch(b -> getPosition().equals(b.getPosition()));
        boolean secondBallDetected = store.getGameObjects(Ball.class).stream()
                .anyMatch(b -> secondPortal.getPosition().equals(b.getPosition()));

        return firstBallDetected && secondBallDetected;
    }

    private boolean isFree() {
        for (Ball ball : store.getGameObjects(Ball.class)) {
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
     * Sets the second portal.
     *
     * @param name the second portal name
     */
    public void to(String name) {
        this.secondPortalName = name;
    }

    @Override
    public Parameters getParameters() {
        Parameters parameters = super.getParameters();
        parameters.put("to", String.class, secondPortalName);
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        super.setParameters(parameters);
        secondPortalName = parameters.getValue("to");
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
