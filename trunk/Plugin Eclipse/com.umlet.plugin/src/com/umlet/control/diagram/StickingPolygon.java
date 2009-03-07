// The UMLet source code is distributed under the terms of the GPL; see license.txt
/*
 * Created on 13.08.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.control.diagram;

import java.awt.*;
import java.util.Vector;

public class StickingPolygon {
	
	private class StickLine
	{
		private Point p1;
		private Point p2;
		//private int stickinfo;
		
		private StickLine(Point p1, Point p2/*, int stickinfo*/) {
			this.p1 = p1;
			this.p2 = p2;
			//this.stickinfo = stickinfo;
		}
		
		private boolean equals(StickLine line) {
			if(p1.x == line.p1.x && p1.y == line.p1.y && p2.x == line.p2.x && p2.y == line.p2.y ||
					p1.x == line.p2.x && p1.y == line.p2.y && p2.x == line.p1.x && p2.y == line.p1.y)
				return true;
			return false;
		}
	}
	
	private Vector<StickLine> stick = new Vector<StickLine>();
	
	public void addLine(Point p1, Point p2/*, int stickingInfo*/) {
		//add a line with corresponding stickingInfo
		int pos=getLinePos(p1,p2);
		StickLine line = new StickLine(p1,p2/*,stickingInfo*/);
		if(pos<0) stick.add(line);
		else stick.setElementAt(line,pos); //replace
	}

/*	private int pyth(Point x1, Point x2) {
		int a = x1.x - x2.x;
		int b = x1.y - x2.y;
		return (int) Math.sqrt(a * a + b * b);
	}
	
	public int getResizeDirection(int x, int y) {
		Point p = new Point(x,y);
		int direct = 0;
		for (int i = 0; i < stick.size(); i++) {
			Point x1 = stick.get(i).p1;
			Point x2 = stick.get(i).p2;
		
			if (pyth(p, x1) + pyth(p, x2) > pyth(x1, x2) + 5)
                continue;
			
			//system origin translated to x1
			double p1x = x2.getX()-x1.getX();
			double p1y = x2.getY()-x1.getY();
			double p2x = p.getX()-x1.getX();
			double p2y = p.getY()-x1.getY();
			//constant - calculated constant by rotating line + calculation intersection point
			double c = (p1x*p2x+p1y*p2y)/(p1x*p1x+p1y*p1y);
			
			//intersection point
			double i1x = p1x * c;
			double i1y = p1y * c;
			
			//abstand
			double ax = i1x - p2x;
			double ay = i1y - p2y;
			double a = Math.sqrt(ax*ax+ay*ay);

			if(a < 5)
				direct = direct | stick.get(i).stickinfo;
		}
		return direct;
	}*/
	
	private int getLinePos(Point p1, Point p2) {
		for(int i=0; i < this.stick.size(); i++) {
			if(this.stick.get(i).equals(new StickLine(p1,p2/*,0*/))) {
			   	return i;
			}
		}
		return -1;
	}
	
	
	public int getStickingInfo(Point p1, Point p2) {
		//get 1 of the line p1#p2
		//returns -1 if matching line-points are not found
		int pos=-1;
		for(int i=0;i<stick.size();i++) {
			StickLine line = stick.elementAt(i);
			if(line.equals(new StickLine(p1,p2))) {
			   	pos=i;
			}
		}
		
		if(pos<0) 
			return -1;
		
		return 1;
	}
	
	public Point[][] getLines() {
		Point[][] lines = new Point[stick.size()][2];
		for(int i=0;i<stick.size();i++) {
			StickLine line = stick.elementAt(i);
			lines[i][0] = line.p1;
			lines[i][1] = line.p2;
		}
		return lines;
	}
	
}
