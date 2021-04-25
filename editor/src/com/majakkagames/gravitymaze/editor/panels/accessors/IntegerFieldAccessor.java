package com.majakkagames.gravitymaze.editor.panels.accessors;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.editor.commands.ChangeParameterCommand;
import com.majakkagames.gravitymaze.editor.CommandHistory;
import com.majakkagames.gravitymaze.editor.utils.ParametersUtils;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

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
