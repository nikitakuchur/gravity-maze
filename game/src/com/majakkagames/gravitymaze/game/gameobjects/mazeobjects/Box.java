package com.majakkagames.gravitymaze.game.gameobjects.mazeobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.game.physics.PhysicalController;
import com.majakkagames.gravitymaze.game.physics.PhysicalObject;

public class Box extends MazeObject implements PhysicalObject {

    private TextureRegion textureRegion;

    private PhysicalController<Box> physicalController;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        textureRegion = new TextureRegion(assetManager.get("textures/box/box.png", Texture.class));
        physicalController = new PhysicalController<>(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = maze.getActualCoords(getX(), getY());
        batch.setColor(getColor());
        batch.draw(textureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public int getLayer() {
        return 3;
    }

    @Override
    public PhysicalController<Box> getPhysicalController() {
        return physicalController;
    }
}
