package com.github.nikitakuchur.puzzlegame.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.game.Level;
import com.github.nikitakuchur.puzzlegame.utils.JsonUtils;

public class Hole extends GameObject {

    private String ballName;

    private Texture texture = new Texture(Gdx.files.internal("game/hole.png"), true);
    private TextureRegion textureRegion = new TextureRegion(texture);

    public Hole() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }

    @Override
    public void act(Level level, float delta) {
        super.act(level, delta);
        if (ballName == null) return;
        Ball ball = level.findActor(ballName);

        if (getX() == ball.getX() && getY() == ball.getY()) {
            level.removeActor(ball);
            ballName = null;
        }
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
     * Adds a ball that can interact with this hole
     */
    public void setBall(String name) {
        ballName = name;
    }

    @Override
    public void restore(JsonValue json) {
        super.restore(json);
        ballName = JsonUtils.getString(json,"ball");
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
