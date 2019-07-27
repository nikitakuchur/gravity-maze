package com.puzzlegame.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class EditorLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 800;
        config.width = 600;
        config.samples = 10;
        config.forceExit = false;

        new LwjglApplication(new Editor(), config);
    }
}