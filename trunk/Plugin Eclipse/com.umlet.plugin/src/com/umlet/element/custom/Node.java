// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.util.Vector;
import java.awt.Point;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.Entity;

@SuppressWarnings("serial")
public class Node extends Entity {
	
	public Node()
	{
		super();
	}

	public void paintEntity (Graphics g)
	{	
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		Composite[] composites = colorize(g2); //enable colors
	    g2.setColor(_activeColor);
	    this.handler.getFRC(g2);
	    
	    int size_3d = Math.min(this.getHeight()/10, this.getWidth()/10);
	    g2.setComposite(composites[1]);
	    g2.setColor(_fillColor);
	    g2.fillRect(0,size_3d,this.getWidth()-size_3d-1,this.getHeight()-size_3d-1);
	    g2.setComposite(composites[0]);
	    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
		
		Polygon p = new Polygon();
		p.addPoint(this.getWidth()-size_3d-1,this.getHeight()-1);
		p.addPoint(this.getWidth()-size_3d-1,size_3d);
		p.addPoint(this.getWidth()-1,0);
		p.addPoint(this.getWidth()-1,this.getHeight()-size_3d-1);
		
		Polygon p1 = new Polygon();
		p1.addPoint(0,size_3d);
		p1.addPoint(size_3d,0);
		p1.addPoint(this.getWidth()-1,0);
		p1.addPoint(this.getWidth()-size_3d-1,size_3d);
		
		g2.setColor(new Color(240,240,240));
		g2.fillPolygon(p);
		g2.fillPolygon(p1);
		g2.setColor(_activeColor);
		
		g2.drawRect(0,size_3d,this.getWidth()-size_3d-1,this.getHeight()-size_3d-1);
		//draw polygons by hand to avoid double painted line
		g2.drawLine(0, size_3d, size_3d, 0);
		g2.drawLine(size_3d,0,this.getWidth()-1,0);
		g2.drawLine(this.getWidth()-1,0,this.getWidth()-1,this.getHeight()-size_3d-1);
		g2.drawLine(this.getWidth()-1,this.getHeight()-size_3d-1, this.getWidth()-size_3d-1, this.getHeight()-1);
		g2.drawLine(this.getWidth()-size_3d-1,size_3d, this.getWidth()-1, 0);
		
		 Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		    int yPos=this.handler.getDistLineToText();
		    yPos = yPos+size_3d;
		    for (int i=0; i<tmp.size(); i++) {
		      String s=(String)tmp.elementAt(i);
		      yPos+=this.handler.getFontsize();
		      if (s.startsWith("center:"))
		      {
		    	  s = s.substring(7);
		    	  this.handler.writeText(g2,s,(this.getWidth()-size_3d-1)/2, yPos, true);
		      }
		      else this.handler.writeText(g2,s,this.handler.getFontsize()/2, yPos, false);
		      
		      yPos+=this.handler.getDistTextToText();
		    }
		
	    
	}

	public StickingPolygon getStickingBorder()
	{
		int size_3d = Math.min(this.getHeight()/10, this.getWidth()/10);
		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(0,size_3d), new Point(0,this.getHeight()));
		p.addLine(new Point(0,this.getHeight()), new Point(this.getWidth()-size_3d-1, this.getHeight()));
		p.addLine(new Point(this.getWidth()-size_3d-1, this.getHeight()), new Point(this.getWidth(),this.getHeight()-size_3d-1));
		p.addLine(new Point(this.getWidth(),this.getHeight()-size_3d-1),new Point(this.getWidth(),0));
		p.addLine(new Point(size_3d,0), new Point(this.getWidth(),0));
		p.addLine(new Point(size_3d,0), new Point(0, size_3d));
		return p;
	}
}