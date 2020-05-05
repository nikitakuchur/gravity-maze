package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.github.nikitakuchur.puzzlegame.game.effects.Effect;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;

public class Spike extends GameObject {

    // TODO: Add texture
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Effect effect;

    @Override
    public void initialize(Level level) {
        super.initialize(level);
        effect = new Effect(level)
                .position(new Vector2(getX(), getY()))
                .count(64)
                .delay(0.8f)
                .useGravity();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        GameObjectsManager manager = level.getGameObjectsManager();
        manager.getGameObjects(Ball.class).forEach(ball -> {
            if (getX() == ball.getX() && getY() == ball.getY()) {
                manager.remove(ball);
                effect.color(ball.getColor()).start();
                // TODO: Game over
            }
        });
        effect.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        Vector2 position = getActualPosition();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(getColor());
        shapeRenderer.triangle(position.x, position.y,
                position.x + getWidth(), position.y,
                (position.x + position.x + getWidth()) / 2, position.y + getHeight());
        shapeRenderer.end();
        batch.begin();
        effect.draw(batch);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
