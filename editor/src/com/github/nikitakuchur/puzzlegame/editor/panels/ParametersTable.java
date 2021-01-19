package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParametersTable extends JTable {

    public ParametersTable(TableModel tableModel) {
        super(tableModel);
    }

    @Override
    public TableCellRenderer getCellRenderer(int r, int c) {
        Class<?> type = getValueAt(r, c).getClass();
        if (type == Boolean.class) {
            return getDefaultRenderer(Boolean.class);
        }
        return super.getCellRenderer(r, c);
    }

    @Override
    public TableCellEditor getCellEditor(int r, int c) {
        Class<?> type = getValueAt(r, c).getClass();
        if (type == Color.class) {
            return new ColorCell();
        } else if (type == Boolean.class) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            return new DefaultCellEditor(checkBox);
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
