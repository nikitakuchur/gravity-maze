package com.android.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Matrix4;

public class UIRenderer {

    BitmapFont font;
    SpriteBatch spriteBatch;

    public UIRenderer() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        font = generator.generateFont(parameter);
        generator.dispose();

        spriteBatch = new SpriteBatch();
    }

    public void draw(Matrix4 projectionMatrix) {
        spriteBatch.setProjectionMatrix(projectionMatrix);
        spriteBatch.begin();

        GlyphLayout glyphLayout = new GlyphLayout();

        // FPS counter
        font.getData().setScale(0.5f);
        glyphLayout.setText(font, "FPS: " + Gdx.graphics.getFramesPerSecond());
        font.draw(spriteBatch, glyphLayout, 0, glyphLayout.height);

        font.getData().setScale(1f);
        glyphLayout.setText(font, "Hello World");
        font.draw(spriteBatch, glyphLayout,
                (Gdx.graphics.getWidth() - glyphLayout.width) / 2, Gdx.graphics.getHeight() - 32);

        spriteBatch.end();
    }

    /**
     * Releases all resources of this object.
     */
    public void dispose() {
        font.dispose();
        spriteBatch.dispose();
    }
}
