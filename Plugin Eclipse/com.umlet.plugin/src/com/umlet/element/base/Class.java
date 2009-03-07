// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;
import com.umlet.control.diagram.StickingPolygon;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company:</p>
 * 
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public class Class extends Entity {

	// A.Mueller start
	private Vector<Class> innerClasses;
	private boolean _isTemplate = false;
	private boolean _isInnerClass = false;
	private String _panelString = "";
	private int _templateHeight = 0;
	private int _templateWidth = 0;
	// A.Mueller end

	public Class() {
		super();
		innerClasses = new Vector<Class>(); //A.Mueller
	}

	private Vector<String> getStringVector() {
		if (this.isInnerClass()) return Constants.decomposeStrings(_panelString, "\n"); //A.Mueller
		Vector<String> ret = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		return ret;
	}

	// A.Mueller start
	public Vector<Class> getInnerClasses() {
		return this.innerClasses;
	}

	public boolean isInnerClass() {
		return _isInnerClass;
	}

	public void setIsInnerClass(boolean i) {
		this._isInnerClass = i;
	}

	public void setPanelString(String p) {
		this._panelString = p;
	}
	// A.Mueller end

	public void paintEntity(Graphics g) {
		int innerSoFar = 0; //A.Mueller
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.handler.getFont());
		this.handler.getFRC(g2);
		Vector<String> tmp = getStringVector();
		int yPos = 0;

		yPos += this.handler.getDistLineToText();

		Composite[] composites = colorize(g2); //LME: enable colors
		g2.setColor(_activeColor);
	    		
		Rectangle r = this.getBounds();
		boolean DASHED = false;
		for (int i = 0; i < tmp.size(); i++) {
			String s = (String) tmp.elementAt(i);
			if (s.equals("lt=.")) {
				DASHED = true;
			} else if (s.startsWith("template") && i == 0) {
				String[] template = s.split("=");
				if (template.length != 2)  
					_isTemplate= false;
				else 
					_isTemplate=true;
			}
		}
		
		if (!_isTemplate) {
			g2.setComposite(composites[1]); //set aplha composite for drawing the background color
		    g2.setColor(_fillColor);
			g2.fillRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
			g2.setComposite(composites[0]); //reset composite settings
			if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
			if(DASHED)
				g2.setStroke(Constants.getStroke(1,1));
			g2.drawRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
		}
		
		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++) {
			String s = (String) tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;

				// A.Mueller start
				if (_isTemplate) g2.drawLine(0, yPos, this.getWidth() - 1 - this.getWidth() / 10, yPos);
				else g2.drawLine(0, yPos, this.getWidth()-1, yPos);
				// A.Mueller end

				yPos += this.handler.getDistLineToText();

				// A.Mueller start
			} else if (s.equals("lt=.")) {
				DASHED = true;
			} else if (s.equals("{active}") && i == 0) {
				g2.drawLine(this.handler.getFontsize()/2,0,this.handler.getFontsize()/2,this.getHeight()-1);
				g2.drawLine(this.getWidth()-this.handler.getFontsize()/2,0,this.getWidth()-this.handler.getFontsize()/2,this.getHeight()-1);
				yPos=this.getHeight()/2-(tmp.size()-1)*(this.handler.getFontsize()+this.handler.getDistTextToText())/2;
			} else if (s.startsWith("template") && i == 0) {
				String[] template = s.split("=");
				if (template.length == 2) {
					
					_templateWidth = this.handler.getPixelWidth(g2, template[1]) + this.handler.getDistLineToText() + this.handler.getDistTextToLine();
					_templateHeight = this.handler.getFontsize() + this.handler.getDistTextToLine() + this.handler.getDistLineToText();

					
					Polygon border = new Polygon();
					border.addPoint(0, _templateHeight / 2); border.addPoint(getWidth() - _templateWidth, _templateHeight / 2);
					border.addPoint(0, _templateHeight / 2); border.addPoint(0, this.getHeight());
					border.addPoint(0, this.getHeight() - 1); border.addPoint(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1);
					border.addPoint(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1); border.addPoint(this.getWidth() - this.getWidth() / 10, _templateHeight+1);
					border.addPoint(getWidth() - _templateWidth, _templateHeight+1);
					border.addPoint(getWidth() - _templateWidth, _templateHeight / 2);
					
					
					g2.setStroke(Constants.getStroke(1, 1));
					g2.setComposite(composites[1]); //set alpha composite
				    g2.setColor(_fillColor);
				    g2.fillRect(getWidth() - _templateWidth, 0,	_templateWidth, _templateHeight+1);
				    g2.fillPolygon(border);
				    g2.setComposite(composites[0]); //reset composite
					if(_selected) g2.setColor(_activeColor); else g2.setColor(_deselectedColor);
				    
					//draw border lines of template box
					g2.drawRect(getWidth() - _templateWidth, 0,	_templateWidth - 1, _templateHeight); //template box
					this.handler.writeText(g2, template[1], getWidth()	- _templateWidth + this.handler.getDistLineToText(), this.handler.getFontsize()	+ this.handler.getDistLineToText(), false);
					g2.setStroke(Constants.getStroke(0, 1));

					//draw border lines of class
					g2.drawLine(0, _templateHeight / 2, getWidth() - _templateWidth, _templateHeight / 2);
					g2.drawLine(0, _templateHeight / 2, 0, this.getHeight());
					g2.drawLine(0, this.getHeight() - 1, this.getWidth() - this.getWidth() / 10, this.getHeight() - 1);
					g2.drawLine(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1, this.getWidth() - this.getWidth() / 10, _templateHeight);

					yPos = yPos + _templateHeight + this.handler.getDistLineToText();
				}
                else _isTemplate= false;
			} else if (s.equals("{innerclass")) {
				String state = "";
				// Counting the inner lines helps to determine the Height of the
				// resulting innerClass (lines times the fontsize gives us the height of
				// the contents) by adding also the INNER words we make sure to have enough
				// space at the bottom since the lines of the classes also need space
				int innerLines = 0;
				int innerCount = 0;
				for (i++; i < tmp.size(); i++) {
					if (tmp.elementAt(i).equals("{innerclass")) {
						innerCount++;
						innerLines++;
					} else if (tmp.elementAt(i).equals("innerclass}")) {
						if (innerCount > 0)	innerCount--; else break;
					} else innerLines++;
					state = state + "\n" + tmp.elementAt(i);
				}

				Class temp;
				try {
					temp = innerClasses.get(innerSoFar);
					temp.setPanelString(state);
				} catch (ArrayIndexOutOfBoundsException e) {
					temp = new Class();
					innerClasses.add(innerSoFar, temp);
					temp.handler = this.handler;
					temp.setIsInnerClass(true);
					temp.setPanelString(state);
					innerSoFar++;
				}

				int height = innerLines * this.handler.getFontsize() + this.handler.getDistLineToText()
						+ this.handler.getDistTextToLine()	+ this.handler.getDistTextToText() * --innerLines;

				if (this.isSelected()) temp.onSelected();
				else temp.onDeselected();

				temp.setLocation(5, yPos);

				if (_isTemplate) temp.setSize(this.getWidth() - this.getWidth() / 10 - 10, height);
				else temp.setSize(this.getWidth() - 10, height);

				temp.paintEntity(g.create(5, yPos, this.getWidth() - 5, temp.getHeight()));
				yPos = yPos + temp.getHeight() + this.handler.getDistLineToText();

				// A.Mueller end

			} else {
				if(_selected) g2.setColor(_activeColor);
			    else g2.setColor(_deselectedColor);
				yPos += this.handler.getFontsize();
				if (CENTER) {
					//A.Mueller
					if (_isTemplate) this.handler.writeText(g2, s, ((int) this.getWidth() - this.getWidth() / 10) / 2, yPos, true); 
					else this.handler.writeText(g2, s, (int) this.getWidth() / 2, yPos, true);
				} else {
					this.handler.writeText(g2, s, this.handler.getFontsize() / 2, yPos, false);
				}
				yPos += this.handler.getDistTextToText();
			}
		}
		
		_isTemplate = false;
	}

	public StickingPolygon getStickingBorder() { //A.Mueller
		// LME: define the polygon on which relations stick on
		if (!_isTemplate) return super.getStickingBorder();
		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(0, _templateHeight / 2), new Point(this.getWidth() - _templateWidth, _templateHeight / 2));
		p.addLine(new Point(this.getWidth() - _templateWidth, _templateHeight / 2), new Point(this.getWidth() - _templateWidth, 0));
		p.addLine(new Point(this.getWidth() - _templateWidth, 0), new Point(this.getWidth(), 0));
		p.addLine(new Point(this.getWidth(), 0), new Point(this.getWidth(),	_templateHeight));
		p.addLine(new Point(this.getWidth(), _templateHeight), new Point(this.getWidth() - this.getWidth() / 10, _templateHeight));
		p.addLine(new Point(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1), new Point(this.getWidth() - this.getWidth() / 10, _templateHeight));
		p.addLine(new Point(0, _templateHeight / 2), new Point(0, this.getHeight()));
		p.addLine(new Point(0, this.getHeight() - 1), new Point(this.getWidth()	- this.getWidth() / 10, this.getHeight() - 1));
		return p;
	}

	
}