package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.github.nikitakuchur.puzzlegame.editor.utils.PropertiesUtils;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParametersPanel extends JPanel {

    private final DefaultTableModel tableModel = new PropertiesTableModel();
    private final JTable table = new PropertiesTable(tableModel);
    private transient Parameters parameters = new Parameters();

    private final transient List<Consumer<Parameters>> propertiesChangeListeners = new ArrayList<>();

    public ParametersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table.setDefaultEditor(Color.class, new ColorCell());

        add(new JLabel("Parameters:"));
        add(new JScrollPane(table));

        tableModel.addTableModelListener(tableModelEvent -> {
            if (tableModel.getRowCount() > 0) {
                propertiesChangeListeners.forEach(listener -> listener.accept(getParameters()));
            }
        });
    }

    public Parameters getParameters() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = tableModel.getValueAt(i, 0).toString();
            String value = tableModel.getValueAt(i, 1).toString();
            if (parameters.getType(name) == String.class) {
                parameters.put(name, String.class, "".equals(value) ? null : value);
            }
            if (parameters.getType(name) == Color.class) {
                parameters.put(name, Color.class, PropertiesUtils.parseColorOrDefault(value, Color.WHITE));
            }
            if (parameters.getType(name) == int.class) {
                parameters.put(name, int.class, PropertiesUtils.parseIntOrDefault(value, 0));
            }
        }
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }
        this.parameters = parameters;
        tableModel.setRowCount(0);
        parameters.nameSet().forEach(name -> {
            if (parameters.getType(name) == String.class) {
                String value = parameters.getValue(name);
                value = value == null ? "" : value;
                tableModel.addRow(new Object[]{name, value});
            }
            if (parameters.getType(name) == Color.class) {
                Color value = parameters.getValue(name);
                tableModel.addRow(new Object[]{name, value});
            }
            if (parameters.getType(name) == int.class) {
                String value = parameters.getValue(name).toString();
                tableModel.addRow(new Object[]{name, value});
            }
        });
    }

    public void addPropertiesChangeListener(Consumer<Parameters> consumer) {
        propertiesChangeListeners.add(consumer);
    }

    public void clearPropertiesChangeListeners() {
        propertiesChangeListeners.clear();
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
