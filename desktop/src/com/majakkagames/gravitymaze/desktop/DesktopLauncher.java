package com.majakkagames.gravitymaze.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.majakkagames.gravitymaze.game.GravityMaze;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 800;
        config.width = 450;
        config.samples = 12;
        config.forceExit = false;
        config.resizable = false;

        new LwjglApplication(new GravityMaze(), config);
    }
}
