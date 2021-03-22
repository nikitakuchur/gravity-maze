package com.triateq.puzzlecore.serialization;

import com.badlogic.gdx.Gdx;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.triateq.puzzlecore.game.Context;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Parameters parameters = getAnnotatedFieldParameters(parameterizable);
        parameters.putAll(getAnnotatedMethodParameters(parameterizable));
        parameters.putAll(parameterizable.getParameters());
        return parameters;
    }

    private static Parameters getAnnotatedFieldParameters(Object obj) {
        Parameters parameters = new Parameters();
        for (Field field : getAllFields(obj.getClass())) {
            Parameter parameter = field.getAnnotation(Parameter.class);
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

    private static Parameters getAnnotatedMethodParameters(Object obj) {
        Parameters parameters = new Parameters();
        for (Method method : getAllMethods(obj.getClass())) {
            Parameter parameter = method.getAnnotation(Parameter.class);
            if (parameter == null || !isGetter(method)) continue;
            try {
                method.setAccessible(true);
                Object value = method.invoke(obj);
                Class<?> type = method.getReturnType();
                String name = parameter.name().isEmpty() ? convertMethodName(method.getName()) : parameter.name();
                parameters.put(name, type, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Gdx.app.error(Serializer.class.getName(), e.toString(), e);
            }
        }
        return parameters;
    }

    public static void setParameters(Parameterizable parameterizable, Parameters parameters) {
        setAnnotatedFieldParameters(parameterizable, parameters);
        setAnnotatedMethodParameters(parameterizable, parameters);
        parameterizable.setParameters(parameters);
    }

    private static void setAnnotatedFieldParameters(Object obj, Parameters parameters) {
        for (Field field : getAllFields(obj.getClass())) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter == null) continue;
            try {
                field.setAccessible(true);
                String name = parameter.name().isEmpty() ? field.getName() : parameter.name();
                Object value = parameters.getValue(name);
                if (value != null) {
                    field.set(obj, value);
                }
            } catch (IllegalAccessException e) {
                Gdx.app.error(Serializer.class.getName(), e.toString(), e);
            }
        }
    }

    private static void setAnnotatedMethodParameters(Object obj, Parameters parameters) {
        for (Method method : getAllMethods(obj.getClass())) {
            Parameter parameter = method.getAnnotation(Parameter.class);
            if (parameter == null || !isSetter(method)) continue;
            try {
                method.setAccessible(true);
                String name = parameter.name().isEmpty() ? convertMethodName(method.getName()) : parameter.name();
                Object value = parameters.getValue(name);
                if (value != null) {
                    method.invoke(obj, value);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                Gdx.app.error(Serializer.class.getName(), e.toString(), e);
            }
        }
    }

    private static boolean isSetter(Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    private static boolean isGetter(Method method) {
        return !method.getReturnType().equals(Void.TYPE);
    }

    private static String convertMethodName(String name) {
        if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        }
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        Class<?> c = clazz;
        while (c != null) {
            result.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return result;
    }

    private static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> result = new ArrayList<>();
        Class<?> c = clazz;
        while (c != null) {
            result.addAll(Arrays.asList(c.getDeclaredMethods()));
            c = c.getSuperclass();
        }
        return result;
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
