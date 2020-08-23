package com.github.nikitakuchur.puzzlegame.editor;

import com.alee.laf.WebLookAndFeel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.github.nikitakuchur.puzzlegame.editor.commands.CommandHistory;
import com.github.nikitakuchur.puzzlegame.editor.panels.RightPanel;
import com.github.nikitakuchur.puzzlegame.editor.panels.TopPanel;
import com.github.nikitakuchur.puzzlegame.editor.utils.ParametersUtils;
import com.github.nikitakuchur.puzzlegame.game.entities.Background;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;

import javax.swing.*;

import java.awt.*;
import java.io.File;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class EditorWindow {
    private static final String DEFAULT_TITLE = "unnamed";

    private final JFrame window = new JFrame(DEFAULT_TITLE);
    private final EditorApplication app = new EditorApplication();

    private final MenuBar menuBar = new MenuBar();
    private final JPanel contentPane = new JPanel();

    public EditorWindow() {
        WebLookAndFeel.install();

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(app);

        window.setMenuBar(menuBar);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas.getCanvas(), BorderLayout.CENTER);
        Gdx.app.postRunnable(this::init);
        window.setContentPane(contentPane);

        window.pack();
        window.setSize(800, 800);
        window.setVisible(true);
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newItem = new MenuItem("New");
        newItem.addActionListener(e -> EventQueue.invokeLater(() -> {
            LevelEditor editor = app.getLevelEditor();
            editor.stop();

            JTextField widthField = new JTextField(5);
            widthField.setText("8");
            JTextField heightField = new JTextField(5);
            heightField.setText("8");

            JPanel dialogPanel = new JPanel();
            dialogPanel.add(new JLabel("Width:"));
            dialogPanel.add(widthField);
            dialogPanel.add(new JLabel("Height:"));
            dialogPanel.add(heightField);

            int option = JOptionPane.showConfirmDialog(null, dialogPanel,
                    "New Level", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                int width = ParametersUtils.parseIntOrDefault(widthField.getText(), 8);
                int height = ParametersUtils.parseIntOrDefault(heightField.getText(), 8);
                Gdx.app.postRunnable(() -> {
                    editor.setLevel(new Level(new Background(), new GameMap(width, height)));
                    app.getFileController().newFile();
                    CommandHistory.getInstance().clear();
                });
            }
        }));

        MenuItem openItem = new MenuItem("Open...");
        openItem.addActionListener(e -> EventQueue.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showDialog(null, "Open");
            if (option == APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Gdx.app.postRunnable(() -> {
                    app.getLevelEditor().stop();
                    app.getFileController().open(file.getPath());
                    CommandHistory.getInstance().clear();
                });
            }
        }));

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setEnabled(false);
        app.getFileController().addPathChangeListener(path -> saveItem.setEnabled(path != null));
        saveItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            app.getLevelEditor().stop();
            app.getFileController().save();
        }));

        MenuItem saveAsItem = new MenuItem("Save As...");
        saveAsItem.addActionListener(e -> EventQueue.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showDialog(null, "Save");
            if (option == APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Gdx.app.postRunnable(() -> {
                    app.getLevelEditor().stop();
                    app.getFileController().saveAs(file.getPath());
                });
            }
        }));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private Menu createEditMenu() {
        Menu editMenu = new Menu("Edit");

        CommandHistory commandHistory = CommandHistory.getInstance();

        MenuItem undoItem = new MenuItem("Undo");
        undoItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            if (commandHistory.canUndo()) {
                commandHistory.undo();
            }
        }));

        MenuItem redoItem = new MenuItem("Redo");
        redoItem.addActionListener(e -> Gdx.app.postRunnable(() -> {
            if (commandHistory.canRedo()) {
                commandHistory.redo();
            }
        }));

        editMenu.add(undoItem);
        editMenu.add(redoItem);

        return editMenu;
    }

    private void init() {
        app.getFileController().addPathChangeListener(path -> window.setTitle(path != null ? path : DEFAULT_TITLE));
        CommandHistory.getInstance().addHistoryChangeListener(() -> {
            String title = window.getTitle();
            if (title.charAt(0) != '*') {
                window.setTitle('*' + title);
            }
        });

        contentPane.add(new TopPanel(app), BorderLayout.NORTH);
        contentPane.add(new RightPanel(app.getLevelEditor()), BorderLayout.EAST);
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());

        app.getLevelEditor().addLevelPlayListener(() -> setMenuBarEnabled(false));
        app.getLevelEditor().addLevelStopListener(() -> setMenuBarEnabled(true));

        window.revalidate();
    }

    private void setMenuBarEnabled(boolean b) {
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            menuBar.getMenu(i).setEnabled(b);
        }
    }
}
