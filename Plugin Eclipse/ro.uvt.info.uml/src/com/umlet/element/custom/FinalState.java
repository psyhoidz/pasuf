/*
 * Created on 29.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import com.umlet.control.Constants;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FinalState extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		g2.drawOval(0,0,this.getWidth()-1,this.getHeight()-1);
		g2.fillOval(4,4,this.getWidth()-8,this.getHeight()-8);
	}
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}
