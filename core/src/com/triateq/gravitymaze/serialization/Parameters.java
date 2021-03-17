package com.triateq.gravitymaze.serialization;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Parameters {

    private final Map<String, Parameter<?>> propertyMap = new LinkedHashMap<>();

    public <T> void put(String name, Class<? extends T> type, T value) {
        propertyMap.put(name, new Parameter<>(type, value));
    }

    public void putAll(Parameters parameters) {
        propertyMap.putAll(parameters.propertyMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String name) {
        return (T) propertyMap.get(name).value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueOrDefault(String name, T defaultValue) {
        T value = (T) propertyMap.get(name).value;
        return value != null ? value : defaultValue;
    }

    public Class<?> getType(String name) {
        return propertyMap.get(name).type;
    }

    public Set<String> nameSet() {
        return propertyMap.keySet();
    }

    private static class Parameter<T>  {
        private final Class<? extends T> type;
        private final T value;

        public Parameter(Class<? extends T> type, T value) {
            this.type = type;
            this.value = value;
        }
    }
}
