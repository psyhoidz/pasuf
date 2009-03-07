// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class ThreeWayRelation extends com.umlet.element.base.Entity {

	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor); 
		this.handler.getFRC(g2);

		Polygon p = new Polygon();
		p.addPoint((int)this.getWidth()/2,0);
		p.addPoint((int)this.getWidth(),(int)this.getHeight()/2);
		p.addPoint((int)this.getWidth()/2,(int)this.getHeight()-1);
		p.addPoint(0,(int)this.getHeight()/2);
		
		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillPolygon(p);
	    g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
		
		g2.drawPolygon(p);
		
		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=this.handler.getDistLineToText();
		for (int i=0; i<tmp.size(); i++) {
			String s=(String)tmp.elementAt(i);
			yPos+=this.handler.getFontsize();
			this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
			yPos+=this.handler.getDistTextToText();
		}
	}


	public int getPossibleResizeDirections() {
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}

	public StickingPolygon getStickingBorder() {
		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(this.getWidth()/2,0), new Point(this.getWidth(),this.getHeight()/2));
		p.addLine(new Point(this.getWidth(),this.getHeight()/2), new Point(this.getWidth()/2,this.getHeight()));
		p.addLine(new Point(this.getWidth()/2,this.getHeight()), new Point(0,this.getHeight()/2));
		p.addLine(new Point(0,this.getHeight()/2), new Point(this.getWidth()/2,0));
		return p;
	}
}
