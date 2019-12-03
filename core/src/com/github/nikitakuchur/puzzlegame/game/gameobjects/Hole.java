package com.github.nikitakuchur.puzzlegame.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.nikitakuchur.puzzlegame.game.Level;

import java.util.ArrayList;
import java.util.List;

public class Hole extends GameObject {

    private List<Ball> balls = new ArrayList<>();

    private Texture texture = new Texture(Gdx.files.internal("game/hole.png"), true);
    private TextureRegion textureRegion = new TextureRegion(texture);

    public Hole() {
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }

    @Override
    public void act(Level level, float delta) {
        super.act(level, delta);
        for (Ball ball : balls) {
            if (getX() == ball.getX() && getY() == ball.getY()) {
                level.removeActor(ball);
                balls.remove(ball);
                break;
            }
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
    public void addBall(Ball ball) {
        balls.add(ball);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
