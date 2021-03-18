package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.gravitymaze.serialization.Parameterizable;
import com.triateq.gravitymaze.serialization.Parameters;

public interface FieldAccessor<T> {

    T getValue(Parameters parameters, String name);

    void setValue(Parameterizable parameterizable, String name, Object value);
}
