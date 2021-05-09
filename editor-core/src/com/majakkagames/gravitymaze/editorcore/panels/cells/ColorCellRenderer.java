package com.majakkagames.gravitymaze.editorcore.panels.cells;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ColorCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JPanel panel = new JPanel();
        com.badlogic.gdx.graphics.Color color = (com.badlogic.gdx.graphics.Color) value;
        panel.setBackground(new Color(color.r, color.g, color.b, color.a));
        return panel;
    }
}
