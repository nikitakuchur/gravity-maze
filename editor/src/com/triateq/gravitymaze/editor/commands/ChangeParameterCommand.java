package com.triateq.gravitymaze.editor.commands;

import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.core.serialization.Parameters;
import com.triateq.gravitymaze.core.serialization.annotations.Serializer;

public class ChangeParameterCommand<T> implements Command {

    private final GameObject gameObject;
    private final String name;
    private final Class<? extends T> type;
    private final T oldValue;
    private final T newValue;

    public ChangeParameterCommand(GameObject gameObject, String name, Class<? extends T> type, T value) {
        this.gameObject = gameObject;
        this.name = name;
        this.type = type;
        this.oldValue = Serializer.getParameters(gameObject).getValue(name);
        this.newValue = value;
    }

    @Override
    public void execute() {
        Parameters parameters = Serializer.getParameters(gameObject);
        parameters.put(name, type, newValue);
        Serializer.setParameters(gameObject, parameters);
    }

    @Override
    public void unexecute() {
        Parameters parameters = Serializer.getParameters(gameObject);
        parameters.put(name, type, oldValue);
        Serializer.setParameters(gameObject, parameters);
    }
}
