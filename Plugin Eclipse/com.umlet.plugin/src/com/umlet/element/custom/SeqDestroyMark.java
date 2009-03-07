// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;

import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class SeqDestroyMark extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		Rectangle r=this.getBounds();
		g2.drawLine(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
		g2.drawLine((int)r.getWidth()-1,0,0,(int)r.getHeight()-1);
	}
	
	public StickingPolygon getStickingBorder() {
	    StickingPolygon p = new StickingPolygon();
	    int x,y;
		x=this.getWidth()/2;
		y=this.getHeight()/2;
	    p.addLine(new Point(x-4,y-4), new Point(x+4,y-4));
	    p.addLine(new Point(x+4,y-4), new Point(x+4,y+4));
	    p.addLine(new Point(x+4,y+4), new Point(x-4,y+4));
	    p.addLine(new Point(x-4,y+4), new Point(x-4,y-4));
	    return p;
	  }
	
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}
