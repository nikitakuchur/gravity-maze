package com.majakkagames.gravitymaze.game.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.game.gameobjects.Maze;
import com.majakkagames.gravitymaze.game.gameobjects.Gravity;

public class Effect {

    private Color color = Color.BLACK.cpy();
    private int count = 32;
    private Vector2 position = Vector2.Zero.cpy();
    private float size = 10;
    private float speed = 200;
    private float delay = 1;
    private boolean useGravity;
    private Vector2 direction;

    private boolean playing;

    private final Maze maze;
    private final Gravity gravity;

    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final Timer timer = new Timer();

    private float currentDelay;
    private Color currentColor;

    public Effect(Level level) {
        GameObjectStore store = level.getGameObjectStore();
        maze = store.getAnyGameObjectOrThrow(Maze.class, () -> new IllegalStateException("Cannot find the maze object"));
        gravity = store.getAnyGameObjectOrThrow(Gravity.class, () -> new IllegalStateException("Cannot find the gravity object"));
    }

    public void start() {
        if (playing) return;
        particles.clear();
        for (int i = 0; i < count; i++) {
            particles.add(new Particle());
        }
        particles.forEach(particle -> {
            Vector2 randVector = Vector2.Y.cpy().setToRandomDirection().scl(random.nextFloat());
            particle.position.add(randVector.cpy().scl(maze.getCellSize() / 2));
            particle.velocity.add(randVector);
            if (direction != null) particle.velocity.add(direction);
            particle.size = (1 + random.nextFloat()) * size / 2;
        });
        playing = true;
        currentDelay = delay;
        currentColor = color;

        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                playing = false;
            }
        }, delay);
        timer.start();
    }

    public void update(float delta) {
        if (!playing) return;
        float speedCorrection = maze.getCellSize() / 100;
        particles.forEach(particle -> {
            particle.position.add(particle.velocity.cpy().scl(speed * speedCorrection * delta));
            if (useGravity) {
                particle.velocity.add(gravity.getGravityDirection().getDirection().scl(2 * delta));
            }
        });
        currentDelay = currentDelay - delta;
        currentColor.a = currentDelay;
        if (currentColor.a <= 0) currentColor.a = 0;
    }

    public void draw(Batch batch) {
        if (!playing) return;
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.translate(-maze.getWidth() / 2, -maze.getHeight() / 2, 0);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        currentColor = color;
        shapeRenderer.setColor(color);
        float cellSize = maze.getCellSize();
        float speedCorrection = cellSize / 100;
        particles.forEach(particle ->
                shapeRenderer.rect(
                        position.x * cellSize + particle.position.x + cellSize / 2,
                        position.y * cellSize + particle.position.y + cellSize / 2,
                        particle.size * speedCorrection, particle.size * speedCorrection));

        shapeRenderer.identity();
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    public Effect color(Color color) {
        this.color = color.cpy();
        return this;
    }

    public Effect count(int count) {
        this.count = count;
        return this;
    }

    public Effect position(Vector2 position) {
        this.position = position.cpy();
        return this;
    }

    public Effect size(float size) {
        this.size = size;
        return this;
    }

    public Effect speed(float speed) {
        this.speed = speed;
        return this;
    }

    public Effect delay(float delay) {
        this.delay = delay;
        return this;
    }

    public Effect useGravity() {
        this.useGravity = true;
        return this;
    }

    public Effect direction(Vector2 direction) {
        this.direction = direction.cpy();
        return this;
    }

    public boolean isPlaying() {
        return playing;
    }

    private static class Particle {
        private final Vector2 position = Vector2.Zero.cpy();
        private final Vector2 velocity = Vector2.Zero.cpy();
        private float size = 10;
    }
}
