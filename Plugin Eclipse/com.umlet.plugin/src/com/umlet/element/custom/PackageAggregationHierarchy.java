// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.custom;


import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class PackageAggregationHierarchy extends com.umlet.element.base.Entity {

	
  public void paintEntity(Graphics g) {
    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    Composite[] composites = colorize(g2); //enable colors
    g2.setColor(_activeColor); this.handler.getFRC(g2);

    Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    
    g2.setComposite(composites[1]);
    g2.setColor(_fillColor);
    g2.fillRect(0,0,this.getWidth()-1,this.getHeight()-1);
    g2.setComposite(composites[0]);
    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
    
    g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);
    
    boolean root = true;
	int level = 0;
    int yPos=10;
    int packageHeight = this.handler.getFontsize();
    int packageWidth = 2*this.handler.getFontsize();
    int in = (int)(packageWidth*1.5);
    Point nextDock = new Point(10+packageWidth/3 ,10+2*packageHeight);
    
    Vector<Point> dock = new Vector<Point>();
    dock.add(nextDock);
    
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      
      if (root){
    	  //Root
    	  g2.drawRect(10 ,0+yPos, packageWidth/3 ,packageHeight/4);
          g2.drawRect(10,packageHeight/4+yPos, packageWidth ,packageHeight);
          this.handler.writeText(g2,s,10+packageWidth+this.handler.getDistLineToText(),packageHeight+yPos,false);
          yPos+=2*packageHeight + this.handler.getDistTextToText();
          nextDock = new Point(in+10+packageWidth/3, 2*packageHeight+yPos);
          dock.add(nextDock);
    	  level++;
    	  root = false;
    	  
      }else if (s.startsWith(">") && level > 0 && !root && yPos > 4*packageHeight) {
          
    	  level++;
    	  in+=(int)(packageWidth*1.5);
    	  
          nextDock = new Point(in+packageWidth/3, 2*packageHeight+yPos);
          dock.add(nextDock);
          
    	  nextDock = dock.elementAt(level-1);
    	  g2.drawLine(nextDock.x, nextDock.y, nextDock.x, packageHeight/2+yPos);
    	  g2.drawLine(nextDock.x, packageHeight/2+yPos, in, packageHeight/2+yPos);
    	  // draw end of Line
    	  g2.drawOval(nextDock.x - 5, nextDock.y - 10, 10, 10);
    	  g2.drawLine(nextDock.x, nextDock.y - 8, nextDock.x, nextDock.y - 2);
    	  g2.drawLine(nextDock.x - 3, nextDock.y - 5, nextDock.x + 3, nextDock.y - 5);
    	  //draw Package
          g2.drawRect(in ,0+yPos, packageWidth/3 ,packageHeight/4);
          g2.drawRect(in ,packageHeight/4+yPos, packageWidth ,packageHeight);
          this.handler.writeText(g2,s.substring(1),in+packageWidth+this.handler.getDistTextToText(),packageHeight+yPos,false);

          nextDock = new Point(in+packageWidth/3, 2*packageHeight+yPos);
          dock.set(level, nextDock);
          
          yPos+=2*packageHeight + this.handler.getDistTextToText();
          
        
      }
      else if (s.startsWith("<") && level > 1){

    	 do {
    	   level--;
    	   in-=(int)(packageWidth*1.5);
    	   s = s.substring(1); 
    	  }
    	  while(s.startsWith("<") && level > 1);
    	  
    	  nextDock = dock.elementAt(level-1);
    	  g2.drawLine(nextDock.x, nextDock.y, nextDock.x, packageHeight/2+yPos);
    	  g2.drawLine(nextDock.x, packageHeight/2+yPos, in, packageHeight/2+yPos);
    	  g2.drawOval(nextDock.x - 5, nextDock.y - 10, 10, 10);
    	  g2.drawLine(nextDock.x, nextDock.y - 8, nextDock.x, nextDock.y - 2);
    	  g2.drawLine(nextDock.x - 3, nextDock.y - 5, nextDock.x + 3, nextDock.y - 5);
          g2.drawRect(in ,0+yPos, packageWidth/3 ,packageHeight/4);
          g2.drawRect(in ,packageHeight/4+yPos, packageWidth ,packageHeight);
          this.handler.writeText(g2,s,in+packageWidth+this.handler.getDistTextToText(),packageHeight+yPos,false);

          nextDock = new Point(in+packageWidth/3, 2*packageHeight+yPos);
          dock.set(level, nextDock);
          
          yPos+=2*packageHeight + this.handler.getDistTextToText();
          
      
      }else {
    	  
    	  nextDock = dock.elementAt(level-1);
    	  g2.drawLine(nextDock.x, nextDock.y, nextDock.x, packageHeight/2+yPos);
    	  g2.drawLine(nextDock.x, packageHeight/2+yPos, in, packageHeight/2+yPos);
    	  g2.drawOval(nextDock.x - 5, nextDock.y - 10, 10, 10);
    	  g2.drawLine(nextDock.x, nextDock.y - 8, nextDock.x, nextDock.y - 2);
    	  g2.drawLine(nextDock.x - 3, nextDock.y - 5, nextDock.x + 3, nextDock.y - 5);
          g2.drawRect(in ,0+yPos, packageWidth/3 ,packageHeight/4);
          g2.drawRect(in ,packageHeight/4+yPos, packageWidth ,packageHeight);
          this.handler.writeText(g2,s,in+packageWidth+this.handler.getDistTextToText(),packageHeight+yPos,false);
          
          nextDock = new Point(in+packageWidth/3, 2*packageHeight+yPos);
          dock.set(level, nextDock);
          
          yPos+=2*packageHeight + this.handler.getDistTextToText();
          
      }
    }
  }


  public int getPossibleResizeDirections() {
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }


  public StickingPolygon getStickingBorder() {
    StickingPolygon p = new StickingPolygon();
    
    return p;
  }
  
    
}


