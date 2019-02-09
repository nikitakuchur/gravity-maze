package com.android.game.view;

import com.android.game.model.Score;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class ScoreRenderer implements Renderer {

    private BitmapFont font;
    private float scale;
    private SpriteBatch spriteBatch;

    private Score score;

    public ScoreRenderer(Score score, BitmapFont font, float scale) {
        this.score = score;

        this.font = font;
        this.scale = scale;
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void draw(Matrix4 projectionMatrix) {
        font.getData().setScale(scale);
        spriteBatch.setProjectionMatrix(projectionMatrix);
        spriteBatch.begin();

        GlyphLayout glyphLayout = new GlyphLayout();

        glyphLayout.setText(font, "" + score.getValue());
        font.draw(spriteBatch, glyphLayout,
                (Gdx.graphics.getWidth() - glyphLayout.width) / 2,
                Gdx.graphics.getHeight() - (float) Gdx.graphics.getHeight() / 30);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
