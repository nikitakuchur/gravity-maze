package com.triateq.gravitymaze.editor.panels;

import com.triateq.gravitymaze.editor.panels.accessors.FieldAccessor;
import com.triateq.gravitymaze.editor.panels.accessors.FieldAccessors;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class ParametersTable extends JTable {

    public ParametersTable(TableModel tableModel) {
        super(tableModel);
    }

    @Override
    public TableCellRenderer getCellRenderer(int r, int c) {
        Class<?> type = getValueAt(r, c).getClass();
        return getDefaultRenderer(type);
    }

    @Override
    public TableCellEditor getCellEditor(int r, int c) {
        Class<?> type = getValueAt(r, c).getClass();
        TableCellEditor editor = getAccessorCellEditor(type);
        if (editor != null) {
            return editor;
        }
        return super.getCellEditor(r, c);
    }

    private <T> TableCellEditor getAccessorCellEditor(Class<T> type) {
        FieldAccessor<T> accessor = FieldAccessors.getAccessor(type);
        if (accessor != null) {
            return accessor.getCellEditor(type);
        }
        return null;
    }
}
