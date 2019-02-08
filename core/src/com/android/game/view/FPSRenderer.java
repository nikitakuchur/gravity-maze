package com.android.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class FPSRenderer implements Renderer {

    private BitmapFont font;
    private float scale;
    private SpriteBatch spriteBatch;

    public FPSRenderer(BitmapFont font, float scale) {
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

        glyphLayout.setText(font, "FPS: " + Gdx.graphics.getFramesPerSecond());
        font.draw(spriteBatch, glyphLayout, 0, glyphLayout.height);

        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
