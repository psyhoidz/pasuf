package com.umlet.element.custom; 

import java.awt.*;
import java.util.*;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

/** 
* <p>Title: </p> 
* <p>Description: </p> 
* <p>Copyright: Copyright (c) 2001</p> 
* <p>Company: </p> 
* @author unascribed 
* @version 1.0 
*/ 

public class EER_Entity extends Entity {
	int ySave = 0;
	boolean hasAttributes = false;
	
	private Vector getStringVector() {
	    Vector ret=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
	    return ret;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
	    g2.setFont(Constants.getFont());
	    g2.setColor(_activeColor);

	    //Constants.getFRC(g2);

	    Vector tmp=getStringVector();

	    int yPos=0;
	    yPos+=Constants.getDistLineToText()*2;

	    boolean CENTER=true;
	    boolean drawInnerRect = false;

	    for (int i=0; i<tmp.size(); i++) {
	      String s=(String)tmp.elementAt(i);
	      if (s.equals("--")) {
	        CENTER=false;
	        ySave = yPos+Constants.getDistLineToText()*2;
	        yPos+=Constants.getDistLineToText()*3;
	      } else {
	        yPos+=Constants.getFontsize();
	        if (CENTER) {
	        	String s1 = s;
	        	if(s.startsWith("##")) {
	    	      	drawInnerRect = true;
	    	      	s1 = s1.substring(2);
	        	}
	        	Constants.write(g2,s1,(int)this.getWidth()/2, yPos, true);
	        } else {
	        	Constants.write(g2,s,Constants.getFontsize(), yPos, false);
	        }
	        yPos+=Constants.getDistTextToText();
	        if(CENTER) ySave = yPos;
	      }
	    }
	    g.drawLine(0,0,this.getWidth()-1,0); //"----"
	    if(CENTER) {
	    	hasAttributes = false; //see getStickingBorder()
	    	ySave = this.getHeight();
	    	g.drawLine(0,this.getHeight()-1,this.getWidth()-1,this.getHeight()-1); //"____"
	    	if(drawInnerRect) {
				g.drawRect(3,3,this.getWidth()-7,this.getHeight()-7);
			}
	    	g.drawLine(0,0,0,this.getHeight()-1); //"|   "
			g.drawLine(this.getWidth()-1,0,this.getWidth()-1,this.getHeight()-1); //"   |"
	    } else {
	    	hasAttributes = true; //see getStickingBorder()
	    	g2.drawLine(0,ySave,this.getWidth()-1,ySave);
	    	g.drawLine(10,ySave,10,yPos+Constants.getDistTextToText()-Constants.getDistLineToText());
	    	if(drawInnerRect) {
				g.drawRect(3,3,this.getWidth()-7,ySave -6);
			}
	    	g.drawLine(0,0,0,ySave); //"|   "
			g.drawLine(this.getWidth()-1,0,this.getWidth()-1,ySave); //"   |"
	    }
	}
	
	public StickingPolygon getStickingBorder() { //LME: define the polygon on which relations stick on
		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(0,0), new Point(this.getWidth()-1,0),Constants.RESIZE_TOP);
		p.addLine(new Point(this.getWidth()-1,0), new Point(this.getWidth()-1,ySave-1),Constants.RESIZE_RIGHT);
		if(!hasAttributes) p.addLine(new Point(this.getWidth()-1,ySave-1), new Point(0,ySave-1),Constants.RESIZE_BOTTOM);
		p.addLine(new Point(0,ySave-1), new Point(0,0),Constants.RESIZE_LEFT);
		return p;
	}
} 
