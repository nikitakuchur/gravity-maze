package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.github.nikitakuchur.puzzlegame.editor.panels.RightPanel;
import com.github.nikitakuchur.puzzlegame.editor.panels.TopPanel;
import com.github.nikitakuchur.puzzlegame.editor.utils.LevelUtils;
import com.github.nikitakuchur.puzzlegame.game.Level;

import javax.swing.*;
import java.awt.*;

public class EditorWindow {

    private JFrame window = new JFrame("Editor");
    private EditorApplication app = new EditorApplication();

    private MenuBar menuBar = new MenuBar();
    private JPanel contentPane = new JPanel();

    public EditorWindow() {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(app);

        window.setMenuBar(menuBar);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas.getCanvas(), BorderLayout.CENTER);
        canvas.postRunnable(this::init);
        window.setContentPane(contentPane);

        window.pack();
        window.setSize(800, 800);
        window.setVisible(true);
    }

    private Menu createFileMenu()
    {
        Menu file = new Menu("File");

        MenuItem newItem = new MenuItem("New");
        newItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            LevelEditor editor = app.getLevelEditor();
            editor.stop();
            editor.setLevel(new Level());
        }));

        MenuItem openItem = new MenuItem("Open...");
        openItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            app.getLevelEditor().stop();
            LevelUtils.load(app);
        }));

        MenuItem saveItem = new MenuItem("Save");
        saveItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            app.getLevelEditor().stop();
            LevelUtils.save(app);
        }));

        MenuItem saveAsItem = new MenuItem("Save As...");
        saveAsItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            app.getLevelEditor().stop();
            LevelUtils.save(app);
        }));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        file.add(newItem);
        file.add(openItem);
        file.add(saveItem);
        file.add(saveAsItem);
        file.addSeparator();
        file.add(exitItem);

        return file;
    }

    private void init() {
        contentPane.add(new TopPanel(app), BorderLayout.NORTH);
        contentPane.add(new RightPanel(app.getLevelEditor()), BorderLayout.EAST);
        menuBar.add(createFileMenu());
        window.revalidate();
    }
}
