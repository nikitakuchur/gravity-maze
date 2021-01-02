package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.Gdx;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.level.LevelLoader;

import javax.swing.*;

public class TopPanel extends JPanel {

    private transient Level level;

    public TopPanel(EditorApplication app) {
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            level = app.getLevelEditor().getLevel();
            Level copy = copyLevel(level);
            app.getLevelEditor().setLevel(copy);
            app.getLevelEditor().play();
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
        }));

        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            app.getLevelEditor().setLevel(level);
            app.getLevelEditor().stop();
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
        }));

        add(playButton);
        add(stopButton);
    }

    private Level copyLevel(Level level) {
        String json = LevelLoader.toJson(level);
        return LevelLoader.fromJson(json);
    }
}
