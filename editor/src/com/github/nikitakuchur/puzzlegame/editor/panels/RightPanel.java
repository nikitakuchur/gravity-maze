package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.utils.Option;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.Parameterizable;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private final transient LevelEditor levelEditor;

    private final JPanel panel = new JPanel();
    private final JComboBox<Option> comboBox = new JComboBox<>(Option.values());

    private final ParametersPanel parametersPanel = new ParametersPanel();
    private final GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();

    private transient Parameterizable parameterizable;

    public RightPanel(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
        Level level = levelEditor.getLevel();
        parameterizable = level.getBackground();

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
            initProperties();
        });

        initProperties();
        levelEditor.addLevelChangeListener(lev -> initProperties());
        levelEditor.addGameObjectSelectListener(gameObject -> {
            parameterizable = gameObject;
            updateProperties();
        });

        panel.add(comboBox);
        panel.add(gameObjectsPanel);
        panel.add(parametersPanel);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (Component component : panel.getComponents()) {
            component.setEnabled(b);
        }
    }

    private void initProperties() {
        Level level = levelEditor.getLevel();
        Option option = (Option) comboBox.getSelectedItem();
        if (option == null) return;
        switch (option) {
            case BACKGROUND:
                parameterizable = level.getBackground();
                break;
            case MAP:
                parameterizable = level.getMap();
                break;
            case GAME_OBJECTS:
                parametersPanel.setParameters(new Parameters());
                return;
            default:
                break;
        }
        updateProperties();
    }

    private void updateProperties() {
        if (parameterizable == null) {
            parametersPanel.setParameters(new Parameters());
            return;
        }
        parametersPanel.setParameters(parameterizable.getParameters());
        parametersPanel.addPropertiesChangeListener(properties -> parameterizable.setParameters(properties));
    }
}
