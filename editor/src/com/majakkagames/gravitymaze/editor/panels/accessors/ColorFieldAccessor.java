package com.majakkagames.gravitymaze.editor.panels.accessors;

import com.badlogic.gdx.graphics.Color;
import com.majakkagames.gravitymaze.editor.panels.cells.ColorCell;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

import javax.swing.table.TableCellEditor;

public class ColorFieldAccessor extends DefaultFieldAccessor<Color> {

    @Override
    public Color getValue(Parameters parameters, String name) {
        return parameters.getValueOrDefault(name, Color.WHITE.cpy());
    }

    @Override
    public TableCellEditor getCellEditor(Class<? extends Color> type) {
        return new ColorCell();
    }
}
