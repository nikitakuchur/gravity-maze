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

    private final List<Pattern> patterns = new ArrayList<>();

    private static final int PATTERN_SIZE = 6;
    private float timer = 0.f;

    public MenuBackground(Color firstColor, Color secondColor) {
        super(firstColor, secondColor);
        patterns.add(new Pattern(getColor()));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        patterns.forEach(pattern -> pattern.update(delta));

        if (timer > 3.f) {
            patterns.add(new Pattern(getColor()));
            timer = 0.f;
            if (patterns.size() > PATTERN_SIZE) {
                patterns.remove(0);
            }
        }
        timer += delta;
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
        patterns.forEach(pattern -> pattern.draw(shapeRenderer));
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    private static class Pattern extends Figure {

        private static final Random random = new Random();
        private final List<Figure> figures = new ArrayList<>();

        private final float sizeFactor;
        private final float rotationFactor;

        private final float time;

        public Pattern(Color color) {
            super(
                    random.nextFloat() * Gdx.graphics.getWidth() - (float) Gdx.graphics.getWidth() / 2,
                    random.nextFloat() * Gdx.graphics.getHeight() - (float) Gdx.graphics.getHeight() / 2,
                    (float) Gdx.graphics.getWidth() / 16 + random.nextFloat() * Gdx.graphics.getWidth() / 16,
                    15 + random.nextFloat() * 30.f,
                    3 + Math.abs(random.nextInt() % 4), color.cpy());
            sizeFactor = 1.5f + random.nextFloat() / 2;
            rotationFactor = 15 + random.nextFloat() * 15.f;
            time = 0.5f + random.nextFloat();
        }

        @Override
        public void update(float delta) {
            figures.forEach(figure -> figure.update(delta));
            if (timer > time) {
                expand();
                timer = 0;
            }
            timer += delta;
        }

        public void expand() {
            float size = this.size;
            float rotation = this.rotation;
            if (!figures.isEmpty()) {
                Figure lastFigure = figures.get(figures.size() - 1);
                size = lastFigure.size * sizeFactor;
                rotation = lastFigure.rotation + rotationFactor;
            }
            figures.add(new Figure(x, y, size, rotation, segments, color.cpy()));
        }

        @Override
        public void draw(ShapeRenderer shapeRenderer) {
            for (Figure figure : figures) {
                figure.draw(shapeRenderer);
            }
        }
    }

    private static class Figure {
        protected final float x;
        protected final float y;

        protected float size;
        protected final float rotation;
        protected final int segments;
        protected final Color color;

        protected float timer = 0;

        public Figure(float x, float y, float size, float rotation, int segments, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.rotation = rotation;
            this.segments = segments;
            this.color = color;
        }

        public void update(float delta) {
            timer += delta;

            color.a -= timer / 300;
            if (color.a < 0) color.a = 0;

            size -= size / 40 * timer / 6;
            if (size < 0) size = 0;
        }

        public void draw(ShapeRenderer shapeRenderer) {
            shapeRenderer.setColor(color);
            shapeRenderer.ellipse(x - size / 2, y - size / 2, size, size, rotation, segments);
        }
    }
}
