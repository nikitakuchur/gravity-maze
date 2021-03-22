package com.triateq.gravitymaze.editor.panels.accessors;

import com.triateq.puzzlecore.serialization.Parameters;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

public class BooleanFieldAccessor extends DefaultFieldAccessor<Boolean> {

    @Override
    public Boolean getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, false);
    }

    @Override
    public TableCellEditor getCellEditor(Class<? extends Boolean> type) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        return new DefaultCellEditor(checkBox);
    }
}
