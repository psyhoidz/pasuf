package com.umlet.control;

import javax.swing.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import com.umlet.element.base.*;
import com.umlet.control.io.*;
import java.io.*;



public class XMLContentHandler extends DefaultHandler {
  	private JPanel _p=null;
  	private Entity e=null;
  	private String elementtext;
  	private int x;
  	private int y;
  	private int w;
  	private int h;
  	
  	private int customTypesLoaded=0;
  	private int customTypesLoadedWError=0;
  	
	public XMLContentHandler(JPanel p) {
		_p=p;
	}
	
	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) {
		elementtext="";
	}
	
	
	
	
	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) {
	    String elementname=qName; //[UB]: we are not name-space aware, so use the qualified name
		
		if (elementname.equals("type")){
			java.lang.Class c=null;
			/*try {
				c=java.lang.Class.forName(elementtext);
				e=(Entity) c.newInstance();
			} catch (Exception e) { 

			} */
			try {
				//LME3 use own class loader
				String elementPath=elementtext.replace('.',File.separatorChar);
				String homePath=Umlet.getInstance().getHomePath();
				File classFile = new File(homePath+Umlet.CUSTOM_ELEMENTS_PATH+elementPath+".class");
				File javaFile = new File(homePath+Umlet.CUSTOM_ELEMENTS_PATH+elementPath+".java");
				if(classFile.exists()||javaFile.exists()) {
					String[] fileNameStrArr = {
						homePath+Umlet.CUSTOM_ELEMENTS_PATH+elementPath+".java",
						elementtext
					};
					e = CustomElementLoader.getInstance().doLoadClass(fileNameStrArr, false, true); //compile and load the modified class, silent error reporting
					customTypesLoaded++; //LME4 count loaded classes
					customTypesLoadedWError=CustomElementLoader.getInstance().getErrorCounter(); //count errors while loading classes
				} else { //load jar-classes conventianally
					c=java.lang.Class.forName(elementtext);
					e=(Entity) c.newInstance();
				}
				_p.add(e);
			} catch (Exception e) { 
				System.err.println("UMLet -> XMLContentHandler -> endElement(): "+e); //LME3
			}
			//_p.add(e);
			return;
		}
		if (elementname.equals("x")){
			Integer i=new Integer(elementtext);
			x=i.intValue();
			return;
		}
		if (elementname.equals("y")){
			Integer i=new Integer(elementtext);
			y=i.intValue();
			return;
		}
		if (elementname.equals("w")){
			Integer i=new Integer(elementtext);
			w=i.intValue();
			return;
		}
		if (elementname.equals("h")){
			Integer i=new Integer(elementtext);
			h=i.intValue();
			
			e.setLocation(x,y);
			e.setSize(w,h);
			return;
		}
		if (elementname.equals("panel_attributes")){
			e.setState(elementtext);
			return;
		}		
		if (elementname.equals("additional_attributes")){
			e.setAdditionalAttributes(elementtext);
			return;
		}		
	}
	public void characters(char[] ch, int start, int length) {
		elementtext+=(new String(ch)).substring(start, start+length);
    }
	
}
