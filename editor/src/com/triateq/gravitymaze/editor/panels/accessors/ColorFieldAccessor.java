package com.triateq.gravitymaze.editor.panels.accessors;

import com.badlogic.gdx.graphics.Color;
import com.triateq.gravitymaze.serialization.Parameters;

public class ColorFieldAccessor extends DefaultFieldAccessor<Color> {

    @Override
    public Color getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, Color.WHITE.cpy());
    }
}
