package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.editor.commands.ChangeParameterCommand;
import com.triateq.gravitymaze.editor.commands.CommandHistory;
import com.triateq.gravitymaze.core.serialization.Parameters;

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
