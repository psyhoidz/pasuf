package com.umlet.listeners;

import java.awt.event.MouseEvent;

import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.PaletteHandler;

public class PaletteListener extends DiagramListener {
	
	public PaletteListener(PaletteHandler handler) {
		super(handler);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		((DrawPanel)me.getComponent()).getSelector().deselectAllWithoutUpdatePropertyPanel();
	}

	@Override
	public void mouseDragged(MouseEvent me) {

	}
}
