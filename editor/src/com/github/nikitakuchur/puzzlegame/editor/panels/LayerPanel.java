package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.EditableLevel;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.editor.Layer;
import com.github.nikitakuchur.puzzlegame.editor.components.BackgroundPanel;
import com.github.nikitakuchur.puzzlegame.editor.components.GameObjectsPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LayerPanel extends JPanel {

    public LayerPanel(EditorApplication app) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(true);
        add(panel);

        DefaultComboBoxModel<Layer> comboBoxModel = new DefaultComboBoxModel<>();
        for (Layer value : Layer.values()) {
            comboBoxModel.addElement(value);
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();

        backgroundPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        gameObjectsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JComboBox<Layer> comboBox = new JComboBox<>(comboBoxModel);
        comboBox.addActionListener(actionEvent -> {
            EditableLevel level = app.getEditableLevel();
            Layer layer = (Layer) comboBox.getSelectedItem();
            if (layer == null) return;

            backgroundPanel.setVisible(false);
            gameObjectsPanel.setVisible(false);
            if (layer == Layer.BACKGROUND) {
                backgroundPanel.setVisible(true);
            }
            if (layer == Layer.GAME_OBJECTS) {
                gameObjectsPanel.setVisible(true);
            }

            level.setLayer(layer);
        });


        panel.add(comboBox);
        panel.add(backgroundPanel);
    }
}
