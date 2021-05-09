package com.majakkagames.gravitymaze.editorcore.panels.accessors;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

public class StringFieldAccessor extends DefaultFieldAccessor<String> {

    @Override
    public String getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, "");
    }

    @Override
    public void setValue(GameObject gameObject, String name, Object value) {
        super.setValue(gameObject, name, "".equals(value) ? null : value);
    }
}
