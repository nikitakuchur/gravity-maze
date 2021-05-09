package com.majakkagames.gravitymaze.editorcore.panels;

import com.badlogic.gdx.Gdx;
import com.majakkagames.gravitymaze.editorcore.EditorApplication;
import com.majakkagames.gravitymaze.editorcore.LevelManager;

import javax.swing.*;

public class TopPanel extends JPanel {

    public TopPanel(EditorApplication app) {
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            LevelManager levelManager = app.getLevelEditor().getLevelManager();
            levelManager.makeSnapshot();
            app.getLevelEditor().play();
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
        }));

        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            LevelManager levelManager = app.getLevelEditor().getLevelManager();
            levelManager.removeLastSnapshot();
            app.getLevelEditor().stop();
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
        }));

        add(playButton);
        add(stopButton);
    }
}
