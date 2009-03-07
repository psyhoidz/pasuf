// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;

public class GenJpg {

    private static GenJpg _instance;
    public static GenJpg getInstance() {
    	if (_instance==null) {
    		_instance=new GenJpg();
    	}
    	return _instance;
    }
    
    private GenJpg() {
    }

    public void createJpgToStream(OutputStream ostream, DiagramHandler handler) {
    	try {
			ImageIO.write(this.getImageFromDiagram(handler),"jpg",ostream);
			ostream.flush();
			ostream.close();
		} catch (IOException e) {
			System.out.println("UMLet: Error: Exception in outputJpeg: "+e);
		}
    }
    
    public void createAndOutputJpgToFile(String filename, DiagramHandler handler) {
      try {
		File file = new File(filename);
		ImageIO.write(this.getImageFromDiagram(handler), "jpg", file);
      } catch (Exception e) {
      	System.out.println("UMLet: Error: Exception in outputJpeg: "+e);
      }
    }
    
    public BufferedImage getImageFromDiagram(DiagramHandler handler) {
		/*get height and width of drawing area to clip the image*/
		Rectangle bounds=null;
		int padding=20;
		for(Entity e : handler.getDrawPanel().getAllEntities()) {
			if(bounds != null)
				bounds = bounds.union(e.getBounds());
			else
				bounds = e.getBounds();
		}

		int x=0, y=0;
		int width=0, height=0;
        if(bounds != null) {
            width = (int)bounds.getWidth()+padding*2;
            height = (int)bounds.getHeight()+padding*2;
            x = (int)bounds.getX()-padding;
            y = (int)bounds.getY()-padding;
        }
        else
        {
	    	Dimension size=handler.getDrawPanel().getSize();
	    	width = size.width;
	    	height = size.height;
        }

        BufferedImage im=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g=im.createGraphics();
        
        //tanslate needed for clipping
        g.translate(-x, -y);
        g.clipRect(x, y, width, height);
		        
        g.setColor(Color.white);
        g.fillRect(x, y, width, height);
        for(Entity e : handler.getDrawPanel().getAllEntities()) {
        	g.translate(e.getX(), e.getY());
        	e.paint(g);
        	g.translate(-e.getX(), -e.getY());
        }
        
		return im;
    }   
}
