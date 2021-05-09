package com.majakkagames.gravitymaze.editorcore.panels;

import com.badlogic.gdx.graphics.Color;
import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.editorcore.panels.cells.ColorCellEditor;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.awt.*;

public class GameObjectPanel extends JPanel {

    private final GameObjectTableModel tableModel = new GameObjectTableModel();
    private final JTable table = new GameObjectTable(tableModel);

    public GameObjectPanel(GameObject gameObject) {
        tableModel.setGameObject(gameObject);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table.setRowHeight(28);
        table.setDefaultEditor(Color.class, new ColorCellEditor());

        add(new JLabel("Game object:"));
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(200, 800));
        pane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(pane);
    }

    public void setGameObject(GameObject gameObject) {
        table.removeEditor();
        tableModel.setGameObject(gameObject);
    }

    public void update() {
        tableModel.update();
        table.repaint();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        table.setEnabled(b);
    }

    public void addTableModelListener(TableModelListener listener) {
        tableModel.addTableModelListener(listener);
    }
}
