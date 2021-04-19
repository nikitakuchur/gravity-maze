package com.triateq.gravitymaze.game.gameobjects.mazeobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.game.physics.PhysicalController;
import com.triateq.gravitymaze.game.physics.PhysicalObject;

public class Ball extends MazeObject implements PhysicalObject {

    private TextureRegion outlineTextureRegion;
    private TextureRegion ballTextureRegion;

    private PhysicalController<Ball> physicalController;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        outlineTextureRegion = new TextureRegion(assetManager.get("textures/ball/outline.png", Texture.class));
        ballTextureRegion = new TextureRegion(assetManager.get("textures/ball/ball.png", Texture.class));
        physicalController = new PhysicalController<>(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = maze.getActualCoords(getX(), getY());
        batch.setColor(Color.WHITE);
        batch.draw(ballTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(getColor());
        batch.draw(outlineTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public int getLayer() {
        return 3;
    }

    @Override
    public PhysicalController<Ball> getPhysicalController() {
        return physicalController;
    }
}
