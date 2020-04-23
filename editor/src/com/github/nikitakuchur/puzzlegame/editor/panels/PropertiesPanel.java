package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.github.nikitakuchur.puzzlegame.editor.utils.PropertiesUtils;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PropertiesPanel extends JPanel {

    private final DefaultTableModel tableModel = new PropertiesTableModel();
    private final JTable table = new PropertiesTable(tableModel);
    private transient Properties properties = new Properties();

    private final transient List<Runnable> propertiesListeners = new ArrayList<>();

    public PropertiesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table.setDefaultEditor(Color.class, new ColorCell());

        add(new JLabel("Properties:"));
        add(new JScrollPane(table));

        tableModel.addTableModelListener(tableModelEvent -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                propertiesListeners.forEach(Runnable::run);
            }
        });
    }

    public Properties getProperties() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = tableModel.getValueAt(i, 0).toString();
            String value = tableModel.getValueAt(i, 1).toString();
            if (properties.getType(name) == String.class) {
                properties.put(name, String.class, "".equals(value) ? null : value);
            }
            if (properties.getType(name) == Color.class) {
                properties.put(name, Color.class, PropertiesUtils.parseColorOrDefault(value, Color.WHITE));
            }
            if (properties.getType(name) == int.class) {
                properties.put(name, int.class, PropertiesUtils.parseIntOrDefault(value, 0));
            }
        }
        return properties;
    }

    public void setProperties(Properties properties) {
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }
        this.properties = properties;
        tableModel.setRowCount(0);
        properties.nameSet().forEach(name -> {
            if (properties.getType(name) == String.class) {
                String value = (String) properties.getValue(name);
                value = value == null ? "" : value;
                tableModel.addRow(new Object[]{name, value});
            }
            if (properties.getType(name) == Color.class) {
                Color value = (Color) properties.getValue(name);
                tableModel.addRow(new Object[]{name, value});
            }
            if (properties.getType(name) == int.class) {
                String value = properties.getValue(name).toString();
                tableModel.addRow(new Object[]{name, value});
            }
        });
    }

    public void addPropertiesListener(Runnable runnable) {
        propertiesListeners.add(runnable);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        table.setEnabled(b);
    }

    private static class PropertiesTableModel extends DefaultTableModel {

        private final String[] columnsHeader = new String[]{"Name", "Value"};

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
