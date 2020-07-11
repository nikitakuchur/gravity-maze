package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

public interface Entity extends Json.Serializable, Disposable {

    Properties getProperties();

    void setProperties(Properties properties);

    @Override
    default void write(Json json) {
        Properties properties = getProperties();
        properties.nameSet().forEach(name -> json.writeValue(name, properties.<Object>getValue(name)));
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
