package com.umlet.element.custom;
// Some import to have access to more Java features
import java.awt.*;
import java.awt.geom.Area;
import java.util.*;

import com.umlet.control.*;
import com.umlet.element.base.Entity;
import com.umlet.element.base.detail.RelationLinePoint;

public class Taxonomy_of_Workprocesses extends com.umlet.element.base.Entity {

	// Change this method if you want to edit the graphical
	// representation of your custom element.
public void paint(Graphics g) {

    // Some unimportant initialization stuff; setting color, font
    // quality, etc. You should not have to change this.
    Graphics2D g2=(Graphics2D) g; g2.setFont(Constants.getFont());
    g2.setColor(_activeColor); Constants.getFRC(g2);


    // It's getting interesting here:
    // First, the strings you type in the element editor are read and
    // split into lines.
    // Then, by default, they are printed out on the element, aligned
    // to the left.
    // Change this to modify this default text printing and to react
    // to special strings
    // (like the "--" string in the UML class elements which draw a line).
    Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    
    boolean root = true;
    int enlarged = 0;
    int wordLength =0;
    int level = 0;
    int yPos=10;
    int dist = 10*Constants.getDistTextToText();
    int ovalHeight = 3*Constants.getFontsize();
    int ovalWidth = 10*Constants.getFontsize();
    int in = (int)(ovalWidth*0.5);
    Point nextDock = new Point(yPos+ovalWidth/2 ,10+ovalHeight);

    Vector<Point> dock = new Vector<Point>();
    dock.add(nextDock);
    
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      wordLength = s.length();
      
      if (root){
    	  
    	  // Root
    	  g2.drawOval(10 ,0+yPos, ovalWidth ,ovalHeight);
    	  Constants.write(g2,s,yPos+(ovalWidth/2),ovalHeight/2+yPos+3,true);
    	nextDock = new Point(in+10, yPos+ovalHeight);
  
	yPos+=2*ovalHeight - (dist/2);
       	  
       	  dock.add(nextDock);
    	  level++;
    	  root = false;
    	  
      } else if (s.startsWith(">") && level > 0 && !root ) {
         	  
    	  level++;
    	  nextDock = dock.elementAt(level-1);
    	  nextDock = new Point(nextDock.x,nextDock.y);
    	  dock.add(nextDock);
    	  nextDock = dock.elementAt(level-1);
	  int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
    	  int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
    	  int kanten_zahl = 3;
    	  g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
    	  g2.drawLine (nextDock.x,nextDock.y+9,nextDock.x,yPos);
    	  g2.drawLine (nextDock.x,yPos,nextDock.x+(ovalWidth/2),yPos);
    	  g2.drawOval(nextDock.x+(ovalWidth/2) ,yPos-(ovalHeight/2), ovalWidth ,ovalHeight);

    	  if (s.length() >1) {
    		  Constants.write(g2,s.substring(1),nextDock.x+ovalWidth,yPos+3,true);
    	  }
    	  nextDock = new Point(nextDock.x+ovalWidth,yPos+(ovalHeight/2) );
    	  dock.set(level, nextDock);
    	  yPos+=2*ovalHeight - dist;

      }else if (s.startsWith("<") && level > 1){

    	 do {
                    level--;
                    // in-=(int)(packageWidth*1.5);
                    s = s.substring(1,s.length());
                    }  while(s.startsWith("<") && level > 1);

    	  nextDock = dock.elementAt(level-1);
    	  int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
    	  int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
    	  int kanten_zahl = 3;
          g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
          g2.drawLine (nextDock.x,nextDock.y+9,nextDock.x,yPos);
          g2.drawLine (nextDock.x,yPos,nextDock.x+(ovalWidth/2),yPos);
          g2.drawOval(nextDock.x+(ovalWidth/2) ,yPos-(ovalHeight/2), ovalWidth ,ovalHeight);
          Constants.write(g2,s,nextDock.x+ovalWidth,yPos+3,true);
          nextDock = new Point(nextDock.x+ovalWidth,yPos+(ovalHeight/2) );
          dock.set(level, nextDock);
          yPos+=2*ovalHeight - dist;
                
      }else {
    	  
    	  nextDock = dock.elementAt(level-1);
    	  int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
    	  int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
    	  int kanten_zahl = 3;
    	  g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
    	  g2.drawLine (nextDock.x,nextDock.y+9,nextDock.x,yPos);
    	  g2.drawLine (nextDock.x,yPos,nextDock.x+(ovalWidth/2),yPos);
    	  g2.drawOval(nextDock.x+(ovalWidth/2) ,yPos-(ovalHeight/2), ovalWidth ,ovalHeight);
    	  Constants.write(g2,s,nextDock.x+ovalWidth,yPos+3,true);
    	  nextDock = new Point(nextDock.x+ovalWidth,yPos+(ovalHeight/2) );
    	  dock.set(level, nextDock);
    	  yPos+=2*ovalHeight - dist;

      }
      
    }


    // Finally, change other graphical attributes using
    // drawLine, getWidth, getHeight..

    g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);


  }
	// Change this method if you want to set the resize-attributes of
	// your custom element
	public int getPossibleResizeDirections() {
		// Remove from this list the borders you don't want to be resizeable.
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT
				| Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}

	// Advanced: change this method to modify the area where relations
	// stick to your custom element.
	public StickingPolygon getStickingBorder() {
		// By default, the element returns its outer borders. Change it,
		// if your element needs to stick to relations differently.
		// See, for example, the source code of the UML interface element.
		StickingPolygon p = new StickingPolygon();

		/*
		 * p.addLine(new Point(0,0), new
		 * Point(this.getWidth()-1,0),Constants.RESIZE_TOP); p.addLine(new
		 * Point(this.getWidth()-1,0), new
		 * Point(this.getWidth()-1,this.getHeight()-1),Constants.RESIZE_RIGHT);
		 * p.addLine(new Point(this.getWidth()-1,this.getHeight()-1), new
		 * Point(0,this.getHeight()-1),Constants.RESIZE_BOTTOM); p.addLine(new
		 * Point(0,this.getHeight()-1), new Point(0,0),Constants.RESIZE_LEFT);
		 */

		return p;
	}

}
