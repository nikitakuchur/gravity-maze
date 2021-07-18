package com.majakkagames.gravitymaze.editorcore;

import com.badlogic.gdx.Gdx;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.editorcore.config.Configurator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static javax.swing.JFileChooser.APPROVE_OPTION;

@SuppressWarnings("java:S1948")
public class EditorMenuBar extends JMenuBar {

    private final Configurator configurator;
    private final EditorApplication app;

    private final JMenu fileMenu = new JMenu("File");
    private final JMenu editMenu = new JMenu("Edit");

    public EditorMenuBar(Configurator configurator, EditorApplication app) {
        this.configurator = configurator;
        this.app = app;
        this.add(createFileMenu());
        this.add(createEditMenu());
    }

    private JMenu createFileMenu() {
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(event -> EventQueue.invokeLater(() -> {
            Level level = configurator.getLevelCreator().create(app.getContext());
            if (level != null) {
                Gdx.app.postRunnable(() -> {
                    configurator.getLevelPreparer().accept(level);
                    app.getFileController().newFile(level);
                });
            }
        }));

        JMenuItem openItem = new JMenuItem("Open...");
        openItem.addActionListener(event -> EventQueue.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser(app.getFileController().getPath());
            int option = fileChooser.showOpenDialog(null);
            if (option == APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Gdx.app.postRunnable(() -> {
                    try {
                        app.getFileController().open(file.getPath());
                    } catch (IOException e) {
                        EventQueue.invokeLater(() ->
                                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                        );
                    }
                });
            }
        }));

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setEnabled(false);
        EventQueue.invokeLater(() ->
                Gdx.app.postRunnable(() ->
                        app.getFileController().addPathChangeListener(path -> saveItem.setEnabled(path != null))
                )
        );
        saveItem.addActionListener(e ->
                Gdx.app.postRunnable(() -> app.getFileController().save())
        );

        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.addActionListener(e -> EventQueue.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser(app.getFileController().getPath());
            int option = fileChooser.showSaveDialog(null);
            if (option == APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Gdx.app.postRunnable(() -> app.getFileController().saveAs(file.getPath()));
            }
        }));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            LevelManager history = app.getLevelEditor().getLevelManager();
            if (history.canUndo()) {
                history.undo();
            }
        }));

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            LevelManager history = app.getLevelEditor().getLevelManager();
            if (history.canRedo()) {
                history.redo();
            }
        }));

        editMenu.add(undoItem);
        editMenu.add(redoItem);

        return editMenu;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
    }
}
