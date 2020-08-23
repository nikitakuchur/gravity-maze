package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.editor.utils.GameObjectType;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameObjectsPanel extends JPanel {

    private final JList<GameObjectType> list = new JList<>(GameObjectType.values());

    private final transient List<Consumer<GameObjectType>> typeSelectListener = new ArrayList<>();

    public GameObjectsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        list.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(new JLabel("Game objects:"));
        add(scrollPane);
        list.addListSelectionListener(e ->
                typeSelectListener.forEach(listener -> listener.accept(list.getSelectedValue())));
    }

    public void addTypeSelectListener(Consumer<GameObjectType> consumer) {
        typeSelectListener.add(consumer);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        list.setEnabled(b);
    }
}
