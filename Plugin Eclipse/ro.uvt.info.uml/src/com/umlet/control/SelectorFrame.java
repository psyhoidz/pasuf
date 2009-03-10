/*
 * Created on 21.07.2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.umlet.control;

import java.awt.*;

import javax.swing.*;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelectorFrame extends JComponent {

	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(Color.black);
		g2.setStroke(Constants.getStroke(1,1));
		g2.drawRect(0,0, getWidth()-1, getHeight()-1);
	}

}
