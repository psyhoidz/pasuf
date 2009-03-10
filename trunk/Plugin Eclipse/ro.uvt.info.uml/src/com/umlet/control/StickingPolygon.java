/*
 * Created on 13.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.umlet.control;

import java.awt.*;
import java.util.Vector;

/**
 * @author Ludwig
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StickingPolygon {
	private Vector<int[]> stick = new Vector<int[]>();
	public void addLine(Point p1, Point p2, int stickingInfo) {
		//add a line with corresponding stickingInfo
		int pos=getLinePos(p1,p2);
		int[] line = new int[5];
		line[0]=p1.x; line[1]=p1.y;
		line[2]=p2.x; line[3]=p2.y;
	    line[4]=stickingInfo;
		if(pos<0) stick.add(line);
		else stick.setElementAt(line,pos); //replace
	}

	private int getLinePos(Point p1, Point p2) {
		int pos=-1;
		for(int i=0;i<stick.size();i++) {
			int [] line = stick.elementAt(i);
			if((line[0]==p1.x && line[1]==p1.y && line[2]==p2.x && line[3]==p2.y)
					|| (line[0]==p2.x && line[1]==p2.y && line[2]==p1.x && line[3]==p1.y)) {
			   	pos=i;
			}
		}
		return pos;
	}
	
	
	public int getStickingInfo(Point p1, Point p2) {
		//get stickingInfo of the line p1#p2
		//returns -1 if matching line-points are not found
		int pos=-1;
		for(int i=0;i<stick.size();i++) {
			int [] line = stick.elementAt(i);
			if((line[0]==p1.x && line[1]==p1.y && line[2]==p2.x && line[3]==p2.y)
					|| (line[0]==p2.x && line[1]==p2.y && line[2]==p1.x && line[3]==p1.y)) {
			   	pos=i;
			}
		}
		if(pos<0) return -1;
		else {
			int[] line=stick.elementAt(pos);
			return line[4];
		}
	}
	
	public Point[][] getLines() {
		Point[][] lines = new Point[stick.size()][2];
		for(int i=0;i<stick.size();i++) {
			int line[] = stick.elementAt(i);
			lines[i][0] = new Point(line[0],line[1]);
			lines[i][1] = new Point(line[2],line[3]);
		}
		return lines;
	}
	
}
