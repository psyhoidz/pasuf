package com.umlet.element.custom;

import java.awt.*;

import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class SeqDestroyMark extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		Rectangle r=this.getBounds();
		g2.drawLine(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
		g2.drawLine((int)r.getWidth()-1,0,0,(int)r.getHeight()-1);
	}
	
	/*public int doesCoordinateAppearToBeConnectedToMe(Point p) {
		int ret=0;
		int tmpX=p.x-this.getX();
		int tmpY=p.y-this.getY();

		if (tmpX>-2 && tmpX<this.getWidth()+2) {
			if(tmpY>this.getHeight()/2-4 && tmpY<this.getHeight()/2+4) ret+=1;
		}
		if (tmpY>-2 && tmpY<this.getHeight()+2) {
			if(tmpX>this.getWidth()/2-4 && tmpX<this.getWidth()/2+4) ret+=1;
		}
		return ret;
	}*/
	public StickingPolygon getStickingBorder() {
	    StickingPolygon p = new StickingPolygon();
	    int x,y;
		x=this.getWidth()/2;
		y=this.getHeight()/2;
	    p.addLine(new Point(x-4,y-4), new Point(x+4,y-4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    p.addLine(new Point(x+4,y-4), new Point(x+4,y+4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    p.addLine(new Point(x+4,y+4), new Point(x-4,y+4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    p.addLine(new Point(x-4,y+4), new Point(x-4,y-4),Constants.RESIZE_LEFT | Constants.RESIZE_RIGHT | Constants.RESIZE_TOP | Constants.RESIZE_BOTTOM);
	    return p;
	  }
	
	public int getPossibleResizeDirections() {return 0;} //deny size changes
}
