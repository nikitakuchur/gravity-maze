package com.majakkagames.gravitymaze.editor;

import com.badlogic.gdx.Gdx;
import com.majakkagames.gravitymaze.core.game.Context;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.editorcore.config.LevelCreator;
import com.majakkagames.gravitymaze.editorcore.utils.ParametersUtils;
import com.majakkagames.gravitymaze.game.gameobjects.Maze;

import javax.swing.*;

public class NewLevelCreator implements LevelCreator {
    @Override
    public Level create(Context context) {
        JTextField widthField = new JTextField(5);
        widthField.setText("8");
        JTextField heightField = new JTextField(5);
        heightField.setText("8");

        JPanel dialogPanel = new JPanel();
        dialogPanel.add(new JLabel("Width:"));
        dialogPanel.add(widthField);
        dialogPanel.add(new JLabel("Height:"));
        dialogPanel.add(heightField);

        int option = JOptionPane.showConfirmDialog(null, dialogPanel, "New Level",
                JOptionPane.OK_CANCEL_OPTION);
        Level level = new Level(context);
        if (option == JOptionPane.OK_OPTION) {
            int width = ParametersUtils.parseIntOrDefault(widthField.getText(), 8);
            int height = ParametersUtils.parseIntOrDefault(heightField.getText(), 8);
            Gdx.app.postRunnable(() -> level.getGameObjectStore().add(new Maze(width, height)));
            return level;
        }
        return null;
    }
}
