package com.triateq.gravitymaze.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.triateq.gravitymaze.editor.panels.accessors.*;

import java.util.HashMap;

public class FieldAccessors {

    private static final HashMap<Class<?>, FieldAccessor<?>> FIELD_ACCESSORS = new HashMap<>();

    static {
        FIELD_ACCESSORS.put(String.class, new StringFieldAccessor());
        FIELD_ACCESSORS.put(Integer.class, new IntegerFieldAccessor());
        FIELD_ACCESSORS.put(int.class, new IntegerFieldAccessor());
        FIELD_ACCESSORS.put(Boolean.class, new BooleanFieldAccessor());
        FIELD_ACCESSORS.put(boolean.class, new BooleanFieldAccessor());
        FIELD_ACCESSORS.put(Color.class, new ColorFieldAccessor());
        FIELD_ACCESSORS.put(Enum.class, new EnumFieldAccessor());
    }

    @SuppressWarnings("unchecked")
    public static <T> FieldAccessor<T> getAccessor(Class<T> type) {
        for (Class<?> clazz : FIELD_ACCESSORS.keySet()) {
            if (clazz.isAssignableFrom(type)) {
                return (FieldAccessor<T>) FIELD_ACCESSORS.get(clazz);
            }
        }
        return null;
    }

    public static boolean isAllowedType(Class<?> type) {
        return FIELD_ACCESSORS.keySet().stream().anyMatch(clazz -> clazz.isAssignableFrom(type));
    }
}
