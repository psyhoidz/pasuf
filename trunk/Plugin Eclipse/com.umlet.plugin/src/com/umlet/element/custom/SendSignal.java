// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 29.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Vector;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")
public class SendSignal extends Entity {
	public void paintEntity(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
		g2.setColor(_activeColor);
		this.handler.getFRC(g2);

		Polygon poly = new Polygon();
		poly.addPoint(0,0);
		poly.addPoint(this.getWidth()-this.handler.getFontsize(),0);
		poly.addPoint(this.getWidth()-1,this.getHeight()/2);
		poly.addPoint(this.getWidth()-this.handler.getFontsize(),this.getHeight()-1);
		poly.addPoint(0, this.getHeight()-1);
		
		g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillPolygon(poly);
	    g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
		g2.drawPolygon(poly);
		
		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=this.getHeight()/2-tmp.size()*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			yPos+=this.handler.getFontsize();
			this.handler.writeText(g2,s,(int)this.getWidth()/2, yPos, true);
			yPos+=this.handler.getDistTextToText();
		}
		//g2.drawLine(0,0,this.getWidth()-this.handler.getFontsize(),0);
		//g2.drawLine(this.getWidth()-this.handler.getFontsize(), this.getHeight()-1, 0, this.getHeight()-1);
		//g2.drawLine(this.getWidth()-this.handler.getFontsize(),0,this.getWidth()-1,this.getHeight()/2);
		//g2.drawLine(this.getWidth(),this.getHeight()/2,this.getWidth()-this.handler.getFontsize(),this.getHeight());
		//g2.drawLine(0, this.getHeight()-1, 0, 0);
	}
	
	public StickingPolygon getStickingBorder() {
	    StickingPolygon p = new StickingPolygon();
	    p.addLine(new Point(0,0), new Point(this.getWidth()-this.handler.getFontsize(),0));
	    p.addLine(new Point(this.getWidth()-this.handler.getFontsize(), this.getHeight()-1), new Point(0, this.getHeight()-1));
	    p.addLine(new Point(this.getWidth()-this.handler.getFontsize(),0), new Point(this.getWidth()-1,this.getHeight()/2));
	    p.addLine(new Point(this.getWidth(),this.getHeight()/2), new Point(this.getWidth()-this.handler.getFontsize(),this.getHeight()));
	    p.addLine(new Point(0, this.getHeight()-1), new Point(0, 0));
	    return p;
	}
}
