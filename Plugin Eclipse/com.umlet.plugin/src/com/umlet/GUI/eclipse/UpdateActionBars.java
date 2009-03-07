package com.umlet.GUI.eclipse;

import org.eclipse.ui.IActionBars;

public class UpdateActionBars implements Runnable {

	private IActionBars bars;
	
	public UpdateActionBars(IActionBars bars) {
		this.bars = bars;
	}

	public void run() {
		this.bars.updateActionBars();
	}

}
