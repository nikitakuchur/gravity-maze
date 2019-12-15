package com.github.nikitakuchur.puzzlegame.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Properties {

    private Map<String, Property> propertyMap = new HashMap<>();

    public void put(String name, Class<?> type, Object value) {
        propertyMap.put(name, new Property(type, value));
    }

    public Object getValue(String name) {
        return propertyMap.get(name).value;
    }

    public Object getValueOrDefault(String name, Object defaultValue) {
        Object value = propertyMap.get(name).value;
        return value != null ? value : defaultValue;
    }

    public Class<?> getType(String name) {
        return propertyMap.get(name).type;
    }

    public Set<String> nameSet() {
        return propertyMap.keySet();
    }

    private static class Property {
        private Class<?> type;
        private Object value;

        public Property(Class<?> type, Object value) {
            this.type = type;
            this.value = value;
        }
    }
}
