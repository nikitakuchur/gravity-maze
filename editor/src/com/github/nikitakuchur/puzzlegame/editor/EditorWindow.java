package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

import javax.swing.*;
import java.awt.*;

public class EditorWindow extends JFrame {

    private EditorApplication app;

    public EditorWindow() {
        setTitle("Editor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        app = new EditorApplication();
        LwjglAWTCanvas canvas = new LwjglAWTCanvas(app);

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
        DefaultComboBoxModel<Layer> model = new DefaultComboBoxModel<>();
        for (Layer value : Layer.values()) {
            model.addElement(value);
        }

        JComboBox<Layer> comboBox = new JComboBox<>(model);
        comboBox.addActionListener(actionEvent -> {
            EditableLevel level = app.getEditableLevel();
            Layer layer = (Layer) comboBox.getSelectedItem();
            level.setLayer(layer);
        });
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(comboBox));

        return panel;
    }
}
