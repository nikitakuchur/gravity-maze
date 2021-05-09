package com.majakkagames.gravitymaze.editorcore.panels;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.Parameters;
import com.majakkagames.gravitymaze.core.serialization.Serializer;
import com.majakkagames.gravitymaze.editorcore.panels.accessors.FieldAccessor;
import com.majakkagames.gravitymaze.editorcore.panels.accessors.FieldAccessors;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class GameObjectTableModel extends AbstractTableModel {

    private final String[] columnsHeader = new String[]{"Name", "Value"};

    private transient GameObject gameObject;
    private transient Parameters parameters;

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        this.parameters = gameObject != null ? Serializer.getParameters(gameObject) : new Parameters();
        fireTableRowsDeleted(0, getRowCount() == 0 ? 0 : getRowCount() - 1);
    }

    public void clear() {
        gameObject = null;
        parameters = new Parameters();
        fireTableRowsDeleted(0, getRowCount() == 0 ? 0 : getRowCount() - 1);
    }

    @Override
    public int getRowCount() {
        if (gameObject == null) return 0;
        return getNames().size();
    }

    private List<String> getNames() {
        return parameters.nameSet().stream()
                .filter(name -> FieldAccessors.isAllowedType(parameters.getType(name)))
                .collect(Collectors.toList());
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
        if (gameObject == null) return null;
        String name = getNames().get(rowIndex);
        if (columnIndex == 0) {
            return name;
        }
        Class<?> type = parameters.getType(name);

        FieldAccessor<?> accessor = FieldAccessors.getAccessor(type);
        if (accessor != null) {
            return accessor.getValue(parameters, name);
        }
        throw new IllegalStateException("Cannot find an accessor for field named " + name);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (gameObject == null) return;
        String name = getNames().get(rowIndex);
        Class<?> type = parameters.getType(name);

        FieldAccessor<?> accessor = FieldAccessors.getAccessor(type);
        if (accessor != null) {
            accessor.setValue(gameObject, name, value);
        } else {
            throw new IllegalStateException("Cannot find an accessor for field named " + name);
        }
        update();
        fireTableDataChanged();
    }

    public void update() {
        if (gameObject == null) return;
        parameters = Serializer.getParameters(gameObject);
    }
}
