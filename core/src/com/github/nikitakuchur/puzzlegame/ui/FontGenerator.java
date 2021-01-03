package com.github.nikitakuchur.puzzlegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;

public class FontGenerator {
    private static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Roboto.ttf"));

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
