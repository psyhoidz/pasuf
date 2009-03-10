// Some import to have access to more Java features
package com.umlet.element.custom;

import java.awt.*;
import java.util.*;
import com.umlet.control.*;

public class Taxonomy_of_Actors extends com.umlet.element.base.Entity {
    
    // Change this method if you want to edit the graphical
    // representation of your custom element.
    public void paint(Graphics g) {
        
        // Some unimportant initialization stuff; setting color, font
        // quality, etc. You should not have to change this.
        Graphics2D g2=(Graphics2D) g; g2.setFont(Constants.getFont());
        g2.setColor(_activeColor); Constants.getFRC(g2);
        
        
        // It's getting interesting here:
        // First, the strings you type in the element editor are read and
        // split into lines.
        // Then, by default, they are printed out on the element, aligned
        // to the left.
        // Change this to modify this default text printing and to react
        // to special strings
        // (like the "--" string in the UML class elements which draw a line).
        Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
        
        boolean first = true;
        boolean root = true;
        int enlarged = 0;
        int wordLength =0;
        int level = 0;
        int yPos=10;
        int head = 14;
        int abstand =10;
        int count = 2;
        String help="";
        Point nextDock = new Point(0 ,0);
        
        Vector<Point> dock = new Vector<Point>();
        // dock.add(nextDock);
        
        for (int i=0; i<tmp.size(); i++) {
            String s=(String)tmp.elementAt(i);
            wordLength = s.length();
            
            if (root){
                
                // Root
                g2.drawOval(10,0+yPos,head,head);
                g2.drawLine(10+head/2,head+yPos,10+head/2,head*3);
                g2.drawLine(6,yPos+head+head/3,14+head,yPos+head+head/3);
                g2.drawLine(10+head/2,head*3,10+head,(head*4));
                g2.drawLine(10+head/2,head*3,10,(head*4));
                Constants.write(g2,s,10,head*5,false);
                yPos+=8*head;
                nextDock = new Point(10+head/2, 5*head+4);
                dock.add(nextDock);
                count = count*2;
                level++;
                root = false;
                help = s;
                
            } else if (s.startsWith(">") && level > 0 && !root && !first) {
                
                level++;
                nextDock = dock.elementAt(level-1);
                nextDock = new Point(nextDock.x,nextDock.y);
                dock.add(nextDock);
                nextDock = dock.elementAt(level-1);
                int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
                int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
                int kanten_zahl = 3;
                
                g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
                
                g2.drawLine(nextDock.x,nextDock.y+9,nextDock.x,yPos-abstand);
                
                g2.drawLine(nextDock.x,yPos-abstand,nextDock.x+abstand*6,yPos-abstand);
                g2.drawOval(nextDock.x+abstand*6+Constants.getFontsize()/2,yPos-3*head,head,head);
                
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-2*head,nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand);
                g2.drawLine((nextDock.x+abstand*6+Constants.getFontsize()/2)-4,yPos-2*head+head/3,(nextDock.x+abstand*6+Constants.getFontsize()/2)+4+head,yPos-2*head+head/3);
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand,nextDock.x+abstand*6+Constants.getFontsize()/2+head,yPos+count);
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2,yPos+count,nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand);
                if (s.length() > 1) {
                    Constants.write(g2,s.substring(1),nextDock.x+abstand*6+Constants.getFontsize()/2,yPos+head+count,false);
                }
                nextDock = new Point(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos+head+count*2);
                dock.add(level,nextDock);
                yPos+=5*head;
                if (i<tmp.size()-1 && ((String)tmp.elementAt(i+1)).startsWith(">")){
                    help = help+s;
                }
                
                
                
            }else if (s.startsWith("<") && level > 1){
                
                
                do {
                    level--;
                    
                    s = s.substring(1,s.length());
                    
                }  while(s.startsWith("<") && level > 1);
                
                nextDock = dock.elementAt(level-1);
                int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
                int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
                int kanten_zahl = 3;
                g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
                
                g2.drawLine(nextDock.x,nextDock.y+9,nextDock.x,yPos-abstand);
                
                g2.drawLine(nextDock.x,yPos-abstand,nextDock.x+abstand*6,yPos-abstand);
                g2.drawOval(nextDock.x+abstand*6+Constants.getFontsize()/2,yPos-3*head,head,head);
                                
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-2*head,nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand);
                g2.drawLine((nextDock.x+abstand*6+Constants.getFontsize()/2)-4,yPos-2*head+head/3,(nextDock.x+abstand*6+Constants.getFontsize()/2)+4+head,yPos-2*head+head/3);
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand,nextDock.x+abstand*6+Constants.getFontsize()/2+head,yPos+count);
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2,yPos+count,nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand);
                if (s.length() > 0) {
                    Constants.write(g2,s,nextDock.x+abstand*6+Constants.getFontsize()/2,yPos+head+count,false);
                    
                }
                nextDock = new Point(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos+head+count*2);
                dock.add(level,nextDock);
                yPos+=5*head;
                
                
                
            }else {
                
                first = false;
                nextDock = dock.elementAt(level-1);
                int[] xkanten = {nextDock.x,nextDock.x+6,nextDock.x-6};
                int[] ykanten = {nextDock.y,nextDock.y+9,nextDock.y+9};
                int kanten_zahl = 3;
                g2.drawPolygon(new Polygon(xkanten,ykanten,kanten_zahl));
                
                g2.drawLine(nextDock.x,nextDock.y+9,nextDock.x,yPos-abstand);
                
                g2.drawLine(nextDock.x,yPos-abstand,nextDock.x+abstand*6,yPos-abstand);
                
                g2.drawOval(nextDock.x+abstand*6+Constants.getFontsize()/2,yPos-3*head,head,head);
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-2*head,nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand);
                g2.drawLine((nextDock.x+abstand*6+Constants.getFontsize()/2)-4,yPos-2*head+head/3,(nextDock.x+abstand*6+Constants.getFontsize()/2)+4+head,yPos-2*head+head/3);
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand,nextDock.x+abstand*6+Constants.getFontsize()/2+head,yPos+count);
                g2.drawLine(nextDock.x+abstand*6+Constants.getFontsize()/2,yPos+count,nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos-abstand);
                
                Constants.write(g2,s,nextDock.x+abstand*6+Constants.getFontsize()/2,yPos+count+head,false);
                
                nextDock = new Point(nextDock.x+abstand*6+Constants.getFontsize()/2+head/2,yPos+head+count*2);
                dock.add(level,nextDock);
                yPos+=5*head;
                
                
                //count = count*2;
                if (i<tmp.size()-1 && ((String)tmp.elementAt(i+1)).startsWith("::")){
                    help = help+s;
                }
                
                
                
                
            }
            
        }
        
        
        // Finally, change other graphical attributes using
        // drawLine, getWidth, getHeight..
        
        g2.drawRect(0,0,this.getWidth()-1,this.getHeight()-1);
        
        
    }
    // Change this method if you want to set the resize-attributes of
    // your custom element
    public int getPossibleResizeDirections() {
        // Remove from this list the borders you don't want to be resizeable.
        return Constants.RESIZE_TOP | Constants.RESIZE_LEFT
                | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
    }
    
    // Advanced: change this method to modify the area where relations
    // stick to your custom element.
    public StickingPolygon getStickingBorder() {
        // By default, the element returns its outer borders. Change it,
        // if your element needs to stick to relations differently.
        // See, for example, the source code of the UML interface element.
        StickingPolygon p = new StickingPolygon();
        
                /*
                 * p.addLine(new Point(0,0), new
                 * Point(this.getWidth()-1,0),Constants.RESIZE_TOP); p.addLine(new
                 * Point(this.getWidth()-1,0), new
                 * Point(this.getWidth()-1,this.getHeight()-1),Constants.RESIZE_RIGHT);
                 * p.addLine(new Point(this.getWidth()-1,this.getHeight()-1), new
                 * Point(0,this.getHeight()-1),Constants.RESIZE_BOTTOM); p.addLine(new
                 * Point(0,this.getHeight()-1), new Point(0,0),Constants.RESIZE_LEFT);
                 */
        
        return p;
    }
    
}