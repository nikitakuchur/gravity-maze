package com.majakkagames.gravitymaze.editor.panels.accessors;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.editor.commands.ChangeParameterCommand;
import com.majakkagames.gravitymaze.editor.CommandHistory;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

public class StringFieldAccessor extends DefaultFieldAccessor<String> {

    @Override
    public String getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, "");
    }

    @Override
    public void setValue(GameObject gameObject, String name, Object value) {
        CommandHistory.getInstance().addAndExecute(
                new ChangeParameterCommand<>(gameObject, name, String.class, "".equals(value) ? null : value)
        );
    }
}
