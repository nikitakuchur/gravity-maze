package com.github.nikitakuchur.puzzlegame.editor.panels;

import javax.swing.*;

public class GameObjectsPanel extends JPanel {

    public GameObjectsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JList<GameObjectType> list = new JList<>(GameObjectType.values());
        list.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(list);
        add(new JLabel("Game objects:"));
        add(scrollPane);
    }
}
