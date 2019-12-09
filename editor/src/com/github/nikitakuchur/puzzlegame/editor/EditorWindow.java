package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EditorWindow extends JFrame {

    private final List<String> layers = Arrays.asList("background", "map", "gameObjects");

    public EditorWindow() {
        setTitle("Editor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(new EditorApplication());

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas.getCanvas(), BorderLayout.CENTER);
        contentPane.add(createTopPanel(), BorderLayout.NORTH);
        contentPane.add(createRightPanel(), BorderLayout.EAST);

        setContentPane(contentPane);
        pack();
        setSize(800, 800);
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        JPanel panel = new JPanel();
        panel.add(playButton);
        panel.add(stopButton);

        return panel;
    }

    private JPanel createRightPanel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        layers.forEach(model::addElement);

        JComboBox<String> comboBox = new JComboBox<>(model);
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(comboBox));

        return panel;
    }
}
