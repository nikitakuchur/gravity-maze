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

    public static Color getColor(JsonValue json, String name) {
        if (json.has(name)) {
            JsonValue color = json.get(name);
            if (color.isString()) {
                try {
                    return Color.valueOf(json.getString(name));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
