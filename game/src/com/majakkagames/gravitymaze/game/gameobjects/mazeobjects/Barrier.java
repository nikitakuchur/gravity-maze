package com.majakkagames.gravitymaze.game.gameobjects.mazeobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.game.physics.EmptyCollider;
import com.majakkagames.gravitymaze.game.physics.FullCollider;
import com.majakkagames.gravitymaze.game.physics.PhysicalController;
import com.majakkagames.gravitymaze.game.physics.PhysicalObject;
import com.majakkagames.gravitymaze.core.serialization.annotations.Parameter;

public class Barrier extends MazeObject implements Switchable, PhysicalObject {

    private TextureRegion textureRegion;

    @Parameter
    private boolean opened;
    @Parameter(name = "switch")
    private String switchName;

    private PhysicalController<Barrier> physicalController;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        textureRegion = new TextureRegion(assetManager.get("textures/box/box.png", Texture.class));
        physicalController = new PhysicalController<>(this);
        physicalController.freeze();
        if (opened) {
            physicalController.setCollider(EmptyCollider.INSTANCE);
        } else {
            physicalController.setCollider(FullCollider.INSTANCE);
        }
    }

    @Override
    public void onSwitch() {
        if (opened) {
            physicalController.setCollider(FullCollider.INSTANCE);
            clearActions();
            addAction(Actions.fadeIn(0.1f));
        } else {
            physicalController.setCollider(EmptyCollider.INSTANCE);
            clearActions();
            addAction(Actions.fadeOut(0.1f));
        }
        opened = !opened;
    }

    @Override
    public String getSwitch() {
        return switchName;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = opened ? getColor().cpy().mul(1, 1, 1, 0) : getColor().cpy().add(0, 0, 0, 1);
        batch.setColor(color);
        Vector2 position = maze.getActualCoords(getX(), getY());
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX() / 2, getScaleY() / 2, getRotation());
    }

    @Override
    public int getLayer() {
        return 4;
    }

    @Override
    public PhysicalController<Barrier> getPhysicalController() {
        return physicalController;
    }
}
