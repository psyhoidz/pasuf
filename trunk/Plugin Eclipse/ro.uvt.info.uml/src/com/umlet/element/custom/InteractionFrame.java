package com.umlet.element.custom;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class InteractionFrame extends Entity {
    public void paint(Graphics g) {
        Graphics2D g2=(Graphics2D) g;
        g2.setFont(Constants.getFont());
        g2.setColor(_activeColor);
        Constants.getFRC(g2); 

        int yPos=0;
        yPos+=Constants.getDistLineToText();

        Vector<String> tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");

        int textWidth=0;
//      A.Mueller start
        boolean center = false;
        int topHeight=tmp.size();
        int maxWidthTemp = 0;
        
        for (int i=0; i<tmp.size(); i++) {
            String s = tmp.elementAt(i);
            maxWidthTemp = Math.max(Constants.getPixelWidth(g2,s),maxWidthTemp);
            if (s.equals("--"))
            {
                textWidth=Constants.getDistLineToText()+maxWidthTemp+Constants.getDistTextToLine();
                topHeight=i;
                yPos += (Constants.getDistTextToLine() + Constants.getDistLineToText());
                center = true;
            } else if (s.equals("-."))
            {
                yPos += Constants.getDistTextToLine();
                g2.setStroke(Constants.getStroke(1, 1));
                g2.drawLine(0, yPos, this.getWidth(), yPos);
                g2.setStroke(Constants.getStroke(0, 1));
                yPos += Constants.getDistLineToText();
            } else
            {
                yPos += Constants.getFontsize();
                if (center)
                    Constants.write(g2, s, this.getWidth() / 2, yPos, true);
                else
                    Constants.write(g2, s, Constants.getFontsize() / 2, yPos,
                            false);
                yPos += Constants.getDistTextToText();
            }
        }
        if (textWidth == 0) textWidth=maxWidthTemp;
        /*<OLDCODE>
         for (int i=0; i<tmp.size(); i++) {
            String s=tmp.elementAt(i);
            yPos+=Constants.getFontsize();
            Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
            yPos+=Constants.getDistTextToText();
            TextLayout l=new TextLayout(s, Constants.getFont(), Constants.getFRC(g2));
            Rectangle2D r2d=l.getBounds();
            textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth);
        }
         </OLDCODE>*/
//A.Mueller end
        
        
        g2.drawLine(0,0,this.getWidth(),0);
        g2.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight()-1);
        g2.drawLine(this.getWidth()-1, this.getHeight()-1, 0, this.getHeight()-1);
        g2.drawLine(0, this.getHeight()-1, 0, 0);
        
        int w=((Constants.getFontsize()*7)>textWidth)?(Constants.getFontsize()*7):(textWidth);
        //A.Mueller start
        int h=topHeight*(Constants.getFontsize()+3)+Constants.getFontsize();
        int sw=w-(topHeight-1)*Constants.getFontsize();
        //<OLDCODE>
        //int h=tmp.size()*(Constants.getFontsize()+3)+Constants.getFontsize();
        //int sw=w-(tmp.size()-1)*Constants.getFontsize();
        //</OLDCODE>
        //A.Mueller end
        
        
        g2.drawLine(0,h,sw,h);
        g2.drawLine(w+Constants.getFontsize(),0,w+Constants.getFontsize(),Constants.getFontsize());
        g2.drawLine(sw,h,w+Constants.getFontsize(),Constants.getFontsize());
    }
}