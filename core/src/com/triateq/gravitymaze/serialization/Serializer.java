package com.triateq.gravitymaze.serialization;

import com.badlogic.gdx.Gdx;
import com.triateq.gravitymaze.utils.Context;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class Serializer implements JsonSerializer<Parameterizable>, JsonDeserializer<Parameterizable> {

    private static final String CLASS_FIELD = "class";
    private static final String VALUE_FIELD = "value";

    private final Context context;

    public Serializer(Context context) {
        this.context = context;
    }

    @Override
    public JsonElement serialize(Parameterizable parameterizable, Type typeOfSrc, JsonSerializationContext context) {
        Parameters parameters = getParameters(parameterizable);
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
            Gdx.app.error(getClass().getName(), e.toString(), e);
        }
        return null;
    }

    private Parameterizable deserializeValue(JsonObject valueObject, Class<?> clazz, JsonDeserializationContext context)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Parameterizable parameterizable = createParameterizable(clazz);
        Parameters parameters = getParameters(parameterizable);
        for (String name : parameters.nameSet()) {
            Class<?> type = parameters.getType(name);
            Object obj = context.deserialize(valueObject.get(name), type);
            parameters.put(name, type, obj);
        }
        setParameters(parameterizable, parameters);
        return parameterizable;
    }

    public static Parameters getParameters(Parameterizable parameterizable) {
        Parameters parameters = parameterizable.getParameters();
        parameters.putAll(getAnnotatedParameters(parameterizable));
        return parameters;
    }

    private static Parameters getAnnotatedParameters(Object obj) {
        Parameters parameters = new Parameters();
        for (Field field : obj.getClass().getDeclaredFields()) {
            Parameter parameter = field.getDeclaredAnnotation(Parameter.class);
            if (parameter == null) continue;
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                Class<?> type = field.getType();
                String name = parameter.name().isEmpty() ? field.getName() : parameter.name();
                parameters.put(name, type, value);
            } catch (IllegalAccessException e) {
                Gdx.app.error(Serializer.class.getName(), e.toString(), e);
            }
        }
        return parameters;
    }

    public static void setParameters(Parameterizable parameterizable, Parameters parameters) {
        setAnnotatedParameters(parameterizable, parameters);
        parameterizable.setParameters(parameters);
    }

    private static void setAnnotatedParameters(Object obj, Parameters parameters) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            Parameter parameter = field.getDeclaredAnnotation(Parameter.class);
            if (parameter == null) continue;
            try {
                field.setAccessible(true);
                String name = parameter.name().isEmpty() ? field.getName() : parameter.name();
                field.set(obj, parameters.getValue(name));
            } catch (IllegalAccessException e) {
                Gdx.app.error(Serializer.class.getName(), e.toString(), e);
            }
        }
    }

    private Parameterizable createParameterizable(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor(Context.class);
            return (Parameterizable) constructor.newInstance(context);
        } catch (NoSuchMethodException ignored) {
            constructor = clazz.getConstructor();
            return (Parameterizable) constructor.newInstance();
        }
    }
}
