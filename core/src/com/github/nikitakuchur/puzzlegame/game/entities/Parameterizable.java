package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

public interface Parameterizable extends Json.Serializable {

    Parameters getParameters();

    void setParameters(Parameters parameters);

    @Override
    default void write(Json json) {
        Parameters parameters = getParameters();
        parameters.nameSet().forEach(name -> json.writeValue(name, parameters.<Object>getValue(name)));
    }

    @Override
    default void read(Json json, JsonValue jsonData) {
        Parameters parameters = getParameters();
        parameters.nameSet().forEach(name -> {
            Object value = json.readValue(parameters.getType(name), jsonData.get(name));
            parameters.put(name, parameters.getType(name), value);
        });
        setParameters(parameters);
    }
}
