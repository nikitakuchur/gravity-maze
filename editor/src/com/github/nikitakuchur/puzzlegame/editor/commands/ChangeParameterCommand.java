package com.github.nikitakuchur.puzzlegame.editor.commands;

import com.github.nikitakuchur.puzzlegame.game.entities.Parameterizable;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

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
        this.oldValue = parameterizable.getParameters().getValue(name);
        this.newValue = value;
    }

    @Override
    public void execute() {
        Parameters parameters = parameterizable.getParameters();
        parameters.put(name, type, newValue);
        parameterizable.setParameters(parameters);
    }

    @Override
    public void unexecute() {
        Parameters parameters = parameterizable.getParameters();
        parameters.put(name, type, oldValue);
        parameterizable.setParameters(parameters);
    }
}
