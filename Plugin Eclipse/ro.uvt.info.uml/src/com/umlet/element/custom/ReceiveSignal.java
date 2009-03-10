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
import java.util.Vector;
import com.umlet.control.Constants;
import com.umlet.control.StickingPolygon;
import com.umlet.element.base.Entity;

/**
 * @author Ludwig
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReceiveSignal extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=this.getHeight()/2-tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText())/2;

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			yPos+=Constants.getFontsize();
			Constants.write(g2,s,(int)this.getWidth()/2, yPos, true);
			yPos+=Constants.getDistTextToText();
		}
		g2.drawLine(0,0,this.getWidth(),0);
		g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
		g2.drawLine(0,0,Constants.getFontsize()-2,this.getHeight()/2);
		g2.drawLine(Constants.getFontsize()-2,this.getHeight()/2,0,this.getHeight());
		g2.drawLine(this.getWidth()-1, this.getHeight()-1, this.getWidth()-1, 0);

	}
	
/*	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	    int ret=0;
	    int tmpX=p.x-this.getX();
	    int tmpY=p.y-this.getY();

	    if (tmpX>-4 && tmpX<this.getWidth()+4) {
	        if (tmpY>-4 && tmpY<4) ret+=1;
	        if (tmpY>this.getHeight()-4 && tmpY<this.getHeight()+4) ret+=4;
	      }
	    if (tmpY>-4 && tmpY<this.getHeight()+4) {
	        if (tmpX>+4 && tmpX<12 && tmpY>this.getHeight()/2-4 && tmpY<this.getHeight()/2+4) ret+=8;
	        if (tmpX>this.getWidth()-4 && tmpX<this.getWidth()+4) ret+=2;
	      }
	    return ret;
	  }*/
	public StickingPolygon getStickingBorder() {
	    StickingPolygon p = new StickingPolygon();
	    p.addLine(new Point(0,0), new Point(this.getWidth(),0),Constants.RESIZE_TOP);
	    p.addLine(new Point(this.getWidth()-1,this.getHeight()-1), new Point(0,this.getHeight()-1),Constants.RESIZE_BOTTOM);
	    p.addLine(new Point(0,0), new Point(Constants.getFontsize()-2,this.getHeight()/2),Constants.RESIZE_LEFT);
	    p.addLine(new Point(Constants.getFontsize()-2,this.getHeight()/2), new Point(0,this.getHeight()),Constants.RESIZE_LEFT);
	    p.addLine(new Point(this.getWidth()-1, 0), new Point(this.getWidth()-1, this.getHeight()-1),Constants.RESIZE_RIGHT);
	    return p;
	  }
}
