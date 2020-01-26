package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class PropertiesTable extends JTable {

    public PropertiesTable(TableModel tableModel) {
        super(tableModel);
    }

    @Override
    public TableCellEditor getCellEditor(int r, int c) {
        Class<?> type = getValueAt(r, c).getClass();
        if (type == Color.class) {
            return new ColorCell();
        }
        return super.getCellEditor(r, c);
    }
}
