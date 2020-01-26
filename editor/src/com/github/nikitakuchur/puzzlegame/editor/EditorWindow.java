package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.github.nikitakuchur.puzzlegame.editor.panels.RightPanel;
import com.github.nikitakuchur.puzzlegame.editor.panels.TopPanel;

import javax.swing.*;
import java.awt.*;

public class EditorWindow {

    private JFrame window = new JFrame("Editor");
    private EditorApplication app = new EditorApplication();

    private MenuBar menuBar = new MenuBar();
    private JPanel contentPane = new JPanel();

    public EditorWindow() {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LwjglAWTCanvas canvas = new LwjglAWTCanvas(app);

        window.setMenuBar(menuBar);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas.getCanvas(), BorderLayout.CENTER);
        canvas.postRunnable(this::init);
        window.setContentPane(contentPane);

        window.pack();
        window.setSize(800, 800);
        window.setVisible(true);
    }

    private Menu createFileMenu()
    {
        Menu file = new Menu("File");
        MenuItem open = new MenuItem("Open...");
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As...");
        MenuItem exit = new MenuItem("Exit");
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);

        open.addActionListener(e -> System.out.println ("ActionListener.actionPerformed : open"));
        return file;
    }

    private void init() {
        contentPane.add(new TopPanel(app), BorderLayout.NORTH);
        contentPane.add(new RightPanel(app.getLevelEditor()), BorderLayout.EAST);
        menuBar.add(createFileMenu());
        window.revalidate();
    }
}
