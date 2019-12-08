package com.github.nikitakuchur.puzzlegame.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;

public class JsonUtils {

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Integer getInt(JsonValue json, String name) {
        if (json.has(name)) {
            JsonValue n = json.get(name);
            if (n.isNumber()) {
                return json.getInt(name);
            }
        }
        return null;
    }

    public static String getString(JsonValue json, String name) {
        if (json.has(name)) {
            JsonValue str = json.get(name);
            if (str.isString()) {
                return json.getString(name);
            }
        }
        return null;
    }

    public static Color getColor(JsonValue json, String name) {
        String color = getString(json, name);
        if (color == null) return null;
        try {
            return Color.valueOf(color);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
