package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.editor.utils.Option;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private final transient LevelEditor levelEditor;

    private final JPanel panel = new JPanel();
    private final JComboBox<Option> comboBox = new JComboBox<>(Option.values());

    private final ParametersPanel parametersPanel;
    private final GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();

    public RightPanel(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;

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
        gameObjectsPanel.addTypeSelectListener(levelEditor::setGameObjectType);

        comboBox.addActionListener(actionEvent -> {
            Option option = (Option) comboBox.getSelectedItem();
            if (option == null) return;

            gameObjectsPanel.setVisible(false);
            if (option == Option.GAME_OBJECTS) {
                gameObjectsPanel.setVisible(true);
            }

            levelEditor.setLayer(option);
            initParameterizable();
        });

        parametersPanel = new ParametersPanel(levelEditor.getLevel().getBackground());

        levelEditor.addLevelChangeListener(lev -> initParameterizable());
        levelEditor.addGameObjectSelectListener(parametersPanel::setParameterizable);

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

    private void initParameterizable() {
        Level level = levelEditor.getLevel();
        Option option = (Option) comboBox.getSelectedItem();
        if (option == null) return;
        switch (option) {
            case BACKGROUND:
                parametersPanel.setParameterizable(level.getBackground());
                break;
            case MAP:
                parametersPanel.setParameterizable(level.getMap());
                break;
            default:
                parametersPanel.clear();
                break;
        }
    }
}
