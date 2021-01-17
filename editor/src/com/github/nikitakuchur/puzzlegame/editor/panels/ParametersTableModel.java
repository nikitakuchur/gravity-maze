package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.github.nikitakuchur.puzzlegame.editor.commands.ChangeParameterCommand;
import com.github.nikitakuchur.puzzlegame.editor.commands.CommandHistory;
import com.github.nikitakuchur.puzzlegame.editor.utils.ParametersUtils;
import com.github.nikitakuchur.puzzlegame.serialization.Parameterizable;
import com.github.nikitakuchur.puzzlegame.serialization.Parameters;

import javax.swing.table.AbstractTableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParametersTableModel extends AbstractTableModel {

    private final String[] columnsHeader = new String[]{"Name", "Value"};

    private static final Set<Class<?>> ALLOWED_TYPES = new HashSet<>();

    static {
        ALLOWED_TYPES.add(String.class);
        ALLOWED_TYPES.add(Integer.class);
        ALLOWED_TYPES.add(Boolean.class);
        ALLOWED_TYPES.add(Color.class);
        ALLOWED_TYPES.add(Enum.class);
    }

    private transient Parameterizable parameterizable;
    private transient Parameters parameters;

    public ParametersTableModel() {
        CommandHistory.getInstance().addHistoryChangeListener(this::update);
    }

    public void setParameterizable(Parameterizable parameterizable) {
        this.parameterizable = parameterizable;
        this.parameters = parameterizable != null ? parameterizable.getParameters() : new Parameters();
        fireTableRowsDeleted(0, getRowCount() == 0 ? 0 : getRowCount() - 1);
    }

    public void clear() {
        parameterizable = null;
        parameters = new Parameters();
        fireTableRowsDeleted(0, getRowCount() == 0 ? 0 : getRowCount() - 1);
    }

    @Override
    public int getRowCount() {
        if (parameterizable == null) return 0;
        return getNames().size();
    }

    private List<String> getNames() {
        return parameters.nameSet().stream()
                .filter(name -> isAllowedType(parameters.getType(name)))
                .collect(Collectors.toList());
    }

    private boolean isAllowedType(Class<?> type) {
        return ALLOWED_TYPES.stream().anyMatch(clazz -> clazz.isAssignableFrom(type));
    }

    @Override
    public int getColumnCount() {
        return columnsHeader.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnsHeader[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (parameterizable == null) return null;
        String name = getNames().get(rowIndex);
        if (columnIndex == 0) {
            return name;
        }
        if (parameters.getType(name) == String.class) {
            return parameters.getValueOrDefault(name, "");
        }
        if (parameters.getType(name) == Boolean.class) {
            return parameters.getValueOrDefault(name, false);
        }
        return parameters.getValue(name);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (parameterizable == null) return;
        String name = getNames().get(rowIndex);
        Class<?> type = parameters.getType(name);
        if (type == String.class) {
            setValue(name, String.class, "".equals(value) ? null : value);
        } else if (type == Integer.class) {
            setValue(name, Integer.class, ParametersUtils.parseIntOrDefault((String) value, 0));
        } else {
            setValue(name, value.getClass(), value);
        }
        update();
    }

    private void update() {
        if (parameterizable == null) return;
        parameters = parameterizable.getParameters();
        fireTableDataChanged();
    }

    private <T> void setValue(String name, Class<? extends T> type, T value) {
        CommandHistory.getInstance().addAndExecute(new ChangeParameterCommand<>(parameterizable, name, type, value));
    }
}
