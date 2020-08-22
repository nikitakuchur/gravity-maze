package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.github.nikitakuchur.puzzlegame.editor.utils.ParametersUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

class ColorCell extends AbstractCellEditor implements TableCellEditor {

    private final JPanel panel = new JPanel();
    private final JTextField textField = new JTextField();

    public ColorCell() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(textField);

        JButton button = new JButton("...");
        button.setPreferredSize(new Dimension(16, 16));
        button.addActionListener(e -> {
            if (e.getSource() instanceof JButton) {
                String hex = "0x" + textField.getText().substring(0, 6);
                java.awt.Color color = JColorChooser.
                        showDialog(panel, "Choose a color", java.awt.Color.decode(hex));
                if (color != null) {
                    textField.setText(toHex(color));
                }
                stopCellEditing();
            }
        });
        panel.add(button);
        panel.add(Box.createHorizontalGlue());
    }

    private String toHex(java.awt.Color color) {
        float[] rgba = color.getComponents(new float[4]);
        Color c = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
        return c.toString();
    }

    @Override
    public Object getCellEditorValue() {
        return ParametersUtils.parseColorOrDefault(textField.getText(), Color.WHITE);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            textField.setText(value.toString());
            return panel;
        } else {
            return null;
        }
    }
}