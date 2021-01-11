package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParametersTable extends JTable {

    public ParametersTable(TableModel tableModel) {
        super(tableModel);
    }

    @Override
    public TableCellEditor getCellEditor(int r, int c) {
        Class<?> type = getValueAt(r, c).getClass();
        if (type == Color.class) {
            return new ColorCell();
        } else if (type.isEnum()) {
            try {
                Method method = type.getDeclaredMethod("values");
                return new DefaultCellEditor(new JComboBox<>((Object[]) method.invoke(null)));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return super.getCellEditor(r, c);
    }
}
