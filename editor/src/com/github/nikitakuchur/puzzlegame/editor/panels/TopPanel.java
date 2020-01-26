package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.Gdx;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.editor.utils.LevelUtils;

import javax.swing.*;

public class TopPanel extends JPanel {

    public TopPanel(EditorApplication app) {

        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            LevelUtils.save(app);
            app.getLevelEditor().play();
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
        }));

        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            LevelUtils.load(app);
            app.getLevelEditor().stop();
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
        }));

        add(playButton);
        add(stopButton);
    }
}
