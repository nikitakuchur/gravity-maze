package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.editor.Layer;
import com.github.nikitakuchur.puzzlegame.game.Background;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RightPanel extends JPanel {

    PropertiesPanel propertiesPanel = new PropertiesPanel();

    public RightPanel(EditorApplication app) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(true);
        add(panel);

        DefaultComboBoxModel<Layer> comboBoxModel = new DefaultComboBoxModel<>();
        for (Layer value : Layer.values()) {
            comboBoxModel.addElement(value);
        }

        GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();
        gameObjectsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JComboBox<Layer> comboBox = new JComboBox<>(comboBoxModel);
        comboBox.addActionListener(actionEvent -> {
            LevelEditor levelEditor = app.getEditableLevel();
            Layer layer = (Layer) comboBox.getSelectedItem();
            if (layer == null) return;

            gameObjectsPanel.setVisible(false);
            if (layer == Layer.GAME_OBJECTS) {
                gameObjectsPanel.setVisible(true);
            }

            levelEditor.setLayer(layer);
        });

        Background background = app.getEditableLevel().getLevel().getBackground();
        propertiesPanel.setProperties(background.getProperties());

        panel.add(comboBox);
        panel.add(propertiesPanel);
    }
}
