package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.github.nikitakuchur.puzzlegame.game.Background;

import javax.swing.*;

public class BackgroundPanel extends JPanel {

    public BackgroundPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        JComboBox<String> themeComboBox = new JComboBox<>();

        themeComboBox.setModel(model);
        Background.getBackgroundNames().forEach(model::addElement);
        add(new JScrollPane(themeComboBox));
    }
}
