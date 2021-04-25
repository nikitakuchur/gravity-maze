package com.majakkagames.gravitymaze.editor.panels.accessors;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.editor.commands.ChangeParameterCommand;
import com.majakkagames.gravitymaze.editor.CommandHistory;
import com.majakkagames.gravitymaze.core.serialization.Parameters;

import javax.swing.table.TableCellEditor;

public class DefaultFieldAccessor<T> implements FieldAccessor<T> {

    public T getValue(Parameters parameters, String name) {
        return parameters.getValue(name);
    }

    public void setValue(GameObject gameObject, String name, Object value) {
        CommandHistory.getInstance().addAndExecute(
                new ChangeParameterCommand<>(gameObject, name, Object.class, value)
        );
    }

    @Override
    public TableCellEditor getCellEditor(Class<? extends T> type) {
        return null;
    }
}
