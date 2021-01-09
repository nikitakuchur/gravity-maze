package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.physics.FullCollider;
import com.github.nikitakuchur.puzzlegame.physics.GravityDirection;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalController;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;
import com.github.nikitakuchur.puzzlegame.physics.Physics;
import com.github.nikitakuchur.puzzlegame.serialization.Parameters;

public class Conveyor extends GameObject implements PhysicalObject {

    private final Texture texture = new Texture(Gdx.files.internal("game/conveyor/conveyor.png"), true);
    private final TextureRegion textureRegion = new TextureRegion(texture);

    private GravityDirection direction = GravityDirection.TOP;

    private PhysicalController physicalController;

    private GameObjectStore store;

    public Conveyor() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        store = level.getGameObjectStore();
        physicalController = new PhysicalController(this);
        physicalController.freeze();
        physicalController.setCollider(this::collider);
    }

    private boolean collider(PhysicalController controller, int x, int y, GravityDirection dir) {
        if (dir.getDirection().cpy().add(direction.getDirection()).equals(Vector2.Zero)) {
            return FullCollider.INSTANCE.checkCollision(controller, x, y, dir);
        }

        boolean hasCloseObject = store.getGameObjects(PhysicalObject.class).stream()
                .anyMatch(p -> p != this && isCloseObject(p.getPhysicalController().getPosition(), controller.getPosition()));
        if (hasCloseObject && !dir.getDirection().cpy().sub(direction.getDirection()).equals(Vector2.Zero)){
            return FullCollider.INSTANCE.checkCollision(controller, x, y, dir);
        }
        return false;
    }

    private boolean isCloseObject(Vector2 conveyorPosition, Vector2 objectPosition) {
        Vector2 delta = conveyorPosition.cpy().sub(objectPosition);
        Vector2 dir = direction.getDirection();
        return delta.cpy().scl(dir).len() < 1.f && delta.cpy().scl(dir.y, dir.x).len() == 0.f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 position = getPosition();

        PhysicalObject physicalObject = store.getGameObjects(PhysicalObject.class).stream()
                .filter(p -> p != this)
                .filter(p -> position.equals(p.getPhysicalController().getPosition()))
                .findAny()
                .orElse(null);

        if (physicalObject != null) {
            // In the first time we freeze the physical object
            if (!physicalObject.getPhysicalController().isFrozen()) {
                physicalObject.getPhysicalController().freeze();
                // We need this return statement for correct collision detection between objects in conveyors
                return;
            }
            // Move the object to the next cell
            movePhysicalObject(physicalObject);
        }
    }

    private void movePhysicalObject(PhysicalObject physicalObject) {
        Vector2 nextPosition = physicalObject.getPhysicalController().getPosition().add(direction.getDirection());

        if (!Physics.detectCollision(level, (int) nextPosition.x, (int) nextPosition.y, direction)) {
            ((GameObject) physicalObject).addAction(
                    Actions.sequence(
                            Actions.moveTo(nextPosition.x, nextPosition.y, 0.3f),
                            Actions.run(() -> unfreezeAction(physicalObject))
                    )
            );
        }
    }

    private void unfreezeAction(PhysicalObject physicalObject) {
        Vector2 position = physicalObject.getPhysicalController().getPosition();
        Conveyor conveyor = store.getGameObjects(Conveyor.class).stream()
                .filter(p -> position.equals(p.getPosition()))
                .findAny()
                .orElse(null);
        if (conveyor == null) {
            physicalObject.getPhysicalController().unfreeze();
            physicalObject.getPhysicalController().setVelocity(Vector2.Zero);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = getActualPosition();
        batch.setColor(getColor());
        Vector2 dir = direction.getDirection();
        double degrees = -Math.atan2(dir.x, dir.y) * 180.0 / Math.PI;
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), (float) degrees);
    }

    @Override
    public Parameters getParameters() {
        Parameters parameters =  super.getParameters();
        parameters.put("direction", String.class, direction.toString());
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        super.setParameters(parameters);
        direction = GravityDirection.valueOf(parameters.getValueOrDefault("direction", "TOP"));
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }
}
