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
import com.majakkagames.gravitymaze.editorcore.utils.TitleUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.majakkagames.gravitymaze.editorcore.LevelManager.*;
import static javax.swing.JFileChooser.APPROVE_OPTION;

@SuppressWarnings("java:S1948")
public class EditorFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Configurator configurator;
    private final EditorApplication app;

    private final TitleUpdater titleUpdater;

    private final EditorMenuBar menu;

    private final LevelPanel levelPanel = new LevelPanel();
    private final ResourcesPanel gameObjectsPanel;

    public EditorFrame(Configurator configurator) {
        this.configurator = configurator;
        app = new EditorApplication(configurator);

        gameObjectsPanel = new ResourcesPanel(configurator);

        titleUpdater = new TitleUpdater(this::setTitle);

        menu = new EditorMenuBar(configurator, app);
        setJMenuBar(menu);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        Canvas canvas = createCanvas();
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                menu.revalidate();
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

        app.getFileController().addPathChangeListener(path -> {
            titleUpdater.update(path);
            titleUpdater.showChangeIndicator(false);
        });
        app.getLevelEditor().getLevelManager().addEventHandler(EventType.SNAPSHOT, e -> titleUpdater.showChangeIndicator(true));

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

        editor.addEventHandler(LevelEditor.EventType.PLAY, level -> {
            menu.setEnabled(false);
            levelPanel.setEnabled(false);
            gameObjectsPanel.setEnabled(false);
        });
        editor.addEventHandler(LevelEditor.EventType.STOP, level -> {
            menu.setEnabled(true);
            levelPanel.setEnabled(true);
            gameObjectsPanel.setEnabled(true);
        });
    }

    private Canvas createCanvas() {
        LwjglAWTCanvas canvas = new LwjglAWTCanvas(app);
        return canvas.getCanvas();
    }
}
