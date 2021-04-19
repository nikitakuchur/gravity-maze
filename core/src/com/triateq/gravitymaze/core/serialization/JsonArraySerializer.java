package com.triateq.gravitymaze.core.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.triateq.gravitymaze.core.serialization.annotations.Transient;

import java.lang.reflect.Type;

class JsonArraySerializer<T> implements JsonSerializer<T[]> {
    @Override
    public JsonElement serialize(T[] source, Type type, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (T item : source) {
            // Skip transient game objects
            if (item.getClass().getAnnotation(Transient.class) == null) {
                jsonArray.add(context.serialize(item));
            }
        }
        return jsonArray;
    }
}
