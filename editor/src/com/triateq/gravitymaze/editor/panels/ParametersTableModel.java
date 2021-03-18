package com.triateq.gravitymaze.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.triateq.gravitymaze.editor.commands.ChangeParameterCommand;
import com.triateq.gravitymaze.editor.commands.CommandHistory;
import com.triateq.gravitymaze.editor.panels.accessors.*;
import com.triateq.gravitymaze.editor.utils.ParametersUtils;
import com.triateq.gravitymaze.serialization.Parameterizable;
import com.triateq.gravitymaze.serialization.Parameters;
import com.triateq.gravitymaze.serialization.Serializer;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ParametersTableModel extends AbstractTableModel {

    private final String[] columnsHeader = new String[]{"Name", "Value"};

    private static final HashMap<Class<?>, FieldAccessor<?>> FIELD_ACCESSORS = new HashMap<>();

    static {
        FIELD_ACCESSORS.put(String.class, new StringFieldAccessor());
        FIELD_ACCESSORS.put(Integer.class, new IntegerFieldAccessor());
        FIELD_ACCESSORS.put(int.class, new IntegerFieldAccessor());
        FIELD_ACCESSORS.put(Boolean.class, new BooleanFieldAccessor());
        FIELD_ACCESSORS.put(boolean.class, new BooleanFieldAccessor());
        FIELD_ACCESSORS.put(Color.class, new ColorFieldAccessor());
        FIELD_ACCESSORS.put(Enum.class, new DefaultFieldAccessor<>());
    }

    private transient Parameterizable parameterizable;
    private transient Parameters parameters;

    public ParametersTableModel() {
        CommandHistory.getInstance().addHistoryChangeListener(this::update);
    }

    public void setParameterizable(Parameterizable parameterizable) {
        this.parameterizable = parameterizable;
        this.parameters = parameterizable != null ? Serializer.getParameters(parameterizable) : new Parameters();
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
        return FIELD_ACCESSORS.keySet().stream().anyMatch(clazz -> clazz.isAssignableFrom(type));
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
        Class<?> type = parameters.getType(name);

        FieldAccessor<?> accessor = getAccessor(type);
        if (accessor != null) {
            return accessor.getValue(parameters, name);
        }
        throw new IllegalStateException("Cannot find an accessor for field named " + name);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (parameterizable == null) return;
        String name = getNames().get(rowIndex);
        Class<?> type = parameters.getType(name);

        FieldAccessor<?> accessor = getAccessor(type);
        if (accessor != null) {
            accessor.setValue(parameterizable, name, value);
        } else {
            throw new IllegalStateException("Cannot find an accessor for field named " + name);
        }
        update();
    }

    private void update() {
        if (parameterizable == null) return;
        parameters = Serializer.getParameters(parameterizable);
        fireTableDataChanged();
    }

    private FieldAccessor<?> getAccessor(Class<?> type) {
        for (Class<?> clazz : FIELD_ACCESSORS.keySet()) {
            if (clazz.isAssignableFrom(type)) {
                return FIELD_ACCESSORS.get(clazz);
            }
        }
        return null;
    }
}
