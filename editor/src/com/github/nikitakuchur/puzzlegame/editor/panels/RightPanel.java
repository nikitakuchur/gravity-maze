package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.utils.Layer;
import com.github.nikitakuchur.puzzlegame.utils.Properties;
import com.github.nikitakuchur.puzzlegame.utils.PropertiesHolder;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private final transient LevelEditor levelEditor;

    private JPanel panel = new JPanel();
    private JComboBox<Layer> comboBox = new JComboBox<>(Layer.values());

    private transient PropertiesHolder propertiesHolder;
    private PropertiesPanel propertiesPanel = new PropertiesPanel();
    private GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();

    public RightPanel(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
        propertiesHolder = levelEditor.getLevel().getBackground();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(140, 400));
        add(panel);

        levelEditor.addLevelPlayListener(() -> setEnabled(false));

        levelEditor.addLevelStopListener(() -> {
            setEnabled(true);
            comboBox.setSelectedIndex(comboBox.getSelectedIndex());
        });

        gameObjectsPanel.setVisible(false);
        gameObjectsPanel.addGameObjectsListener(
                () -> levelEditor.setGameObjectType(gameObjectsPanel.getSelectedGameObjectType()));

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
        levelEditor.addSelectGameObjectListener(() -> {
            propertiesHolder = levelEditor.getSelectedGameObject();
            updateProperties();
        });

        panel.add(comboBox);
        panel.add(gameObjectsPanel);
        panel.add(propertiesPanel);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (Component component : panel.getComponents()) {
            component.setEnabled(b);
        }
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
            case GAME_OBJECTS:
                propertiesPanel.setProperties(new Properties());
                return;
            default:
                break;
        }
        updateProperties();
    }

    private void updateProperties() {
        if (propertiesHolder == null) {
            propertiesPanel.setProperties(new Properties());
            return;
        }
        propertiesPanel.setProperties(propertiesHolder.getProperties());
        propertiesPanel.addPropertiesListener(() -> propertiesHolder.setProperties(propertiesPanel.getProperties()));
    }
}
