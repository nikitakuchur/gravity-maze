package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.badlogic.gdx.graphics.Color;

public class PropertiesUtils {

    private PropertiesUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Color parseColorOrDefault(String value, Color defaultValue) {
        try {
            return Color.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
