package com.umlet.element.base;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
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

public class Interface extends Entity {
  public Entity CloneFromMe() {
    Interface ret=new Interface();
    ret.setState(this.getPanelAttributes());

    ret.setBounds(this.getBounds());
    
    int i=ret.getWidth()+Umlet.getInstance().getMainUnit()-1;
    i=i/Umlet.getInstance().getMainUnit();
    ret.setBounds(new Rectangle(i, ret.getHeight()));
    
    return ret;
  }

  public Interface(String s) {
    super(s);
  }
  public Interface() {
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

    /*FontRenderContext rendering;
    if (Constants.getFontsize()>12) {
         rendering=new FontRenderContext(null, true, true);
         g2.setRenderingHints(Constants.UxRenderingQualityHigh());
    }
    else {
         rendering=new FontRenderContext(null, false, false);
         g2.setRenderingHints(Constants.UxRenderingQualityLow());
    }*/

    boolean ADAPT_SIZE=false;

    Vector tmp=getStringVector();
    int yPos=0;
    yPos+=2*Umlet.getInstance().getMainUnit();
    yPos+=Constants.getDistLineToText();



    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      if (s.equals("--")) {
        yPos+=Constants.getDistTextToLine();
        g2.drawLine(this.getWidth()/2-Constants.getFontsize()*4,yPos,this.getWidth()/2+Constants.getFontsize()*4,yPos);
        yPos+=Constants.getDistLineToText();
      } else {
        yPos+=Constants.getFontsize();
        TextLayout l=new TextLayout(s, Constants.getFont(), Constants.getFRC(g2));
        Rectangle2D r2d=l.getBounds();
        int width=(int)r2d.getWidth();
        int xPos=this.getWidth()/2-width/2;
        if (xPos<0) { ADAPT_SIZE=true; break; }
        Constants.write(g2, s, this.getWidth()/2, yPos, true);
        yPos+=Constants.getDistTextToText();
      }
    }

    if (ADAPT_SIZE) {
      Controller.getInstance().executeCommandWithoutUndo(new Resize(this,8,-Umlet.getInstance().getMainUnit(),0));
      Controller.getInstance().executeCommandWithoutUndo(new Resize(this,2,Umlet.getInstance().getMainUnit(),0));
      return;
    }
    if (yPos>this.getHeight()) {
      Controller.getInstance().executeCommandWithoutUndo(new Resize(this,4,0,20));
      return;
    }

    g.drawOval(this.getWidth()/2-Umlet.getInstance().getMainUnit(), 0, 2*Umlet.getInstance().getMainUnit(), 2*Umlet.getInstance().getMainUnit());
    /*if (_selected) {
      g.drawOval(this.getWidth()/2-Constants.getFontsize()+1, 1, 2*Constants.getFontsize()-2, 2*Constants.getFontsize()-2);
    }*/
  }
  
  
/*  public int doesCoordinateAppearToBeConnectedToMe(Point p) {
    int tmpX=p.x-this.getX();
    int tmpY=p.y-this.getY();

    int links=this.getWidth()/2-Umlet.getInstance().getMainUnit();
    int rechts=this.getWidth()/2+Umlet.getInstance().getMainUnit();

    int oben=0;
    int unten=2*Umlet.getInstance().getMainUnit();

    if (tmpX>links-4 && tmpX<rechts+4) {
        if ((tmpY>oben-4 && tmpY<oben+4) || (tmpY>unten-4 && tmpY<unten+4)) return 15;
      }
    if (tmpY>oben-4 && tmpY<unten+4) {
        if ((tmpX>links-4 && tmpX<links+4) || (tmpX>rechts-4 && tmpX<rechts+4)) return 15;
      }

    return 0;
  }*/
  
  public StickingPolygon getStickingBorder() { //LME
    int links=this.getWidth()/2-Umlet.getInstance().getMainUnit();
    int rechts=this.getWidth()/2+Umlet.getInstance().getMainUnit();
    int oben=0;
    int unten=2*Umlet.getInstance().getMainUnit();
    StickingPolygon p = new StickingPolygon();
    p.addLine(new Point(links,oben), new Point(rechts,oben),Constants.RESIZE_TOP);
    p.addLine(new Point(rechts,oben), new Point(rechts,unten),Constants.RESIZE_TOP);
    p.addLine(new Point(rechts,unten), new Point(links,unten),Constants.RESIZE_TOP);
    p.addLine(new Point(links,unten), new Point(links,oben),Constants.RESIZE_TOP);
    return p;
  }
  
  public int getPossibleResizeDirections() {return 0;} //deny size changes
}