package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.gravitymaze.editor.commands.ChangeParameterCommand;
import com.triateq.gravitymaze.editor.commands.CommandHistory;
import com.triateq.gravitymaze.core.serialization.Parameterizable;
import com.triateq.gravitymaze.core.serialization.Parameters;

public class StringFieldAccessor extends DefaultFieldAccessor<String> {

    @Override
    public String getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, "");
    }

    @Override
    public void setValue(Parameterizable parameterizable, String name, Object value) {
        CommandHistory.getInstance().addAndExecute(
                new ChangeParameterCommand<>(parameterizable, name, String.class, "".equals(value) ? null : value)
        );
    }
}
