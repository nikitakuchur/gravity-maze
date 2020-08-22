package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.github.nikitakuchur.puzzlegame.game.entities.Parameterizable;

import javax.swing.*;

public class ParametersPanel extends JPanel {

    private final ParametersTableModel tableModel = new ParametersTableModel();
    private final JTable table = new ParametersTable(tableModel);

    public ParametersPanel(Parameterizable parameterizable) {
        tableModel.setParameterizable(parameterizable);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table.setDefaultEditor(Color.class, new ColorCell());

        add(new JLabel("Parameters:"));
        add(new JScrollPane(table));
    }

    public void clear() {
        tableModel.clear();
    }

    public void setParameterizable(Parameterizable parameterizable) {
        tableModel.setParameterizable(parameterizable);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        table.setEnabled(b);
    }
}
