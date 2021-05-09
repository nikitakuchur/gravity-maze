package com.majakkagames.gravitymaze.editorcore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.editorcore.config.Configuration;
import com.majakkagames.gravitymaze.editorcore.config.Configurator;
import com.majakkagames.gravitymaze.editorcore.config.Modifier;
import com.majakkagames.gravitymaze.editorcore.panels.ResourcesPanel;
import com.majakkagames.gravitymaze.editorcore.panels.LevelPanel;
import com.majakkagames.gravitymaze.editorcore.panels.TopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class EditorFrame extends JFrame {

    private static final String DEFAULT_TITLE = "unnamed";

    private final Configurator configurator;
    private final EditorApplication app;

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenu editMenu = new JMenu("Edit");

    private final LevelPanel levelPanel = new LevelPanel();
    private final ResourcesPanel gameObjectsPanel;

    public EditorFrame(Configurator configurator) {
        this.configurator = configurator;
        app = new EditorApplication(configurator);

        gameObjectsPanel = new ResourcesPanel(configurator);

        setTitle('*' + DEFAULT_TITLE);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        setJMenuBar(menuBar);

        Canvas canvas = createCanvas();
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                menuBar.revalidate();
            }
        });
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                levelPanel,
                canvas
        );
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                splitPane,
                gameObjectsPanel
        );
        Component divider = splitPane2.getComponent(2);
        divider.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        splitPane2.setResizeWeight(1);

        contentPane.add(new TopPanel(app), BorderLayout.NORTH);
        contentPane.add(splitPane2, BorderLayout.CENTER);
        setContentPane(contentPane);

        Gdx.app.postRunnable(this::postInit);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_DELETE) {
                GameObjectSelector selector = app.getGameObjectSelector();
                if (!selector.hasSelectedObject()) return false;
                if (Configuration.isGameObjectLocked(configurator, selector.getSelectedObject().getClass())) {
                    return false;
                }
                selector.removeSelectedObject();
                app.getLevelEditor().getLevelManager().makeSnapshot();
            }
            return false;
        });
    }

    private void postInit() {
        LevelEditor editor = app.getLevelEditor();

        gameObjectsPanel.addSelectListener(editor::setGameObjectCreator);

        levelPanel.setLevelManager(editor.getLevelManager());

        GameObjectSelector selector = app.getGameObjectSelector();
        selector.addEventHandler(GameObjectSelector.EventType.SELECTED, e -> levelPanel.select(e.getGameObject()));
        levelPanel.addGameObjectSelectListener(selector::select);
        levelPanel.addGameObjectSelectListener(gameObject -> {
            Modifier modifier = Optional.ofNullable(gameObject)
                    .map(obj -> configurator.getModifier(gameObject.getClass()))
                    .orElse(null);
            if (modifier != null) {
                levelPanel.setModifier(modifier.getComponent(editor));
            } else {
                levelPanel.removeModifier();
            }
        });

        app.getLevelEditor().addEventHandler(LevelEditor.EventType.PLAY, level -> {
            fileMenu.setEnabled(false);
            editMenu.setEnabled(false);

            levelPanel.setEnabled(false);
            gameObjectsPanel.setEnabled(false);
        });
        app.getLevelEditor().addEventHandler(LevelEditor.EventType.STOP, level -> {
            fileMenu.setEnabled(true);
            editMenu.setEnabled(true);

            levelPanel.setEnabled(true);
            gameObjectsPanel.setEnabled(true);
        });
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

    private Canvas createCanvas() {
        LwjglAWTCanvas canvas = new LwjglAWTCanvas(app);
        return canvas.getCanvas();
    }

    @Override
    public void repaint(long time, int x, int y, int width, int height) {
        super.repaint(time, x, y, width, height);
    }
}
