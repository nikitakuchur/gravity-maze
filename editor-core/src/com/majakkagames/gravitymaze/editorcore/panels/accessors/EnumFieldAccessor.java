package com.majakkagames.gravitymaze.editorcore.panels.accessors;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumFieldAccessor extends DefaultFieldAccessor<Enum<?>> {

    @Override
    public TableCellEditor getCellEditor(Class<? extends Enum<?>> type) {
        try {
            Method method = type.getDeclaredMethod("values");
            return new DefaultCellEditor(new JComboBox<>((Object[]) method.invoke(null)));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
