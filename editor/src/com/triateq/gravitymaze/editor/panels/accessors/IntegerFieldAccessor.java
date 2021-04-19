package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.editor.commands.ChangeParameterCommand;
import com.triateq.gravitymaze.editor.commands.CommandHistory;
import com.triateq.gravitymaze.editor.utils.ParametersUtils;
import com.triateq.gravitymaze.core.serialization.Parameters;

public class IntegerFieldAccessor extends DefaultFieldAccessor<Integer> {

    @Override
    public Integer getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, 0);
    }

    @Override
    public void setValue(GameObject gameObject, String name, Object value) {
        CommandHistory.getInstance().addAndExecute(
                new ChangeParameterCommand<>(gameObject, name, Integer.class, ParametersUtils.parseIntOrDefault((String) value, 0))
        );
    }
}
