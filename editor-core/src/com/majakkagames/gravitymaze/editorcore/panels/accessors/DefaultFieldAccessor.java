package com.majakkagames.gravitymaze.editorcore.panels.accessors;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.Parameters;
import com.majakkagames.gravitymaze.core.serialization.Serializer;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public abstract class DefaultFieldAccessor<T> implements FieldAccessor<T> {

    public T getValue(Parameters parameters, String name) {
        return parameters.getValue(name);
    }

    public void setValue(GameObject gameObject, String name, Object value) {
        Parameters parameters = Serializer.getParameters(gameObject);
        parameters.put(name, value.getClass(), value);
        Serializer.setParameters(gameObject, parameters);
    }

    @Override
    public TableCellEditor getCellEditor(Class<? extends T> type) {
        return null;
    }

    @Override
    public TableCellRenderer getCellRenderer(Class<? extends T> type) {
        return null;
    }
}
