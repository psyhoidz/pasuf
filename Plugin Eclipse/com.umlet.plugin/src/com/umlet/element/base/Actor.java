// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;

import com.umlet.constants.Constants;
import com.umlet.control.*;
import com.umlet.control.command.Resize;
import com.umlet.control.diagram.StickingPolygon;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Actor extends Entity {

  public Actor() {
    super();
  }

  private Vector<String> getStringVector() {
    Vector<String> ret=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    return ret;
  }

  public void paintEntity(Graphics g) {
    Graphics2D g2=(Graphics2D) g;
    g2.setFont(this.handler.getFont());
    Composite[] composites = colorize(g2);
    g2.setColor(_activeColor);
    this.handler.getFRC(g2);

    boolean ADAPT_SIZE=false;

    Vector<String> tmp=getStringVector();
    int yPos=6*this.handler.getFontsize();

    for (int i=0; i<tmp.size(); i++) {
      String s=tmp.elementAt(i);
      if (s.equals("--")) {
        yPos+=this.handler.getDistTextToLine();
        g2.drawLine(this.getWidth()/2-this.handler.getFontsize()*4,yPos,this.getWidth()/2+this.handler.getFontsize()*4,yPos);
        yPos+=this.handler.getDistLineToText();
      } else {
        yPos+=this.handler.getFontsize();
        TextLayout l=new TextLayout(s, this.handler.getFont(), this.handler.getFRC(g2));
        Rectangle2D r2d=l.getBounds();
        int width=(int)r2d.getWidth();
        int xPos=this.getWidth()/2-width/2;
        if (xPos<0) { ADAPT_SIZE=true; break; }
        this.handler.writeText(g2, s, this.getWidth()/2, yPos, true);
        yPos+=this.handler.getDistTextToText();
      }
    }

    if (ADAPT_SIZE) {
      (new Resize(this,8,-Umlet.getInstance().getMainUnit(),0)).execute(this.handler);
      (new Resize(this,2,Umlet.getInstance().getMainUnit(),0)).execute(this.handler);
      return;
    }
    if (yPos>this.getHeight()) {
      (new Resize(this,4,0,20)).execute(this.handler);
      return;
    }

    int startx=this.getWidth()/2;

    g2.setComposite(composites[1]);
    g2.setColor(_fillColor);
    g2.fillOval(startx-this.handler.getFontsize()/2, 0, this.handler.getFontsize(), this.handler.getFontsize());
    g2.setComposite(composites[0]);
    if(_selected) g2.setColor(_activeColor);
    else g2.setColor(_deselectedColor);
    
    g2.drawOval(startx-this.handler.getFontsize()/2, 0, this.handler.getFontsize(), this.handler.getFontsize());
    g2.drawLine(startx, this.handler.getFontsize(), startx, (int)(this.handler.getFontsize()*2.5));
    g2.drawLine(startx-2*this.handler.getFontsize(), (int)(this.handler.getFontsize()*1.3), startx+2*this.handler.getFontsize(), (int)(this.handler.getFontsize()*1.3));

    // Feet
    g2.drawLine(startx, (int)(this.handler.getFontsize()*2.5), startx-this.handler.getFontsize(),this.handler.getFontsize()*5);
    g2.drawLine(startx, (int)(this.handler.getFontsize()*2.5), startx+this.handler.getFontsize(),this.handler.getFontsize()*5);
  }
  
  public StickingPolygon getStickingBorder() { //LME
    int links=this.getWidth()/2-Umlet.getInstance().getMainUnit();
    int rechts=this.getWidth()/2+Umlet.getInstance().getMainUnit();
    int oben=0;
    int unten=4*Umlet.getInstance().getMainUnit();
    StickingPolygon p = new StickingPolygon();
    p.addLine(new Point(links,oben), new Point(rechts,oben));
    p.addLine(new Point(rechts,oben), new Point(rechts,unten));
    p.addLine(new Point(rechts,unten), new Point(links,unten));
    p.addLine(new Point(links,unten), new Point(links,oben));
    return p;
  }
  
  public int getPossibleResizeDirections() {return 0;} //deny size changes
  
}
