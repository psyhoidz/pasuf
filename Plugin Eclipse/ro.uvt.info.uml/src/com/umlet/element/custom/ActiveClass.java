package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class ActiveClass extends Entity {	
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		yPos=this.getHeight()/2-tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText())/2;


		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			yPos+=Constants.getFontsize();
			Constants.write(g2,s,(int)this.getWidth()/2, yPos, true);
			yPos+=Constants.getDistTextToText();
		}


		g2.drawLine(0,0,this.getWidth(),0);
		g2.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);
		g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
		g2.drawLine(0, this.getHeight()-1, 0, 0);
		
		g2.drawLine(Constants.getFontsize()/2,0,Constants.getFontsize()/2,this.getHeight()-1);
		g2.drawLine(this.getWidth()-Constants.getFontsize()/2,0,this.getWidth()-Constants.getFontsize()/2,this.getHeight()-1);
	}
}
