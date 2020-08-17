package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.Gdx;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;
import com.google.gson.*;

import java.lang.reflect.Type;

public interface Parameterizable {

    Parameters getParameters();

    void setParameters(Parameters parameters);

    // TODO: Refactor this class, please
    class Serializer implements JsonSerializer<Parameterizable>, JsonDeserializer<Parameterizable> {

        @Override
        public JsonElement serialize(Parameterizable parameterizable, Type typeOfSrc, JsonSerializationContext context) {
            Parameters parameters = parameterizable.getParameters();
            JsonObject root = new JsonObject();
            root.addProperty("class", parameterizable.getClass().getName());
            parameters.nameSet().forEach(name -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", parameters.getType(name).getName());
                jsonObject.add("value", context.serialize(parameters.getValue(name)));
                root.add(name, jsonObject);
            });
            return root;
        }

        @Override
        public Parameterizable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            Parameters parameters = new Parameters();
            JsonObject jsonObject = json.getAsJsonObject();
            try {
                Class<?> clazz = Class.forName(jsonObject.get("class").getAsString());
                jsonObject.remove("class");
                jsonObject.keySet().forEach(key -> {
                    JsonObject property = jsonObject.get(key).getAsJsonObject();
                    try {
                        Class<?> type = Class.forName(property.get("type").getAsString());
                        Object value = context.deserialize(property.get("value"), type);
                        parameters.put(key, type, value);
                    } catch (ClassNotFoundException e) {
                        Gdx.app.error("Parameterizable.Serializer", e.toString());
                    }
                });
                Parameterizable parameterizable = (Parameterizable) clazz.getConstructor().newInstance();
                parameterizable.setParameters(parameters);
                return parameterizable;
            } catch (Exception e) {
                Gdx.app.error("Parameterizable.Serializer", e.toString());
            }
            return null;
        }
    }
}
