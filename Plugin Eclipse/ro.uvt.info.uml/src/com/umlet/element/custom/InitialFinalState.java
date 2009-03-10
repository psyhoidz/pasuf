package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class InitialFinalState extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(Color.red);
		Constants.getFRC(g2);

		Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		yPos=this.getHeight()/2-tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText())/2;

		boolean initialState=false;
		for (int i=0; i<tmp.size(); i++) {
			String s=(String)tmp.elementAt(i);
			if(s.equals("i")) initialState=true;
		}

		

		if(!initialState) {
			g2.drawOval(0,0,this.getWidth()-1,this.getHeight()-1);

			g2.fillOval(4,4,this.getWidth()-8,this.getHeight()-8);
		} else {
			g2.fillOval(0,0,this.getWidth(),this.getHeight());
		}
	}
}
