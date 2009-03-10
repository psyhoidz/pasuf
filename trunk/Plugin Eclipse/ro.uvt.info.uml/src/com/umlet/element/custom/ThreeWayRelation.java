package com.umlet.element.custom;

import java.awt.*;
import java.util.*;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class ThreeWayRelation extends com.umlet.element.base.Entity {

  public void paint(Graphics g) {

   Graphics2D g2=(Graphics2D) g; g2.setFont(Constants.getFont());
    g2.setColor(_activeColor); Constants.getFRC(g2);

    Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=Constants.getDistLineToText();
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      yPos+=Constants.getFontsize();
      Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
      yPos+=Constants.getDistTextToText();
    }


    Polygon p = new Polygon();
    p.addPoint((int)this.getWidth()/2,0);
    p.addPoint((int)this.getWidth(),(int)this.getHeight()/2);
    p.addPoint((int)this.getWidth()/2,(int)this.getHeight());
    p.addPoint(0,(int)this.getHeight()/2);
    g2.drawPolygon(p);
  }


  public int getPossibleResizeDirections() {
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }

  public StickingPolygon getStickingBorder() {
    StickingPolygon p = new StickingPolygon();
    p.addLine(new Point(this.getWidth()/2,0), new Point(this.getWidth(),this.getHeight()/2),Constants.RESIZE_TOP);
    p.addLine(new Point(this.getWidth(),this.getHeight()/2), new Point(this.getWidth()/2,this.getHeight()),Constants.RESIZE_RIGHT);
    p.addLine(new Point(this.getWidth()/2,this.getHeight()), new Point(0,this.getHeight()/2),Constants.RESIZE_BOTTOM);
    p.addLine(new Point(0,this.getHeight()/2), new Point(this.getWidth()/2,0),Constants.RESIZE_LEFT);
    return p;
  }
}
