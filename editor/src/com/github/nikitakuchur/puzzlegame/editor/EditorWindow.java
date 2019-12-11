package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.github.nikitakuchur.puzzlegame.editor.panels.LayerPanel;
import com.github.nikitakuchur.puzzlegame.editor.panels.TopPanel;

import javax.swing.*;
import java.awt.*;

public class EditorWindow extends JFrame {

    private transient EditorApplication app;

    private JPanel contentPane = new JPanel();

    public EditorWindow() {
        setTitle("Editor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        app = new EditorApplication();
        LwjglAWTCanvas canvas = new LwjglAWTCanvas(app);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas.getCanvas(), BorderLayout.CENTER);
        app.addOnCreateListener(this::init);
        setContentPane(contentPane);

        pack();
        setSize(800, 800);
        setVisible(true);
    }

    private void init() {
        contentPane.add(new TopPanel(), BorderLayout.NORTH);
        contentPane.add(new LayerPanel(app), BorderLayout.EAST);
        revalidate();
    }
}
