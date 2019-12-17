package com.github.nikitakuchur.puzzlegame.editor.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PropertiesPanel extends JPanel {

    public PropertiesPanel() {
        DefaultTableModel tableModel = new PropertiesTableModel();

        JTable table = new JTable(tableModel);
        tableModel.addRow(new String[]{"name", "ball"});
        tableModel.addRow(new String[]{"x", "0"});
        tableModel.addRow(new String[]{"y", "0"});

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(150, 300));
        add(scrollPane);
    }

    private static class PropertiesTableModel extends DefaultTableModel {

        private String[] columnsHeader = new String[]{"Name", "Value"};

        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }

        @Override
        public String getColumnName(int i) {
            return columnsHeader[i];
        }

        @Override
        public int getColumnCount() {
            return columnsHeader.length;
        }
    }
}
