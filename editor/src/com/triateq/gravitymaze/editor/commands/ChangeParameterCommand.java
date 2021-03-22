package com.triateq.gravitymaze.editor.commands;

import com.triateq.puzzlecore.serialization.Parameterizable;
import com.triateq.puzzlecore.serialization.Parameters;
import com.triateq.puzzlecore.serialization.Serializer;

public class ChangeParameterCommand<T> implements Command {

    private final Parameterizable parameterizable;
    private final String name;
    private final Class<? extends T> type;
    private final T oldValue;
    private final T newValue;

    public ChangeParameterCommand(Parameterizable parameterizable, String name, Class<? extends T> type, T value) {
        this.parameterizable = parameterizable;
        this.name = name;
        this.type = type;
        this.oldValue = Serializer.getParameters(parameterizable).getValue(name);
        this.newValue = value;
    }

    @Override
    public void execute() {
        Parameters parameters = Serializer.getParameters(parameterizable);
        parameters.put(name, type, newValue);
        Serializer.setParameters(parameterizable, parameters);
    }

    @Override
    public void unexecute() {
        Parameters parameters = Serializer.getParameters(parameterizable);
        parameters.put(name, type, oldValue);
        Serializer.setParameters(parameterizable, parameters);
    }
}
