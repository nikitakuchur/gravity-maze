package com.android.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;

public class UIRenderer implements Renderer {

    private BitmapFont font;

    private FPSRenderer fpsRenderer;
    private ScoreRenderer scoreRenderer;

    public UIRenderer() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getWidth() / 16;
        font = generator.generateFont(parameter);

        fpsRenderer = new FPSRenderer(font, 0.5f);
        scoreRenderer = new ScoreRenderer(font, 1);

        generator.dispose();
    }

    @Override
    public void draw(Matrix4 projectionMatrix) {
        fpsRenderer.draw(projectionMatrix);
        scoreRenderer.draw(projectionMatrix);
    }

    @Override
    public void dispose() {
        font.dispose();
        font.dispose();
        fpsRenderer.dispose();
        scoreRenderer.dispose();
    }
}
