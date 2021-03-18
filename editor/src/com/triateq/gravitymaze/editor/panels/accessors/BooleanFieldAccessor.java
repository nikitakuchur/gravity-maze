package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.gravitymaze.serialization.Parameters;

public class BooleanFieldAccessor extends DefaultFieldAccessor<Boolean> {

    @Override
    public Boolean getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, false);
    }
}
