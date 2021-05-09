package com.majakkagames.gravitymaze.editorcore.panels.accessors;

import com.badlogic.gdx.graphics.Color;
import com.majakkagames.gravitymaze.core.serialization.Parameters;
import com.majakkagames.gravitymaze.editorcore.panels.cells.ColorCellEditor;
import com.majakkagames.gravitymaze.editorcore.panels.cells.ColorCellRenderer;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ColorFieldAccessor extends DefaultFieldAccessor<Color> {

    @Override
    public Color getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, Color.WHITE.cpy());
    }

    @Override
    public TableCellEditor getCellEditor(Class<? extends Color> type) {
        return new ColorCellEditor();
    }

    @Override
    public TableCellRenderer getCellRenderer(Class<? extends Color> type) {
        return new ColorCellRenderer();
    }
}
