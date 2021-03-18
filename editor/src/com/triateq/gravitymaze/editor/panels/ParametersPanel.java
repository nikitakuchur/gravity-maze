package com.triateq.gravitymaze.editor.panels;

import java.awt.*;

import com.badlogic.gdx.graphics.Color;
import com.triateq.gravitymaze.editor.panels.cells.ColorCell;
import com.triateq.gravitymaze.serialization.Parameterizable;

import javax.swing.*;

public class ParametersPanel extends JPanel {

    private final ParametersTableModel tableModel = new ParametersTableModel();
    private final JTable table = new ParametersTable(tableModel);

    public ParametersPanel(Parameterizable parameterizable) {
        tableModel.setParameterizable(parameterizable);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table.setRowHeight(28);
        table.setDefaultEditor(Color.class, new ColorCell());

        add(new JLabel("Parameters:"));
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(200, 800));
        pane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(pane);
    }

    public void clear() {
        tableModel.clear();
    }

    public void setParameterizable(Parameterizable parameterizable) {
        table.removeEditor();
        tableModel.setParameterizable(parameterizable);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        table.setEnabled(b);
    }
}
