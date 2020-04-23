package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;

import javax.swing.*;

public class TopPanel extends JPanel {

    private final transient EditorApplication app;

    private String levelSave;

    public TopPanel(EditorApplication app) {
        this.app = app;

        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            save();
            app.getLevelEditor().play();
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
        }));

        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> Gdx.app.postRunnable(() -> {
            restore();
            app.getLevelEditor().stop();
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
        }));

        add(playButton);
        add(stopButton);
    }

    private void save() {
        Level level = app.getLevelEditor().getLevel();
        levelSave = new Json(JsonWriter.OutputType.json).toJson(level);
    }

    private void restore() {
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonValue = jsonReader.parse(levelSave);
        Level level =  new Json().readValue(Level.class, jsonValue);
        app.getLevelEditor().setLevel(level);
        levelSave = null;
    }
}
