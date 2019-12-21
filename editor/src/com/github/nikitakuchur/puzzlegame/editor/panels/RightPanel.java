package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.Layer;
import com.github.nikitakuchur.puzzlegame.utils.PropertiesHolder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RightPanel extends JPanel {

    private final transient LevelEditor levelEditor;
    JComboBox<Layer> comboBox;
    private PropertiesHolder propertiesHolder;
    private PropertiesPanel propertiesPanel = new PropertiesPanel();

    public RightPanel(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
        propertiesHolder = levelEditor.getLevel().getBackground();

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

        comboBox = new JComboBox<>(comboBoxModel);
        comboBox.addActionListener(actionEvent -> {
            Layer layer = (Layer) comboBox.getSelectedItem();
            if (layer == null) return;

            gameObjectsPanel.setVisible(false);
            if (layer == Layer.GAME_OBJECTS) {
                gameObjectsPanel.setVisible(true);
            }

            levelEditor.setLayer(layer);
            initProperties();
        });

        initProperties();
        levelEditor.addLevelChangeListener(this::initProperties);

        panel.add(comboBox);
        panel.add(propertiesPanel);
    }

    private void initProperties() {
        Layer layer = (Layer) comboBox.getSelectedItem();
        if (layer == null) return;
        switch (layer) {
            case BACKGROUND:
                propertiesHolder = levelEditor.getLevel().getBackground();
                break;
            case MAP:
                propertiesHolder = levelEditor.getLevel().getMap();
                break;
            default:
                break;
        }
        propertiesPanel.setProperties(propertiesHolder.getProperties());
        propertiesPanel.addPropertiesListener(() -> propertiesHolder.setProperties(propertiesPanel.getProperties()));
    }
}
