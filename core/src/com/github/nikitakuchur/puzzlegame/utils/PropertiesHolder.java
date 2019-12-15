package com.github.nikitakuchur.puzzlegame.utils;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public interface PropertiesHolder extends Json.Serializable {

    Properties getProperties();

    void setProperties(Properties properties);

    @Override
    default void write(Json json) {
        Properties properties = getProperties();
        properties.nameSet().forEach(name -> json.writeValue(name, properties.getValue(name)));
    }

    @Override
    default void read(Json json, JsonValue jsonData) {
        Properties properties = getProperties();
        properties.nameSet().forEach(name -> {
            Object value = json.readValue(properties.getType(name), jsonData.get(name));
            properties.put(name, properties.getType(name), value);
        });
        setProperties(properties);
    }
}
