package com.triateq.gravitymaze.editor.panels;

import com.badlogic.gdx.Gdx;
import com.triateq.gravitymaze.editor.LevelEditor;
import com.triateq.gravitymaze.editor.commands.ChangeParameterCommand;
import com.triateq.gravitymaze.editor.commands.Command;
import com.triateq.gravitymaze.editor.commands.CommandHistory;
import com.triateq.gravitymaze.editor.utils.Option;
import com.triateq.gravitymaze.game.level.Level;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private final transient LevelEditor levelEditor;

    private final JPanel panel = new JPanel();
    private final JComboBox<Option> comboBox = new JComboBox<>(Option.values());

    private final ParametersPanel parametersPanel;
    private final GameObjectsPanel gameObjectsPanel = new GameObjectsPanel();

    private final JSpinner maxMovesSpinner;

    public RightPanel(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(true);
        panel.setPreferredSize(new Dimension(200, 500));
        add(panel);

        levelEditor.addLevelPlayListener(() -> setEnabled(false));

        levelEditor.addLevelStopListener(() -> {
            setEnabled(true);
            comboBox.setSelectedIndex(comboBox.getSelectedIndex());
        });

        gameObjectsPanel.setVisible(false);
        gameObjectsPanel.addTypeSelectListener(levelEditor::setGameObjectType);
        gameObjectsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        parametersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("Max moves:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 32, 1);
        maxMovesSpinner = new JSpinner(spinnerModel);
        maxMovesSpinner.setPreferredSize(new Dimension(60, 30));
        maxMovesSpinner.addChangeListener(event ->
                Gdx.app.postRunnable(() -> {
                    int maxMoves = levelEditor.getLevel().getMaxMoves();
                    int value = (int) maxMovesSpinner.getValue();
                    if (maxMoves != value) {
                        Command command = new ChangeParameterCommand<>(levelEditor.getLevel(), "maxMoves",
                                Integer.class, maxMovesSpinner.getValue());
                        CommandHistory.getInstance().addAndExecute(command);
                    }
                }));
        CommandHistory.getInstance().addHistoryChangeListener(() -> {
            int newValue = levelEditor.getLevel().getMaxMoves();
            spinnerModel.setValue(newValue);
        });

        levelEditor.addLevelChangeListener(lev -> {
            initParameterizable();
            maxMovesSpinner.setValue(levelEditor.getLevel().getMaxMoves());
        });
        levelEditor.addGameObjectSelectListener(parametersPanel::setParameterizable);

        JPanel levelPanel = new JPanel();
        levelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        levelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        levelPanel.add(label);
        levelPanel.add(maxMovesSpinner);

        panel.add(comboBox);
        panel.add(gameObjectsPanel);
        panel.add(parametersPanel);
        panel.add(levelPanel);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        for (Component component : panel.getComponents()) {
            component.setEnabled(b);
        }
        maxMovesSpinner.setEnabled(b);
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
