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
public class SynchBarVertical extends Entity {
	private static int textHeight=0;
	
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		int yPos=0;
		textHeight=0; //reset

		Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		textHeight=tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText());
		boolean ADAPT_SIZE=false;

	    for (int i=0; i<tmp.size(); i++) {
	      String s=(String)tmp.elementAt(i);
	        yPos+=Constants.getFontsize();
	        TextLayout l=new TextLayout(s, Constants.getFont(), Constants.getFRC(g2));
	        Rectangle2D r2d=l.getBounds();
	        int width=(int)r2d.getWidth();
	        if ((this.getWidth()/2-width/2)<0) { ADAPT_SIZE=true; break; }
	        Constants.write(g2, s, this.getWidth()/2, yPos, true);
	        yPos+=Constants.getDistTextToText();
	    }

	    if (ADAPT_SIZE) {
	      Controller.getInstance().executeCommandWithoutUndo(new Resize(this,8,-Umlet.getInstance().getMainUnit(),0));
	      Controller.getInstance().executeCommandWithoutUndo(new Resize(this,2,Umlet.getInstance().getMainUnit(),0));
	      return;
	    }
	    if (yPos>this.getHeight()) {
	      Controller.getInstance().executeCommandWithoutUndo(new Resize(this,4,0,Constants.getFontsize()+Constants.getDistTextToText()));
	      return;
	    }

	    g2.fillRect(this.getWidth()/2-3,textHeight+Constants.getDistTextToLine(),5,this.getHeight()-textHeight-Constants.getDistTextToLine()*2);
	}
	
/*	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret=0;
		int tmpX=p.x-this.getX();
		int tmpY=p.y-this.getY();

		if (tmpY>textHeight+4 && tmpY<this.getHeight()+4) {
			//if (tmpX>0 && tmpX<16) ret+=8;
			if (tmpX>this.getWidth()/2-4 && tmpX<this.getWidth()/2+4) ret+=2;
		}
		return ret;
	}*/
	
	public StickingPolygon getStickingBorder() {
	    StickingPolygon p = new StickingPolygon();
	    p.addLine(new Point(this.getWidth()/2-1,textHeight), new Point(this.getWidth()/2-1,this.getHeight()-1),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    return p;
	  }

	public int getPossibleResizeDirections() { //allow height changes only
		return Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM;
	}
}
