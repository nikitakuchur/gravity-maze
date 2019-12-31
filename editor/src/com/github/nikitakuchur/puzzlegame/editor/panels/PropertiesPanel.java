package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PropertiesPanel extends JPanel {

    private DefaultTableModel tableModel = new PropertiesTableModel();
    private transient Properties properties = new Properties();

    private transient List<Runnable> propertiesListeners = new ArrayList<>();

    public PropertiesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(new JLabel("Properties:"));
        add(scrollPane);

        tableModel.addTableModelListener(tableModelEvent -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = tableModel.getValueAt(i, 0).toString();
                if (properties.getType(name) == String.class) {
                    String value = tableModel.getValueAt(i, 1).toString();
                    properties.put(name, properties.getType(name), "".equals(value) ? null : value);
                }
                if (properties.getType(name) == Color.class) {
                    try {
                        Color color = Color.valueOf(tableModel.getValueAt(i, 1).toString());
                        properties.put(name, properties.getType(name), color);
                    } catch (Exception e) {
                        properties.put(name, properties.getType(name), Color.WHITE);
                    }
                }
                propertiesListeners.forEach(Runnable::run);
            }
        });
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
        tableModel.setRowCount(0);
        properties.nameSet().forEach(name -> {
            if (properties.getType(name) == String.class) {
                String value = (String) properties.getValue(name);
                tableModel.addRow(new String[]{name, value == null ? "" : value});
            }
            if (properties.getType(name) == Color.class) {
                tableModel.addRow(new String[]{name, properties.getValue(name).toString()});
            }
        });
    }

    public void addPropertiesListener(Runnable runnable) {
        propertiesListeners.add(runnable);
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
