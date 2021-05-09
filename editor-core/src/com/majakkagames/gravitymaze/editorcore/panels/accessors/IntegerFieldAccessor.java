package com.majakkagames.gravitymaze.editorcore.panels.accessors;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.Parameters;
import com.majakkagames.gravitymaze.editorcore.utils.ParametersUtils;

public class IntegerFieldAccessor extends DefaultFieldAccessor<Integer> {

    @Override
    public Integer getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, 0);
    }

    @Override
    public void setValue(GameObject gameObject, String name, Object value) {
        super.setValue(gameObject, name, ParametersUtils.parseIntOrDefault((String) value, 0));
    }
}
