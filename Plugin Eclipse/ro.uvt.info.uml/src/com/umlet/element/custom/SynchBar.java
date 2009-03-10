package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class SynchBar extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(Color.red);
		Constants.getFRC(g2);

		int yPos=0;
		yPos+=Constants.getDistLineToText();

		Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");

		for (int i=0; i<tmp.size(); i++) {
			String s=(String)tmp.elementAt(i);
			yPos+=Constants.getFontsize();
			Constants.write(g2,s,0,yPos, true);
			yPos+=Constants.getDistTextToText();
		}

		//g2.fillRect(0,7,this.getWidth(),this.getHeight()-15);
		g2.fillRect(0,7,this.getWidth(),5);
	}
	
	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	    int ret=0;
	    int tmpX=p.x-this.getX();
	    int tmpY=p.y-this.getY();
	    
	    if (tmpX>-4 && tmpX<this.getWidth()+4) {
	        if (tmpY>0 && tmpY<8) ret+=1; 
	        if (tmpY>this.getHeight()-16 && tmpY<this.getHeight()+0) ret+=4;
	    }
	    //if (tmpY>-4 && tmpY<this.getHeight()+4) {
	    //    if (tmpX>0 && tmpX<8) ret+=8;
	    //    if (tmpX>this.getWidth()-4 && tmpX<this.getWidth()+4) ret+=2;
	    //  }
	    return ret;
	  }
}
