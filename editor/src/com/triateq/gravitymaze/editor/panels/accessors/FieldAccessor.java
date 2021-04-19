package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.core.serialization.Parameters;

import javax.swing.table.TableCellEditor;

public interface FieldAccessor<T> {

    T getValue(Parameters parameters, String name);

    void setValue(GameObject gameObject, String name, Object value);

    TableCellEditor getCellEditor(Class<? extends T> type);
}
