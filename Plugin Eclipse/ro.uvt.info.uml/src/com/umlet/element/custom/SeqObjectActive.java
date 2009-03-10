package com.umlet.element.custom;

import java.awt.*;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class SeqObjectActive extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		Rectangle r=this.getBounds();
		Rectangle rf = new Rectangle(0,0,r.width-1,r.height-1);
		g2.setColor(_fillColor);
		g2.fill(rf);
		g2.setColor(_activeColor);
		g2.draw(rf);
	}
	public int getPossibleResizeDirections() { //allow height changes only
		return Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM;
	}
}
