package com.umlet.element.custom;

import java.awt.*;
import java.util.*;

import com.umlet.control.*;
import com.umlet.element.base.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class TemplateClass extends Entity {
  public Entity CloneFromMe() {
    TemplateClass c=new TemplateClass();
    c.setState(this.getPanelAttributes());
    //c.setVisible(true);
    c.setBounds(this.getBounds());
    return c;
  }
  public TemplateClass(String s) {
    super(s);
  }
  public TemplateClass() {
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

    //Constants.getFRC(g2);

    Vector tmp=getStringVector();

    int yPos=0;
    yPos+=Constants.getDistLineToText();

    boolean CENTER=true;
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      if (s.equals("--")) {
        CENTER=false;
        g2.drawLine(0,yPos,this.getWidth(),yPos);
        yPos+=Constants.getDistLineToText();
      } else {
        yPos+=Constants.getFontsize();
        if (CENTER) {
          Constants.write(g2,s,(int)this.getWidth()/2, yPos, true);
        } else {
          Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
        }
        yPos+=Constants.getDistTextToText();
      }
    }

    Rectangle r=this.getBounds();
    g.drawRect(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
    /*if (_selected) {
      g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
    }*/
  }


}