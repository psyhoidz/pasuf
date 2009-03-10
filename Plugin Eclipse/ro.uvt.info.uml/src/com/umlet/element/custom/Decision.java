package com.umlet.element.custom;

import java.awt.*;

import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class Decision extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		g2.drawLine(this.getWidth()/2,0,this.getWidth(),this.getHeight()/2);
		g2.drawLine(this.getWidth()/2,0,0,this.getHeight()/2);
		g2.drawLine(0,this.getHeight()/2,this.getWidth()/2,this.getHeight());
		g2.drawLine(this.getWidth()-1,this.getHeight()/2,this.getWidth()/2-1,this.getHeight());
	}
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}
