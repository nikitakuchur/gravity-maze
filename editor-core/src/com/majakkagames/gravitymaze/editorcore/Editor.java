package com.majakkagames.gravitymaze.editorcore;

import com.majakkagames.gravitymaze.editorcore.config.Configurator;

import javax.swing.*;

public class Editor {

    private Editor() {
        throw new IllegalStateException("Utility class");
    }

    public static void launch(Configurator configurator) {
        SwingUtilities.invokeLater(() -> {
            EditorFrame window = new EditorFrame(configurator);
            window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            window.pack();
            window.setSize(1000, 800);
            window.setVisible(true);
        });
    }
}
