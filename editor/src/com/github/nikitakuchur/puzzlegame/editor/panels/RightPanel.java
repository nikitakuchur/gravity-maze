package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.utils.Option;
import com.github.nikitakuchur.puzzlegame.game.entities.Entity;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private final JPanel panel = new JPanel();
    private final JComboBox<Option> comboBox = new JComboBox<>(Option.values());

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
            Option option = (Option) comboBox.getSelectedItem();
            if (option == null) return;

            gameObjectsPanel.setVisible(false);
            if (option == Option.GAME_OBJECTS) {
                gameObjectsPanel.setVisible(true);
            }

            levelEditor.setLayer(option);
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
        Option option = (Option) comboBox.getSelectedItem();
        if (option == null) return;
        switch (option) {
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
