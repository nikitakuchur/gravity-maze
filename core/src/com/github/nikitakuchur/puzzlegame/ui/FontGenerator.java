package com.github.nikitakuchur.puzzlegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

// TODO: delete this after creation asset manager
public class FontGenerator {
    private static final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Roboto.ttf"));

    private FontGenerator() {
    }

    public static BitmapFont getFont(int size) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        return generator.generateFont(parameter);
    }

    public static void dispose() {
        generator.dispose();
    }
}
