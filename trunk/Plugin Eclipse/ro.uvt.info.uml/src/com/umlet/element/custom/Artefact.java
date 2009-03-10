package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import com.umlet.control.Constants;
import com.umlet.element.base.Entity;

public class Artefact extends Entity
{
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=10;
		int startY=Constants.getDistLineToText();

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
			yPos+=Constants.getFontsize();
			Constants.write(g2,s,Constants.getDistLineToText(),startY+ yPos, false);
			yPos+=Constants.getDistTextToText();
		}

		//symbol outline
		g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);

		//small component symbol
		g2.drawLine(this.getWidth()-30,10,this.getWidth()-30,40);
		g2.drawLine(this.getWidth()-30,40,this.getWidth()-5,40);
		g2.drawLine(this.getWidth()-5,40,this.getWidth()-5,20);
		g2.drawLine(this.getWidth()-5,20,this.getWidth()-15,10);
		g2.drawLine(this.getWidth()-15,10,this.getWidth()-30,10);
		
		g2.drawLine(this.getWidth()-5,20,this.getWidth()-15,20);
		g2.drawLine(this.getWidth()-15,20,this.getWidth()-15,10);
		
	}
}
