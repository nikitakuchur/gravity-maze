package com.github.nikitakuchur.puzzlegame.editor;

import javax.swing.*;

public class EditorLauncher {
	public static void main (String[] arg) {
		SwingUtilities.invokeLater(EditorWindow::new);
	}
}