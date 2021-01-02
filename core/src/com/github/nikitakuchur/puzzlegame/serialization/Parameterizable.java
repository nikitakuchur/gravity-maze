package com.github.nikitakuchur.puzzlegame.serialization;

import com.badlogic.gdx.Gdx;
import com.google.gson.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * Every serializable class must implement this interface if it needs to save and load some fields.
 *
 * This interface is also used in the level editor. The user can edit fields obtained from parameters.
 */
public interface Parameterizable {

    /**
     * Creates new parameters and returns them.
     *
     * @return parameters
     */
    Parameters getParameters();

    /**
     * Sets the given parameters to the object.
     *
     * @param parameters the parameters
     */
    void setParameters(Parameters parameters);

    class Serializer implements JsonSerializer<Parameterizable>, JsonDeserializer<Parameterizable> {

        private static final String CLASS_FIELD = "class";
        private static final String VALUE_FIELD = "value";

        @Override
        public JsonElement serialize(Parameterizable parameterizable, Type typeOfSrc, JsonSerializationContext context) {
            Parameters parameters = parameterizable.getParameters();
            JsonObject root = new JsonObject();
            root.addProperty(CLASS_FIELD, parameterizable.getClass().getName());
            JsonObject valueObject = new JsonObject();
            parameters.nameSet().forEach(name -> valueObject.add(name, context.serialize(parameters.getValue(name))));
            root.add(VALUE_FIELD, valueObject);
            return root;
        }

        @Override
        public Parameterizable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject jsonObject = json.getAsJsonObject();
            try {
                Class<?> clazz = Class.forName(jsonObject.get(CLASS_FIELD).getAsString());
                JsonElement valueElement = jsonObject.get(VALUE_FIELD);
                if (valueElement.isJsonObject()) {
                    JsonObject valueObject = valueElement.getAsJsonObject();
                    return deserializeValue(valueObject, clazz, context);
                }
            } catch (Exception e) {
                Gdx.app.error("Parameterizable.Serializer", e.toString());
            }
            return null;
        }

        private Parameterizable deserializeValue(JsonObject valueObject, Class<?> clazz, JsonDeserializationContext context)
                throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            Parameterizable parameterizable = (Parameterizable) clazz.getConstructor().newInstance();
            Parameters parameters = parameterizable.getParameters();
            for (String name : parameters.nameSet()) {
                Class<?> type = parameters.getType(name);
                Object obj = context.deserialize(valueObject.get(name), type);
                parameters.put(name, type, obj);
            }
            parameterizable.setParameters(parameters);
            return parameterizable;
        }
    }
}
