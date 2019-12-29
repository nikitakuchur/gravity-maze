package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.Layer;
import com.github.nikitakuchur.puzzlegame.utils.PropertiesHolder;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private final transient LevelEditor levelEditor;

    private JComboBox<Layer> comboBox;

    private transient PropertiesHolder propertiesHolder;
    private PropertiesPanel propertiesPanel = new PropertiesPanel();

    public RightPanel(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
        propertiesHolder = levelEditor.getLevel().getBackground();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(140, 400));
        add(panel);

        GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();
        gameObjectsPanel.setVisible(false);

        comboBox = new JComboBox<>(Layer.values());
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
        panel.add(gameObjectsPanel);
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