package com.umlet.GUI.eclipse;

import java.awt.Panel;

public class RepaintGUI implements Runnable {

	private Panel panel;

	public RepaintGUI(Panel panel) {
		this.panel = panel;
	}

	public void run() {
		this.panel.repaint();
	}
}
