package com.github.nikitakuchur.puzzlegame.editor.panels;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

class ButtonTextFieldCell extends AbstractCellEditor implements TableCellEditor {

    private JPanel panel = new JPanel();
    private JTextField textField = new JTextField();
    private JButton button = new JButton("...");

    public ButtonTextFieldCell() {
        this("");
    }

    public ButtonTextFieldCell(String txt) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        textField.setText(txt);
        panel.add(textField);

        button.setPreferredSize(new Dimension(16, 16));
        button.addActionListener(e -> {
            if (e.getSource() instanceof JButton) {
                JOptionPane.showConfirmDialog(panel, "you clicked a button", "Info", JOptionPane.DEFAULT_OPTION);
            }
        });
        panel.add(button);

        panel.add(Box.createHorizontalGlue());
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText();
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