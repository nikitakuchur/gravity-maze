package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.github.nikitakuchur.puzzlegame.editor.panels.RightPanel;
import com.github.nikitakuchur.puzzlegame.editor.panels.TopPanel;
import com.github.nikitakuchur.puzzlegame.editor.utils.LevelUtils;

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
        open.addActionListener(e -> Gdx.app.postRunnable(() -> LevelUtils.load(app)));

        MenuItem save = new MenuItem("Save");
        save.addActionListener(e -> Gdx.app.postRunnable(() -> LevelUtils.save(app)));

        MenuItem saveAs = new MenuItem("Save As...");
        saveAs.addActionListener(e -> Gdx.app.postRunnable(() -> LevelUtils.save(app)));

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);

        return file;
    }

    private void init() {
        contentPane.add(new TopPanel(app), BorderLayout.NORTH);
        contentPane.add(new RightPanel(app.getLevelEditor()), BorderLayout.EAST);
        menuBar.add(createFileMenu());
        window.revalidate();
    }
}
