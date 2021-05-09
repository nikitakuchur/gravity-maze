package com.majakkagames.gravitymaze.editorcore.panels.accessors;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public interface FieldAccessor<T> {

    T getValue(Parameters parameters, String name);

    void setValue(GameObject gameObject, String name, Object value);

    TableCellEditor getCellEditor(Class<? extends T> type);

    TableCellRenderer getCellRenderer(Class<? extends T> type);
}
