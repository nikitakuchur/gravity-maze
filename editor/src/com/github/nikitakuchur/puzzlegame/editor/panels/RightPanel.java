package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.utils.Layer;
import com.github.nikitakuchur.puzzlegame.game.entities.Entity;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private final JPanel panel = new JPanel();
    private final JComboBox<Layer> comboBox = new JComboBox<>(Layer.values());

    private final PropertiesPanel propertiesPanel = new PropertiesPanel();
    private final GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();

    private transient Entity entity;

    public RightPanel(LevelEditor levelEditor) {
        Level level = levelEditor.getLevel();
        entity = level.getBackground();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(200, 400));
        add(panel);

        levelEditor.addLevelPlayListener(() -> setEnabled(false));

        levelEditor.addLevelStopListener(() -> {
            setEnabled(true);
            comboBox.setSelectedIndex(comboBox.getSelectedIndex());
        });

        gameObjectsPanel.setVisible(false);
        gameObjectsPanel.addGameObjectSelectListener(levelEditor::setGameObjectType);

        comboBox.addActionListener(actionEvent -> {
            Layer layer = (Layer) comboBox.getSelectedItem();
            if (layer == null) return;

            gameObjectsPanel.setVisible(false);
            if (layer == Layer.GAME_OBJECTS) {
                gameObjectsPanel.setVisible(true);
            }

            levelEditor.setLayer(layer);
            initProperties(level);
        });

        initProperties(level);
        levelEditor.addLevelChangeListener(this::initProperties);
        levelEditor.addGameObjectSelectListener(gameObject -> {
            entity = gameObject;
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

    private void initProperties(Level level) {
        Layer layer = (Layer) comboBox.getSelectedItem();
        if (layer == null) return;
        switch (layer) {
            case BACKGROUND:
                entity = level.getBackground();
                break;
            case MAP:
                entity = level.getMap();
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
        if (entity == null) {
            propertiesPanel.setProperties(new Properties());
            return;
        }
        propertiesPanel.setProperties(entity.getProperties());
        propertiesPanel.addPropertiesChangeListener(properties -> entity.setProperties(properties));
    }
}
