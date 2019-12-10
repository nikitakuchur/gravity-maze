package com.github.nikitakuchur.puzzlegame.editor.panels;

import javax.swing.*;

public class TopPanel extends JPanel {

    public TopPanel() {
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");
        add(playButton);
        add(stopButton);
    }
}
