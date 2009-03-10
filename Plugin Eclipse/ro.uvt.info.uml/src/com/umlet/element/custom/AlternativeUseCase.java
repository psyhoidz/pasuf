package com.umlet.element.custom;
// Some import to have access to more Java features
import java.awt.*;
import java.util.*;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class AlternativeUseCase extends com.umlet.element.base.Entity {

  // Change this method if you want to edit the graphical 
  // representation of your custom element.
  public void paint(Graphics g) {

    // Some unimportant initialization stuff; setting color, font 
    // quality, etc. You should not have to change this.
    Graphics2D g2=(Graphics2D) g; g2.setFont(Constants.getFont());
    g2.setColor(_activeColor); Constants.getFRC(g2);

    boolean center = false;

    Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=Constants.getDistLineToText();
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      yPos+=Constants.getFontsize();
      if (s.equals("--")) {
    	  yPos=35;
          center = true;
      }
      else if (center == true) {
    	  Constants.write(g2,s,(getWidth()-1)/2, yPos, true);
    	  center = false;
      }
      else {
    	  Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
    	  yPos+=Constants.getDistTextToText();
      }
    }


    // Finally, change other graphical attributes using
    // drawLine, getWidth, getHeight..
    g2.drawRect(0,0,getWidth()-1,getHeight()-1);
    g2.drawLine(0,30,getWidth()-1,30);
    g2.drawOval(getWidth()-59,3,55,20);
  }
}
