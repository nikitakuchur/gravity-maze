package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.gravitymaze.editor.commands.ChangeParameterCommand;
import com.triateq.gravitymaze.editor.commands.CommandHistory;
import com.triateq.gravitymaze.editor.utils.ParametersUtils;
import com.triateq.gravitymaze.serialization.Parameterizable;
import com.triateq.gravitymaze.serialization.Parameters;

public class IntegerFieldAccessor extends DefaultFieldAccessor<Integer> {

    @Override
    public Integer getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, 0);
    }

    @Override
    public void setValue(Parameterizable parameterizable, String name, Object value) {
        CommandHistory.getInstance().addAndExecute(
                new ChangeParameterCommand<>(parameterizable, name, Integer.class, ParametersUtils.parseIntOrDefault((String) value, 0))
        );
    }
}
