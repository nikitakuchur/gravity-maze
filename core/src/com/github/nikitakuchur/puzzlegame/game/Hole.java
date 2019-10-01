package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class Hole extends GameObject {

    private List<Ball> balls = new ArrayList<>();

    private Texture texture = new Texture("game/hole.png");
    private TextureRegion textureRegion = new TextureRegion(texture);

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
