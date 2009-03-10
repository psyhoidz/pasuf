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

public class Note extends Entity {
  public Entity CloneFromMe() {
    Note c=new Note();
    c.setState(this.getPanelAttributes());
    //c.setVisible(true);
    c.setBounds(this.getBounds());
    return c;
  }
  public Note(String s) {
    super(s);
  }
  public Note() {
    super();
  }

  private Vector getStringVector() {
    Vector ret=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    return ret;
  }

  public void paint(Graphics g) {
    Graphics2D g2=(Graphics2D) g;
    g2.setFont(Constants.getFont());
    g2.setColor(_activeColor);
    Constants.getFRC(g2);


    int yPos=0;
    yPos+=Constants.getDistLineToText();

    Vector tmp=this.getStringVector();

    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      yPos+=Constants.getFontsize();
      Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
      yPos+=Constants.getDistTextToText();
    }

    g2.drawLine(0,0,this.getWidth()-Constants.getFontsize(),0);
    g2.drawLine(this.getWidth()-Constants.getFontsize(),0, this.getWidth()-1, Constants.getFontsize());
    g2.drawLine(this.getWidth()-1, Constants.getFontsize(), this.getWidth()-1, this.getHeight()-1);
    g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
    g2.drawLine(0, this.getHeight()-1, 0, 0);
    g2.drawLine(this.getWidth()-Constants.getFontsize(),0, this.getWidth()-Constants.getFontsize(), Constants.getFontsize());
    g2.drawLine(this.getWidth()-Constants.getFontsize(),Constants.getFontsize(), this.getWidth()-1, Constants.getFontsize());

    /*Rectangle r=this.getBounds();
    g.drawRect(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
    if (_selected) {
      g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
    }*/
  }
}