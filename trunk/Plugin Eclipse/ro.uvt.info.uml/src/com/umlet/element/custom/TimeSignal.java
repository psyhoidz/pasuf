/*
 * Created on 29.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import com.umlet.control.Constants;
import com.umlet.control.Controller;
import com.umlet.control.Resize;
import com.umlet.control.StickingPolygon;
import com.umlet.control.Umlet;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TimeSignal extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);
		boolean ADAPT_SIZE=false;

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		yPos+=4*Umlet.getInstance().getMainUnit();
		yPos+=Constants.getDistLineToText();

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			if (s.equals("--")) {
				yPos+=Constants.getDistTextToLine();
				g2.drawLine(this.getWidth()/2-Constants.getFontsize()*4,yPos,this.getWidth()/2+Constants.getFontsize()*4,yPos);
				yPos+=Constants.getDistLineToText();
			} else {
				yPos+=Constants.getFontsize();
				TextLayout l=new TextLayout(s, Constants.getFont(), Constants.getFRC(g2));
				Rectangle2D r2d=l.getBounds();
				int width=(int)r2d.getWidth();
				int xPos=this.getWidth()/2-width/2;
				if (xPos<0) { ADAPT_SIZE=true; break; }
				Constants.write(g2, s, this.getWidth()/2, yPos, true);
				yPos+=Constants.getDistTextToText();
			}
		}

		if (ADAPT_SIZE) {
			Controller.getInstance().executeCommandWithoutUndo(new Resize(this,8,-Umlet.getInstance().getMainUnit(),0));
			Controller.getInstance().executeCommandWithoutUndo(new Resize(this,2,Umlet.getInstance().getMainUnit(),0));
			return;
		}
		if (yPos>this.getHeight()) {
			Controller.getInstance().executeCommandWithoutUndo(new Resize(this,4,0,20));
			return;
		}

		int x0,y0,b,h;
		x0=this.getWidth()/2-Umlet.getInstance().getMainUnit()-10;
		y0=0;
		b=this.getWidth()/2-Umlet.getInstance().getMainUnit()+2*Umlet.getInstance().getMainUnit()+10;
		h=2*Umlet.getInstance().getMainUnit()+20;
    
		g2.drawLine(x0,y0,b,y0);
		g2.drawLine(b, h, x0, h);
		g2.drawLine(x0,y0,b, h);
		g2.drawLine(b,y0,x0, h);
	}
	
/*	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int tmpX=p.x-this.getX()-this.getWidth()/2;
		int tmpY=p.y-this.getY()-(2*Umlet.getInstance().getMainUnit()+20)/2;
		
		if ((tmpX>-4 && tmpX<+4)&&(tmpY>-4 && tmpY<+4)) {
			return 15;
		} else return 0;
	} */
	public StickingPolygon getStickingBorder() {
	    StickingPolygon p = new StickingPolygon();
	    int x,y;
		x=this.getWidth()/2;
		y=(2*Umlet.getInstance().getMainUnit()+20)/2;
	    p.addLine(new Point(x-4,y-4), new Point(x+4,y-4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    p.addLine(new Point(x+4,y-4), new Point(x+4,y+4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    p.addLine(new Point(x+4,y+4), new Point(x-4,y+4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    p.addLine(new Point(x-4,y+4), new Point(x-4,y-4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    return p;
	  }
	
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}