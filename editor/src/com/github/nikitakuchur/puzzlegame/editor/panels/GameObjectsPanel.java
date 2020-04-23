package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.utils.GameObjectType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameObjectsPanel extends JPanel {

    private final JList<GameObjectType> list = new JList<>(GameObjectType.values());

    private final transient List<Consumer<GameObjectType>> gameObjectSelectListener = new ArrayList<>();

    public GameObjectsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        list.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(list);
        add(new JLabel("Game objects:"));
        add(scrollPane);
        list.addListSelectionListener(e ->
                gameObjectSelectListener.forEach(listener -> listener.accept(list.getSelectedValue())));
    }

    public void addGameObjectSelectListener(Consumer<GameObjectType> consumer) {
        gameObjectSelectListener.add(consumer);
    }

    public void clearGameObjectsSelectionListeners() {
        gameObjectSelectListener.clear();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        list.setEnabled(b);
    }
}
