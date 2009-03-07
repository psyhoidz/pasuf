// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.control.io;

import javax.swing.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import com.umlet.element.base.*;
import com.umlet.control.command.HelpPanelChanged;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.custom.CustomElementCompiler;

import java.util.ArrayList;
import java.util.List;

public class XMLContentHandler extends DefaultHandler {
  	private JPanel _p=null;
  	private Entity e=null;
  	private String elementtext;
  	
  	private int x;
  	private int y;
  	private int w;
  	private int h;
  	private String entityname;
  	private String code;
  	private String panel_attributes;
  	private String additional_attributes;
  	
  	private Group currentGroup;
  	private DiagramHandler handler;
  	private ClassLoader fcl;
  	
//  to be backward compatible - add list of old elements that were removed so that they are ignored when loading old files
  	private List<String> ignoreElements;  
  	
	public XMLContentHandler(DiagramHandler handler) {
		this.handler = handler;
		_p=handler.getDrawPanel();
		ignoreElements = new ArrayList<String>();
		ignoreElements.add("com.umlet.control.Group");
		currentGroup = null;
		//use classloader of current thread (not systemclassloader - important for eclipse)
		this.fcl = Thread.currentThread().getContextClassLoader();
	}
	
	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName, Attributes attributes) {
		elementtext="";
		if(qName.equals("element")) {
			this.panel_attributes = "";
			this.additional_attributes = "";
			this.code = null;
		}
		if(qName.equals("group"))
		{
			Group g = new Group();
			g.assignToDiagram(this.handler);
			if(currentGroup != null)
				currentGroup.addMember(g);
			currentGroup = g;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) {
		String elementname=qName; //[UB]: we are not name-space aware, so use the qualified name
		
	    if(elementname.equals("help_text")) {
	    	//dont use the command because this causes an exception - dont know why - maybe a swing bug?!?
	    	handler.updateHelpText(elementtext);
	    	handler.setFontsize(HelpPanelChanged.getFontsize(elementtext));
	    }
	    else if (elementname.equals("group")) {
	    	if(this.currentGroup != null) {
	    		this.currentGroup.adjustSize();
	    		_p.add(this.currentGroup);
	    		this.currentGroup = this.currentGroup.getGroup();
	    	}
	    }
	    else if (elementname.equals("element")) {
	    	if(!this.ignoreElements.contains(this.entityname)){ //load classes
				try
				{
					if(this.code == null)
					{
				        java.lang.Class c = fcl.loadClass(this.entityname);
					    e=(Entity) c.newInstance();
					}
					else
						e = CustomElementCompiler.getInstance().genEntity(this.code);
				} catch (Exception ex) { 
					e = new ErrorOccurred();
				}
				e.setBounds(x,y,w,h);
				e.setState(this.panel_attributes);
				e.setAdditionalAttributes(this.additional_attributes);
				e.assignToDiagram(this.handler);
				
				if(this.currentGroup != null)
					this.currentGroup.addMember(e);
				_p.add(e);
			}
	    }
	    else if (elementname.equals("type")){
			this.entityname = elementtext;
		}
		else if (elementname.equals("x")){
			Integer i=new Integer(elementtext);
			x=i.intValue();
		}
		else if (elementname.equals("y")){
			Integer i=new Integer(elementtext);
			y=i.intValue();
		}
		else if (elementname.equals("w")){
			Integer i=new Integer(elementtext);
			w=i.intValue();
		}
		else if (elementname.equals("h")){
			Integer i=new Integer(elementtext);
			h=i.intValue();
		}
		else if (elementname.equals("panel_attributes"))
			this.panel_attributes = elementtext;	
		else if (elementname.equals("additional_attributes"))
			this.additional_attributes = elementtext;
		else if(elementname.equals("custom_code"))
			this.code = elementtext;
	}
	
	public void characters(char[] ch, int start, int length) {
		elementtext+=(new String(ch)).substring(start, start+length);
    }
	
}