package com.triateq.gravitymaze.editor;

import javax.swing.*;

public class EditorLauncher {
	public static void main (String[] arg) {
		SwingUtilities.invokeLater(EditorWindow::new);
	}
}
