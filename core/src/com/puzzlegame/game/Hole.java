package com.puzzlegame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public class Hole extends GameObject {

    private List<Ball> balls = new ArrayList<>();

    private Texture texture = new Texture("game/hole.png");
    private TextureRegion textureRegion = new TextureRegion(texture);

    /**
     * Creates a new hole
     *
     * @param level the level
     */
    public Hole(Level level) {
        super(level);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (Ball ball : balls) {
            if (getX() == ball.getX() && getY() == ball.getY()) {
                getLevel().removeBall(ball);
                balls.remove(ball);
                break;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Map map = getLevel().getMap();
        batch.setColor(getColor());
        batch.draw(textureRegion, getX() * getWidth() - map.getWidth() / 2,
                getY() * getHeight() - map.getHeight() / 2,
                getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    /**
     * Adds a ball that can interact with this hole
     *
     * @param ball the ball
     */
    public void addBall(Ball ball) {
        balls.add(ball);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
