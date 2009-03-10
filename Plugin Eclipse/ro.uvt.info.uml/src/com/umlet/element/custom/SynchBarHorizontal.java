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
public class SynchBarHorizontal extends Entity {
	private static int textWidth=0;
	
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		textWidth=0; //reset
		Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=this.getHeight()/2-tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText())/2;
		boolean ADAPT_SIZE_X=false;
		int textHeight=tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText());
		
		for (int i=0; i<tmp.size(); i++) {
	    	String s=(String)tmp.elementAt(i);

	    	TextLayout l=new TextLayout(s, Constants.getFont(), Constants.getFRC(g2));
	    	Rectangle2D r2d=l.getBounds();
	    	textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth);

	    	if ((this.getWidth()-textWidth)<0) { ADAPT_SIZE_X=true; break; }
	    	yPos+=Constants.getFontsize();
	    	Constants.write(g2, s, 0, yPos, false);
	    	yPos+=Constants.getDistTextToText();
		}

		if (ADAPT_SIZE_X) {
			Controller.getInstance().executeCommandWithoutUndo(new Resize(this,8,-Umlet.getInstance().getMainUnit(),0));
			Controller.getInstance().executeCommandWithoutUndo(new Resize(this,2,Umlet.getInstance().getMainUnit(),0));
			return;
		}
		
		if (textHeight>this.getHeight()) {
		  Controller.getInstance().executeCommandWithoutUndo(new Resize(this,4,0,20));
		  return;
		}

		g2.fillRect(textWidth+Constants.getDistTextToLine(),getHeight()/2-3,this.getWidth()-textWidth-Constants.getDistTextToLine()*2,5);
	}
	
/*	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret=0;
		int tmpX=p.x-this.getX();
		int tmpY=p.y-this.getY();
	    
	    if (tmpX>(textWidth+4) && tmpX<this.getWidth()+4) {
	        //if (tmpY>0 && tmpY<8) ret+=1; 
	        if (tmpY>this.getHeight()/2-8 && tmpY<this.getHeight()/2+8) ret+=4;
	    }
		return ret;
	}*/
	public StickingPolygon getStickingBorder() {
	    StickingPolygon p = new StickingPolygon();
	    p.addLine(new Point(textWidth,getHeight()/2-1), new Point(this.getWidth()-1,getHeight()/2-1),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    return p;
	}
	
	public int getPossibleResizeDirections() { //allow width changes only
		return Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT;
	}
}
