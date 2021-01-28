package com.github.nikitakuchur.puzzlegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.nikitakuchur.puzzlegame.actors.Background;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuBackground extends Background {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final List<Figure> figures = new ArrayList<>();

    private float timer = 0.f;

    public MenuBackground(Color firstColor, Color secondColor) {
        super(firstColor, secondColor);
        figures.add(Figure.randomFigure(getColor()));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        figures.forEach(figure -> figure.update(delta));

        if (timer > 0.1f) {
            figures.add(Figure.randomFigure(getColor()));
            timer = 0.f;
        }
        timer += delta;

        figures.removeIf(Figure::isDead);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl20.glLineWidth(Gdx.graphics.getWidth() / 300f);
        figures.forEach(figure -> figure.draw(shapeRenderer));
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    private static class Figure {

        private static final Random random = new Random();

        protected float x;
        protected float y;

        protected float size;
        protected float rotation;
        protected final int segments;
        protected final Color color;

        protected float timer = 0;

        private final float rotationFactor;

        public Figure(float x, float y, float size, float rotation, int segments, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.rotation = rotation;
            this.segments = segments;
            this.color = color.cpy();

            rotationFactor = 1 - (random.nextFloat() * 2);
        }

        public void update(float delta) {
            timer += delta;

            color.a -= timer / 300;
            if (color.a < 0) color.a = 0;

            size -= (float) Gdx.graphics.getWidth() / 64 * delta;
            if (size < 0) size = 0;

            y -= (float) Gdx.graphics.getWidth() / 32 * delta;
            rotation += 15 * rotationFactor * delta;
        }

        public void draw(ShapeRenderer shapeRenderer) {
            shapeRenderer.setColor(color);
            shapeRenderer.ellipse(x - size / 2, y - size / 2, size, size, rotation, segments);
        }

        public boolean isDead() {
            return color.a == 0;
        }

        public static Figure randomFigure(Color color) {
            float c = (0.5f - random.nextFloat()) / 16;
            return new Figure(
                    random.nextFloat() * Gdx.graphics.getWidth() - (float) Gdx.graphics.getWidth() / 2,
                    random.nextFloat() * Gdx.graphics.getHeight() - (float) Gdx.graphics.getHeight() / 2,
                    (float) Gdx.graphics.getWidth() / 24 + random.nextFloat() * Gdx.graphics.getWidth() / 8,
                    random.nextFloat() * 360.f,
                    3 + Math.abs(random.nextInt() % 4), color.cpy().add(c, c, c, 0.8f));
        }
    }
}
