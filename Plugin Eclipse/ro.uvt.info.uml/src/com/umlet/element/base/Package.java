package com.umlet.element.base;

import java.awt.*;
import java.awt.geom.Area;
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

public class Package extends Entity {

  Area lastKnown = new Area();
    
  public Entity CloneFromMe() {
    Package c=new Package();
    c.setState(this.getPanelAttributes());
    //c.setVisible(true);
    c.setBounds(this.getBounds());
    return c;
  }
  public Package(String s) {
    super(s);
    //A.Mueller begin
    this.setTransparentSelection(true);
    //A.Mueller end
  }
  public Package() {
    super();
    //A.Mueller begin
    this.setTransparentSelection(true);
    //A.Mueller end
  }

  private Vector<String> getStringVector() {
    Vector<String> ret=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    return ret;
  }

  public void paint(Graphics g) {
    Graphics2D g2=(Graphics2D) g;
    g2.setFont(Constants.getFont());
    g2.setColor(_activeColor);
    Constants.getFRC(g2);

    Vector<String> tmp=new Vector<String>(getStringVector()); // in order to make the addition of "--" possible
    if (tmp.size()==0) tmp.add(" ");
    if (!tmp.contains("--")) tmp.add("--");

    int yPos=Constants.getDistLineToText();
    boolean borders=false;
    // G. Mueller start
    boolean normal=false;
    // int maxUpperBox=5*Constants.getFontsize();
    int maxUpperBox=(int)(this.getWidth()*0.4); // I think this looks better
    int lines = 0;
    
    
    for (int i=0; i<tmp.size(); i++) {
      String s=tmp.elementAt(i);
      
      if (tmp.elementAt(0).equals("--") && borders==false) yPos = Constants.getDistLineToText()+Constants.getDistTextToText()+Constants.getFontsize(); // if there is no Packagename
      
      if (s.equals("--") && borders==false) {
        borders=true;
        
        g2.drawRect(0,0,maxUpperBox,yPos);
        g2.drawRect(0,yPos,this.getWidth()-1,this.getHeight()-yPos-1);
        
        // yPos to write the String centered
        yPos=Constants.getFontsize()/2+yPos/2+this.getHeight()/2-(tmp.size()-lines)*(Constants.getFontsize()+Constants.getDistTextToText())/2;

       
          
      } else if (!normal && i>1 && (tmp.elementAt(i-1).equals("--")&& tmp.elementAt(i).startsWith("left:"))) {
          // writes the string normal
          yPos=(lines+1)*(Constants.getFontsize()+Constants.getDistTextToText())/2;
          yPos+=Constants.getDistTextToText();
          yPos+=Constants.getFontsize();
          Constants.write(g2,s.substring(5),Constants.getFontsize()/2, yPos, false);
          yPos+=Constants.getDistTextToText();
          normal = true;
          
      
      } else if (!borders){
          
          lines++;
          maxUpperBox=Math.max(maxUpperBox,Constants.getPixelWidth(g2,s)+Constants.getFontsize());
          yPos+=Constants.getFontsize();
          Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
          yPos+=Constants.getDistTextToText();
          
      } else if (normal) {
          
          yPos+=Constants.getFontsize();
          Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
          yPos+=Constants.getDistTextToText();
      
      } else if(!normal){
           
          yPos+=Constants.getFontsize();
          Constants.write(g2,s,(int)this.getWidth()/2, yPos, true);
          yPos+=Constants.getDistTextToText();
               
      }
      // G. Mueller End
    }

    /*Rectangle r=this.getBounds();
    g.drawRect(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);
    if (_selected) {
      g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3);
    }*/
  }
  public StickingPolygon getStickingBorder() { //LME
    StickingPolygon p = new StickingPolygon();
    Vector<String> tmp=new Vector<String>(getStringVector());
    if (tmp.size()==0) tmp.add(" ");
    int yPos=Constants.getDistLineToText();
    boolean borders=false;
    //int maxUpperBox=5*Constants.getFontsize();
    int maxUpperBox=(int)(this.getWidth()*0.4);
    for (int i=0; i<tmp.size(); i++) {
        String s=tmp.elementAt(i);
        // G. Mueller start
        if (tmp.elementAt(0).equals("--") && borders==false) yPos = Constants.getDistLineToText()+Constants.getDistTextToText()+Constants.getFontsize(); // if there is no Packagename
        // G.Mueller End
        if (s.equals("--") && borders==false) borders=true;
        else if(!borders) {
            maxUpperBox=Math.max(maxUpperBox,Constants.getPixelWidth((Graphics2D)this.getGraphics(),s)+Constants.getFontsize());
            yPos+=Constants.getFontsize()+Constants.getDistTextToText();
        }
    }
    p.addLine(new Point(0,0), new Point(maxUpperBox,0),Constants.RESIZE_TOP);
    p.addLine(new Point(maxUpperBox,yPos), new Point(getWidth()-1,yPos),Constants.RESIZE_TOP);
    p.addLine(new Point(maxUpperBox,0), new Point(maxUpperBox,yPos),Constants.RESIZE_TOP | Constants.RESIZE_LEFT);
    p.addLine(new Point(getWidth()-1,yPos), new Point(getWidth()-1,getHeight()-1),Constants.RESIZE_RIGHT);
    p.addLine(new Point(getWidth()-1,getHeight()-1), new Point(0,getHeight()-1),Constants.RESIZE_BOTTOM);
    p.addLine(new Point(0,getHeight()-1), new Point(0,0),Constants.RESIZE_LEFT);
    return p;
  }
}