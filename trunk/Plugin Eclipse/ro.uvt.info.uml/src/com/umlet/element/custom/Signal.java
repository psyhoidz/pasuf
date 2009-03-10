package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class Signal extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(Color.red);
		Constants.getFRC(g2);

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		yPos=this.getHeight()/2-(tmp.size()-1)*(Constants.getFontsize()+Constants.getDistTextToText())/2;


		int signalType=0;

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			if(s.equals(">")) signalType=1; //send signal
			else if(s.equals("<")) signalType=2; //accept signal
			else if(s.equals("x")) signalType=3; //time signal
			else { //draw string
				yPos+=Constants.getFontsize();
				Constants.write(g2,s,(int)this.getWidth()/2, yPos, true);
				yPos+=Constants.getDistTextToText();
			}
		}




		if(signalType==1) { //send signal
			g2.drawLine(0,0,this.getWidth()-Constants.getFontsize(),0);
			g2.drawLine(this.getWidth()-Constants.getFontsize(), this.getHeight()-1, 0, this.getHeight()-1);
			g2.drawLine(this.getWidth()-Constants.getFontsize(),0,this.getWidth()-1,this.getHeight()/2);
			g2.drawLine(this.getWidth(),this.getHeight()/2,this.getWidth()-Constants.getFontsize(),this.getHeight());
			g2.drawLine(0, this.getHeight()-1, 0, 0);
		} else if(signalType==2) { //accept signal
			g2.drawLine(0,0,this.getWidth(),0);
			g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
			g2.drawLine(0,0,Constants.getFontsize()-2,this.getHeight()/2);
			g2.drawLine(Constants.getFontsize()-2,this.getHeight()/2,0,this.getHeight());
			g2.drawLine(this.getWidth()-1, this.getHeight()-1, this.getWidth()-1, 0);
		} else if(signalType==3) { //time signal
			g2.drawLine(0,0,this.getWidth(),0);
			g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
			g2.drawLine(0,0,this.getWidth()-1, this.getHeight()-1);
			g2.drawLine(this.getWidth()-1,0,0, this.getHeight()-1);
		} else { //NO signal specified
			g2.drawLine(0,0,this.getWidth(),0);
			g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
			g2.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);
			g2.drawLine(0, this.getHeight()-1, 0, 0);
		}

	}
	
	public int doesCoordinateAppearToBeConnectedToMe(Point p) {
	    int ret=0;

	    int tmpX=p.x-this.getX();
	    int tmpY=p.y-this.getY();

	    if (tmpX>-4 && tmpX<this.getWidth()+4) {
	        if (tmpY>-4 && tmpY<4) ret+=1;
	        if (tmpY>this.getHeight()-4 && tmpY<this.getHeight()+4) ret+=4;
	      }
	    if (tmpY>-4 && tmpY<this.getHeight()+4) {
	        if (tmpX>-4 && tmpX<12) ret+=8;
	        if (tmpX>this.getWidth()-4 && tmpX<this.getWidth()+4) ret+=2;
	      }
	    return ret;
	  }
}
