package com.triateq.gravitymaze.actors.gameobjects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.triateq.gravitymaze.level.Level;
import com.triateq.gravitymaze.physics.FullCollider;
import com.triateq.puzzlecore.serialization.Parameter;
import com.triateq.gravitymaze.utils.Direction;
import com.triateq.gravitymaze.physics.PhysicalController;
import com.triateq.gravitymaze.physics.PhysicalObject;
import com.triateq.gravitymaze.physics.Physics;
import com.triateq.puzzlecore.game.Context;

import java.util.Optional;

public class Conveyor extends GameObject implements PhysicalObject {

    private static final float ANIMATION_SPEED = 20.f;
    private static final int FRAMES_NUMBER = 16;

    private final TextureRegion textureRegion;

    private Direction direction = Direction.TOP;

    private PhysicalController physicalController;

    private GameObjectStore store;

    private float frame;

    public Conveyor(Context context) {
        AssetManager assetManager = context.getAssetManager();
        textureRegion = new TextureRegion(assetManager.get("textures/conveyor/conveyor.png", Texture.class));
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        store = level.getGameObjectStore();
        physicalController = new PhysicalController(this);
        physicalController.freeze();
        physicalController.setCollider(this::collider);
    }

    private boolean collider(PhysicalController controller, int x, int y, Direction dir) {
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

    @SuppressWarnings("SuspiciousNameCombination")
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
                // We need to skip move method for correct collision detection between objects in conveyors
            } else {
                // Move the object to the next cell
                movePhysicalObject(physicalObject);
            }
        }

        frame += ANIMATION_SPEED * delta;
        if (frame >= FRAMES_NUMBER) frame = 0;
    }

    private void movePhysicalObject(PhysicalObject physicalObject) {
        Vector2 nextPosition = physicalObject.getPhysicalController().getPosition().add(direction.getDirection());
        boolean hasNextObject = store.getGameObjects(PhysicalObject.class).stream()
                .filter(p -> !p.getPhysicalController().isMoving() && !p.getPhysicalController().isFrozen())
                .anyMatch(p -> p.getPhysicalController().getPosition().equals(nextPosition));

        if (!hasNextObject && !Physics.detectCollision(level, (int) nextPosition.x, (int) nextPosition.y, direction)) {
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

    @Parameter
    public Direction getDirection() {
        return direction;
    }

    @Parameter
    public void setDirection(Direction direction) {
        this.direction = Optional.ofNullable(direction).orElse(Direction.TOP);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = getActualPosition();
        batch.setColor(getColor());
        Vector2 dir = direction.getDirection();
        double degrees = -Math.atan2(dir.x, dir.y) * 180.0 / Math.PI;
        textureRegion.setRegion(0, (int) frame * 16, 512, 512);
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), (float) degrees);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }
}
