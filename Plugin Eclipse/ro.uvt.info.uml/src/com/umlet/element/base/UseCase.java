package com.umlet.element.base;

import java.awt.*;
import java.util.*;

import com.umlet.control.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
// G. Wirrer start
public class UseCase extends Entity {
  public Entity CloneFromMe() {
    UseCase c=new UseCase();
    c.setState(this.getPanelAttributes());
    //c.setVisible(true);
    c.setBounds(this.getBounds());
    return c;
  }
  public UseCase(String s) {
    super(s);
  }
  public UseCase() {
    super();
  }

  private Vector<String> getStringVector() {
    Vector<String> ret=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    return ret;
  }

  public void paint(Graphics g) {
    int a = (getWidth()-1)/2;
	int b = (getHeight()-1)/2;
	boolean found = false;        
	int x = ((getWidth()-1)/9*4);
	int y = (int)Math.round((Math.sqrt(((a*a*b*b)-(b*b*x*x))/(a*a))));
	int yPos=0;
	int yPos1=b;     
	Graphics2D g2=(Graphics2D) g;
    g2.setFont(Constants.getFont());
    g2.setColor(_activeColor);

    Constants.getFRC(g2);

    Vector<String> tmp=new Vector<String>(getStringVector());
    if (tmp.contains("lt=.")) {
      tmp.remove("lt=.");
      g2.setStroke(Constants.getStroke(1,1));
    }
    if (tmp.contains("--")) {
    	yPos=((b-y)/2);
    	g2.drawLine(a-x,b-y,a+x,b-y);
    	found = true;
    }else {
    	yPos=this.getHeight()/2-tmp.size()*(Constants.getFontsize()+Constants.getDistTextToText())/2;	
    }
    
    for (int i=0; i<tmp.size(); i++) {
      String s=tmp.elementAt(i);
      if ((s.equals("--")) && found) {
    	  yPos=yPos1;
      }
      else if (found) {
    	  Constants.write(g2,s,a, yPos+5, true);
    	  yPos+=5*Constants.getDistTextToText();
    	
      } else {
        yPos+=Constants.getFontsize();
        Constants.write(g2, s, (int)this.getWidth()/2, yPos, true);
        yPos+=Constants.getDistTextToText();
      }
    }
    g2.drawOval(0,0,2*a,2*b);

  }
}
// G. Wirrer end 
