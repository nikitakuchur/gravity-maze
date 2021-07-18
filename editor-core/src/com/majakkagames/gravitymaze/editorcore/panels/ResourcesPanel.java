package com.majakkagames.gravitymaze.editorcore.panels;

import com.majakkagames.gravitymaze.editorcore.config.Configurator;
import com.majakkagames.gravitymaze.editorcore.config.GameObjectCreator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("java:S1948")
public class ResourcesPanel extends JPanel {

    private final JList<GameObjectCreator> list;

    private final List<Consumer<GameObjectCreator>> selectListener = new ArrayList<>();

    public ResourcesPanel(Configurator configurator) {
        list = new JList<>(configurator.getGameObjectCreators().toArray(new GameObjectCreator[0]));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(200, 500));
        add(new JLabel("Resources:"));
        add(scrollPane);
        list.addListSelectionListener(e -> selectListener.forEach(listener -> listener.accept(list.getSelectedValue())));
    }

    public void addSelectListener(Consumer<GameObjectCreator> consumer) {
        selectListener.add(consumer);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        list.setEnabled(b);
    }
}
