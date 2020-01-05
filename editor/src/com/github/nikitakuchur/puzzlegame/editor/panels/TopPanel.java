package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.game.Level;
import com.github.nikitakuchur.puzzlegame.utils.LevelLoader;

import javax.swing.*;

public class TopPanel extends JPanel {

    private transient EditorApplication app;

    public TopPanel(EditorApplication app) {
        this.app = app;

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> Gdx.app.postRunnable(this::loadLevel));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> Gdx.app.postRunnable(this::saveLevel));

        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            saveLevel();
            app.getLevelEditor().play();
            loadButton.setEnabled(false);
            saveButton.setEnabled(false);
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
        }));

        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            loadLevel();
            app.getLevelEditor().stop();
            loadButton.setEnabled(true);
            saveButton.setEnabled(true);
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
        }));

        add(loadButton);
        add(saveButton);
        add(playButton);
        add(stopButton);
    }

    private void loadLevel() {
        Level level = LevelLoader.load(Gdx.files.internal("levels/sample.json"));
        app.getLevelEditor().setLevel(level);
    }

    private void saveLevel() {
        Level level = app.getLevelEditor().getLevel();

        Json json = new Json(JsonWriter.OutputType.json);
        String text = json.prettyPrint(json.toJson(level));

        FileHandle file = Gdx.files.local("levels/sample.json");
        file.writeString(text, false);
    }
}
