package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class Component extends Entity {
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);
		// G.M Beginn
		boolean normal = false;
		// G.M. End

		Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos=0;
		int startY=this.getHeight()/2-tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText())/2;

		for (int i=0; i<tmp.size(); i++) {
			String s=tmp.elementAt(i);
//			G.M Beginn
			if (s.startsWith("'") || normal==true) {
				startY=Constants.getFontsize();
				s=s.substring(1,s.length());
				yPos+=Constants.getFontsize();
				Constants.write(g2,s,10,startY+ yPos, false);
				yPos+=Constants.getDistTextToText();
				normal = true;
			}
			//G.M.End
			else {
				if(s.startsWith("*")) {
					startY=Constants.getFontsize();
					s=s.substring(1,s.length());
				}
				yPos+=Constants.getFontsize();
				Constants.write(g2,s,(int)this.getWidth()/2,startY+ yPos, true);
				yPos+=Constants.getDistTextToText();
			}
		}

		//symbol outline
		g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);

		//small component symbol
		g2.drawLine(this.getWidth()-50,10,this.getWidth()-15,10);
		g2.drawLine(this.getWidth()-50,35,this.getWidth()-15,35);
		g2.drawLine(this.getWidth()-15,10,this.getWidth()-15,35);
		g2.drawLine(this.getWidth()-50,10,this.getWidth()-50,15);
		g2.drawLine(this.getWidth()-55,15,this.getWidth()-45,15);
		g2.drawLine(this.getWidth()-55,20,this.getWidth()-45,20);
		g2.drawLine(this.getWidth()-55,15,this.getWidth()-55,20);
		g2.drawLine(this.getWidth()-45,15,this.getWidth()-45,20);
		g2.drawLine(this.getWidth()-50,20,this.getWidth()-50,25);
		g2.drawLine(this.getWidth()-55,25,this.getWidth()-45,25);
		g2.drawLine(this.getWidth()-55,30,this.getWidth()-45,30);
		g2.drawLine(this.getWidth()-55,25,this.getWidth()-55,30);
		g2.drawLine(this.getWidth()-45,25,this.getWidth()-45,30);
		g2.drawLine(this.getWidth()-50,30,this.getWidth()-50,35);
	}

}
