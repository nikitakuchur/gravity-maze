package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.utils.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PropertiesPanel extends JPanel {

    private DefaultTableModel tableModel = new PropertiesTableModel();

    public PropertiesPanel() {
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(150, 300));
        add(scrollPane);
    }

    public void setProperties(Properties properties) {
        properties.nameSet().forEach(name -> {
            tableModel.addRow(new String[]{name, properties.getValue(name).toString()});
        });
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
