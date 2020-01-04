package com.github.nikitakuchur.puzzlegame.editor.panels;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameObjectsPanel extends JPanel {

    private JList<GameObjectType> list = new JList<>(GameObjectType.values());

    private transient List<Runnable> gameObjectsListeners = new ArrayList<>();

    public GameObjectsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        list.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(list);
        add(new JLabel("Game objects:"));
        add(scrollPane);
        list.addListSelectionListener(e -> gameObjectsListeners.forEach(Runnable::run));
    }

    public void addGameObjectsListener(Runnable gameObjectsListener) {
        gameObjectsListeners.add(gameObjectsListener);
    }

    public GameObjectType getSelectedGameObjectType() {
        return list.getSelectedValue();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        list.setEnabled(b);
    }
}
