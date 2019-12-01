package com.github.nikitakuchur.puzzlegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.nikitakuchur.puzzlegame.PuzzleGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 800;
		config.width = 450;
		config.samples = 10;
		config.forceExit = false;

		new LwjglApplication(new PuzzleGame(), config);
	}
}