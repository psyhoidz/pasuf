package com.umlet.control.io;

import java.awt.*;
import java.io.*;
import java.util.*;

import org.apache.batik.dom.*;
import org.apache.batik.svggen.*;
import org.w3c.dom.*;

import com.umlet.control.*;
import com.umlet.element.base.Entity;
     
public class GenSvg {
	private static GenSvg _instance;
	public static GenSvg getInstance() {
		if (_instance==null) {
			_instance=new GenSvg();
		}
		return _instance;
	}

    public void paint(Graphics2D g2d) {
        /*g2d.setPaint(Color.red);
        g2d.fill(new Rectangle(10, 10, 100, 100));
        */    
        /*Actor a=new Actor();
	    a.setSize(80,120);
	    a.setState("Testactor");
	    a.setLocation(40,150);*/
	    
	    Vector v=Selector.getInstance().getAllEntitiesOnPanel();
    
        for (int i=0; i<v.size(); i++) {
        	Entity e=(Entity)v.elementAt(i);
            g2d.translate(e.getX(), e.getY());
            e.paint(g2d);
            g2d.translate(-e.getX(), -e.getY());
        }
        
        //Frame.IS_CLIPPING=true;
        //Frame.getInstance().getPanel().paint(g2d);
    }

    public static void genSvg() {
      try {
        // Get a DOMImplementation
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation
        GenSvg test = new GenSvg();
        test.paint(svgGenerator);

        // Finally, stream out SVG to the standard output using UTF-8
        // character to byte encoding
        boolean useCSS = true; // we want to use CSS style attribute
        Writer out = new OutputStreamWriter(System.out, "UTF-8");
        svgGenerator.stream(out, useCSS);
      } catch (IOException e) {
      	System.out.println("IO Exception.");
      }
    }
    
    public static void createAndOutputSvgToFile(String filename) {
      try {
		OutputStream ostream = new FileOutputStream(filename);
		createSvgToStream(ostream);
      } catch (IOException e) {
      	System.out.println("IO Exception.");
      }
    }
    
	public static void createSvgToStream(OutputStream ostream) {
		try {
			// Get a DOMImplementation
			 DOMImplementation domImpl =
				 GenericDOMImplementation.getDOMImplementation();

			 // Create an instance of org.w3c.dom.Document
			 Document document = domImpl.createDocument(null, "svg", null);

			 // Create an instance of the SVG Generator
			 SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

			 // Ask the test to render into the SVG Graphics2D implementation
			 GenSvg test = new GenSvg();
			 test.paint(svgGenerator);

			 // Finally, stream out SVG to the standard output using UTF-8
			 // character to byte encoding
			 boolean useCSS = true; // we want to use CSS style attribute
			 //
			Writer out = new OutputStreamWriter(ostream,"UTF-8");
			 //Writer out = new OutputStreamWriter( new FileOutputStream(filename), "UTF-8");
			 //BufferedWriter bw = new BufferedWriter(
		
			 svgGenerator.stream(out, useCSS);
			//OutputStream ostream = new FileOutputStream("c:\\t\\out.pdf");
			/*TranscoderOutput output = new TranscoderOutput(ostream);
			// save the image
			t.transcode(input, output);
			// flush and close the stream then exit
			ostream.flush();
			ostream.close();*/			 		
		} catch (Exception e) {
		  	 System.out.println("UMLet: Error: Exception in outputPdf: "+e);
		}
    }
}



















