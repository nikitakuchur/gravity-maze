package com.majakkagames.gravitymaze.editorcore.panels;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.editorcore.config.Configuration;
import com.majakkagames.gravitymaze.editorcore.LevelManager;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class LevelPanel extends JPanel {

    private LevelManager levelManager;

    private final DefaultListModel<GameObject> gameObjectsModel = new DefaultListModel<>();
    private final JList<GameObject> gameObjects = new JList<>(gameObjectsModel);

    private final GameObjectPanel gameObjectPanel = new GameObjectPanel(null);

    private final JSplitPane splitPane;
    private boolean hasModifier = false;

    private final List<Consumer<GameObject>> gameObjectSelectListeners = new ArrayList<>();

    public LevelPanel() {
        gameObjects.addListSelectionListener(e -> {
            GameObject gameObject = gameObjects.getSelectedValue();
            gameObjectPanel.setGameObject(gameObject);
            gameObjectSelectListeners.forEach(listener -> listener.accept(gameObject));
        });
        gameObjectPanel.addTableModelListener(e -> {
            gameObjects.repaint();
            if (levelManager == null) throw new IllegalStateException("Level manager must be initialized!");
            if (e.getType() == TableModelEvent.UPDATE) {
                levelManager.makeSnapshot();
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        gameObjects.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(gameObjects);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(200, 500));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, scrollPane, gameObjectPanel);
        splitPane.setResizeWeight(0.3);

        add(new JLabel("Level:"));
        add(splitPane);
    }

    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
        init();
        levelManager.addEventHandler(LevelManager.EventType.CHANGED, event -> init());
        levelManager.addEventHandler(LevelManager.EventType.SNAPSHOT, event -> gameObjectPanel.update());
    }

    private void init() {
        gameObjectsModel.clear();
        GameObjectStore store = levelManager.getLevel().getGameObjectStore();
        store.getGameObjects()
                .stream()
                .filter(gameObject -> !Configuration.isGameObjectTransient(gameObject.getClass()))
                .sorted(Comparator.comparingInt(GameObject::getLayer))
                .forEach(gameObjectsModel::addElement);
        store.addEventHandler(GameObjectStore.EventType.ADDED, e -> gameObjectsModel.addElement(e.getGameObject()));
        store.addEventHandler(GameObjectStore.EventType.REMOVED, e -> gameObjectsModel.removeElement(e.getGameObject()));
    }

    public void select(GameObject gameObject) {
        int index = gameObjectsModel.indexOf(gameObject);
        if (index < 0) {
            gameObjects.clearSelection();
        } else {
            gameObjects.setSelectedIndex(index);
        }
    }

    public void setModifier(Component component) {
        if (hasModifier) return;
        remove(splitPane);
        JSplitPane modSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, splitPane, component);
        Component divider = modSplitPane.getComponent(2);
        divider.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        modSplitPane.setResizeWeight(1);
        add(modSplitPane);
        revalidate();
        hasModifier = true;
    }

    public void removeModifier() {
        if (!hasModifier) return;
        remove(getComponentCount() - 1);
        add(splitPane);
        revalidate();
        hasModifier = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        gameObjects.setEnabled(enabled);
        gameObjectPanel.setGameObject(null);
        gameObjectPanel.setEnabled(enabled);
        removeModifier();
    }

    public void addGameObjectSelectListener(Consumer<GameObject> consumer) {
        gameObjectSelectListeners.add(consumer);
    }
}
