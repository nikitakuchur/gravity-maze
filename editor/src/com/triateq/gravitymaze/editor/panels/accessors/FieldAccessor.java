package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.puzzlecore.serialization.Parameterizable;
import com.triateq.puzzlecore.serialization.Parameters;

import javax.swing.table.TableCellEditor;

public interface FieldAccessor<T> {

    T getValue(Parameters parameters, String name);

    void setValue(Parameterizable parameterizable, String name, Object value);

    TableCellEditor getCellEditor(Class<? extends T> type);
}
