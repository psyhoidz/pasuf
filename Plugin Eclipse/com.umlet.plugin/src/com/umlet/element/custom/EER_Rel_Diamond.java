package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

@SuppressWarnings("serial")
public class EER_Rel_Diamond extends com.umlet.element.base.Entity {

  // Change this method if you want to edit the graphical 
  // representation of your custom element.
  public void paintEntity(Graphics g) {

    // Some unimportant initialization stuff; setting color, font 
    // quality, etc. You should not have to change this.
    Graphics2D g2=(Graphics2D) g; g2.setFont(this.handler.getFont());
    Composite[] composites = colorize(g2); //enable colors
    g2.setColor(_activeColor); this.handler.getFRC(g2);


    // It's getting interesting here:
    // First, the strings you type in the element editor are read and
    // split into lines. Then, by default, they are printed out on the
    // element, aligned to the left. Change this to modify this default 
    // text printing and to react to special strings (like the "--" string 
    // in the UML class elements which draw a line).
    Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=this.getHeight()/2-((this.handler.getDistLineToText() + this.handler.getFontsize()) * tmp.size())/2;
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      yPos+=this.handler.getFontsize();
      this.handler.writeText(g2,s,this.getWidth()/2, yPos, true);
      yPos+=this.handler.getDistTextToText();
    }


    // Finally, change other graphical attributes using
    // drawLine, getWidth, getHeight..

    // Define the elements outline using a polygon, rectangle, oval, etc.
    Polygon poly = new Polygon();
    poly.addPoint(this.getWidth()/2,0);
    poly.addPoint(this.getWidth()-1,this.getHeight()/2);
    poly.addPoint(this.getWidth()/2, this.getHeight()-1);
    poly.addPoint(0, this.getHeight()/2);


    g2.setComposite(composites[1]); //set aplha composite for drawing the background color
    g2.setColor(_fillColor);
    g2.fillPolygon(poly); //fill the background
    g2.setComposite(composites[0]); //reset composite settings
    if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
    
    // Draw the elements outline
    g2.drawPolygon(poly); 
  }


  // Change this method if you want to set the resize-attributes of
  // your custom element
  public int getPossibleResizeDirections() {
    // Remove from this list the borders you don't want to be resizeable.
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }


  // Advanced: change this method to modify the area where relations
  // stick to your custom element.
  public StickingPolygon getStickingBorder() {
    // By default, the element returns its outer borders. Change it,
    // if your element needs to stick to relations differently.
    // See, for example, the source code of the UML interface element.
    StickingPolygon p = new StickingPolygon();
    p.addLine(new Point(0,0), new Point(this.getWidth()-1,0));
    p.addLine(new Point(this.getWidth()-1,0), new Point(this.getWidth()-1,this.getHeight()-1));
    p.addLine(new Point(this.getWidth()-1,this.getHeight()-1), new Point(0,this.getHeight()-1));
    p.addLine(new Point(0,this.getHeight()-1), new Point(0,0));
    return p;
  }
}
