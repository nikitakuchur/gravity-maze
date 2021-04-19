package com.triateq.gravitymaze.editor.panels;

import com.badlogic.gdx.Gdx;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.editor.EditorApplication;
import com.triateq.gravitymaze.core.serialization.LevelLoader;
import com.triateq.gravitymaze.game.utils.LevelBuilder;

import javax.swing.*;

public class TopPanel extends JPanel {

    private final LevelLoader levelLoader;
    private transient Level level;

    public TopPanel(EditorApplication app) {
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        levelLoader = new LevelLoader(app.getContext());

        playButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            level = app.getLevelEditor().getLevel();
            Level copy = LevelBuilder.from(level).build();
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
        String json = levelLoader.toJson(level);
        return levelLoader.fromJson(json);
    }
}
