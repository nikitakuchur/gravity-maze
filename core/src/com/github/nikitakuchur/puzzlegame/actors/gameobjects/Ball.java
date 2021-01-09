package com.github.nikitakuchur.puzzlegame.actors.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalController;
import com.github.nikitakuchur.puzzlegame.physics.PhysicalObject;
import com.github.nikitakuchur.puzzlegame.level.Layer;

public class Ball extends GameObject implements PhysicalObject {

    private final Texture outlineTexture = new Texture(Gdx.files.internal("game/ball/outline.png"), true);
    private final Texture ballTexture = new Texture(Gdx.files.internal("game/ball/ball.png"), true);

    private final TextureRegion outlineTextureRegion = new TextureRegion(outlineTexture);
    private final TextureRegion ballTextureRegion = new TextureRegion(ballTexture);

    private PhysicalController physicalController;

    public Ball() {
        outlineTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        ballTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        physicalController = new PhysicalController(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Vector2 position = getActualPosition();
        batch.setColor(Color.WHITE);
        batch.draw(ballTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        batch.setColor(getColor());
        batch.draw(outlineTextureRegion, position.x, position.y, getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public Layer getLayer() {
        return Layer.FRONT;
    }

    @Override
    public PhysicalController getPhysicalController() {
        return physicalController;
    }

    @Override
    public void dispose() {
        outlineTexture.dispose();
        ballTexture.dispose();
    }
}
