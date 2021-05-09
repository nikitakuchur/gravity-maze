package com.majakkagames.gravitymaze.editorcore.panels.accessors;

import com.majakkagames.gravitymaze.core.serialization.Parameters;

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
