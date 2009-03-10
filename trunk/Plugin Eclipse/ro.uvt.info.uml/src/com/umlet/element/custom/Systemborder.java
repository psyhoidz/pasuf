package com.umlet.element.custom;

import java.awt.*;
import java.util.*;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class Systemborder extends com.umlet.element.base.Entity {

  public void paint(Graphics g) {

    Graphics2D g2=(Graphics2D) g; g2.setFont(Constants.getFont());
    g2.setColor(_activeColor); Constants.getFRC(g2);

    Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    int yPos=Constants.getDistLineToText();
    boolean center = false;
    boolean downleft = false;
    boolean upcenter = false;
    for (int i=0; i<tmp.size(); i++) {
      String s=(String)tmp.elementAt(i);
      if (s.startsWith("center:")&& !s.equals("center:")) {
          if (tmp.size() == 1) {
              yPos=((getHeight()-1)/2)-10;
          }
          else {
              yPos=((getHeight()-1)/2)-10-Constants.getFontsize()*(tmp.size()/2);
          }
          center = true;
          s = s.replace("center:","");
      }
      else if (s.startsWith("bottomleft:")&& !s.equals("bottomleft:")) {
          downleft = true;
          s = s.replace("bottomleft:","");
          yPos=(getHeight()-1)-Constants.getFontsize();
      }
      else if (s.startsWith("topcenter:")&& !s.equals("topcenter:")) {
          upcenter = true;
          s = s.replace("topcenter:","");
          //yPos+=Constants.getFontsize();
      } 
      if (center) {
          yPos+=Constants.getFontsize();
          Constants.write(g2,s,(getWidth()-1)/2, yPos, true);
          yPos+=2*Constants.getDistTextToText(); 
      }
      else if (downleft) {
          Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
      }
      else if (upcenter) {
          yPos+=Constants.getFontsize();
          Constants.write(g2,s,(getWidth()-1)/2, yPos, true);
          yPos+=Constants.getDistTextToText();
      }
      else {
          yPos+=Constants.getFontsize();
          Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
          yPos+=Constants.getDistTextToText();
      }
    }


   g2.drawRect(0,0,getWidth()-1,getHeight()-1);
  }
} 
