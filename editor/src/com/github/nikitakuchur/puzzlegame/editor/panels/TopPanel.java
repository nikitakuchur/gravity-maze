package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.nikitakuchur.puzzlegame.editor.EditorApplication;
import com.github.nikitakuchur.puzzlegame.game.Level;

import javax.swing.*;

public class TopPanel extends JPanel {

    private final EditorApplication app;

    public TopPanel(EditorApplication app) {
        this.app = app;

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            JsonReader jsonReader = new JsonReader();
            JsonValue jsonValue = jsonReader.parse(Gdx.files.local("levels/sample.json"));

            Json json = new Json();
            Gdx.app.postRunnable(() -> {
                Level level = json.readValue(Level.class, jsonValue);
                app.getEditableLevel().setLevel(level);
            });
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            Level level = app.getEditableLevel().getLevel();

            Json json = new Json(JsonWriter.OutputType.json);
            String text = json.prettyPrint(json.toJson(level));

            FileHandle file = Gdx.files.local("levels/sample.json");
            file.writeString(text, false);
        });

        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        add(loadButton);
        add(saveButton);
        add(playButton);
        add(stopButton);
    }
}
