package com.umlet.element.base;

import java.awt.*;
import java.util.*;

import com.umlet.control.*;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company:</p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class Class extends Entity {

	// A.Mueller start
	private Vector<Class> innerClasses;
	private boolean _isTemplate = false;
	private boolean _isInnerClass = false;
	private String _panelString = "";
	private int _templateHeight = 0;
	private int _templateWidth = 0;
	// A.Mueller end

	public Entity CloneFromMe()
	{
		Class c = new Class();
		c.setState(this.getPanelAttributes());
		// c.setVisible(true);
		c.setBounds(this.getBounds());
		return c;
	}

	public Class(String s)
	{
		super(s);
		// A.Mueller start
		innerClasses = new Vector<Class>();
		// A.Mueller end
	}

	public Class()
	{
		super();
		// A.Mueller start
		innerClasses = new Vector<Class>();
		// A.Mueller end
	}

	private Vector getStringVector()
	{
		// A.Mueller start
		if (this.isInnerClass())
			return Constants.decomposeStrings(_panelString, "\n");
		// A.Mueller end
		Vector ret = Constants
				.decomposeStrings(this.getPanelAttributes(), "\n");
		return ret;
	}

	// A.Mueller start
	public Vector<Class> getInnerClasses()
	{
		return this.innerClasses;
	}

	public boolean isInnerClass()
	{
		return _isInnerClass;
	}

	public void setIsInnerClass(boolean i)
	{
		this._isInnerClass = i;
	}

	public void setPanelString(String p)
	{
		this._panelString = p;
	}

	// A.Mueller end

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
	
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);

		// Constants.getFRC(g2);

		// A.Mueller start
		int innerSoFar = 0;
		// A.Mueller end

		Vector tmp = getStringVector();

		int yPos = 0;
		yPos += Constants.getDistLineToText();

		boolean CENTER = true;
		for (int i = 0; i < tmp.size(); i++)
		{
			String s = (String) tmp.elementAt(i);
			if (s.equals("--"))
			{
				CENTER = false;

				// A.Mueller start
				if (_isTemplate)
					g2.drawLine(0, yPos, this.getWidth() - 1 - this.getWidth() / 10, yPos);
				else
					g2.drawLine(0, yPos, this.getWidth(), yPos);
				// A.Mueller end

				yPos += Constants.getDistLineToText();

				// A.Mueller start
			} else if (s.equals("{active}") && i == 0)
			{
				g2.drawLine(Constants.getFontsize()/2,0,Constants.getFontsize()/2,this.getHeight()-1);
				g2.drawLine(this.getWidth()-Constants.getFontsize()/2,0,this.getWidth()-Constants.getFontsize()/2,this.getHeight()-1);
				yPos=this.getHeight()/2-(tmp.size()-1)*(Constants.getFontsize()+Constants.getDistTextToText())/2;
			} else if (s.startsWith("template") && i == 0)
			{
				String[] template = s.split("=");
				if (template.length == 2)
				{
					_isTemplate = true;
					_templateWidth = Constants.getPixelWidth(g2, template[1])
							+ Constants.getDistLineToText()
							+ Constants.getDistTextToLine();
					_templateHeight = Constants.getFontsize()
							+ Constants.getDistTextToLine()
							+ Constants.getDistLineToText();
					g2.setStroke(Constants.getStroke(1, 1));
					g2.drawRect(getWidth() - _templateWidth, 0,
							_templateWidth - 1, _templateHeight);
					Constants.write(g2, template[1], getWidth()
							- _templateWidth + Constants.getDistLineToText(),
							Constants.getFontsize()
									+ Constants.getDistLineToText(), false);
					g2.setStroke(Constants.getStroke(0, 1));

					g2.drawLine(0, _templateHeight / 2, getWidth()
							- _templateWidth, _templateHeight / 2);
					g2.drawLine(0, _templateHeight / 2, 0, this.getHeight());
					g2.drawLine(0, this.getHeight() - 1, this.getWidth()
							- this.getWidth() / 10, this.getHeight() - 1);
					g2.drawLine(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1, this.getWidth() - this.getWidth()
							/ 10, _templateHeight);

					yPos = yPos + _templateHeight
							+ Constants.getDistLineToText();
				}
                else _isTemplate= false;

			}

			else if (s.equals("{innerclass"))
			{
				String state = "";
				// Counting the inner lines helps to determine the Height of the
				// resulting
				// innerClass (lines times the fontsize gives us the height of
				// the contents)
				// by adding also the INNER words we make sure to have enough
				// space at the
				// bottom since the lines of the classes also need space
				int innerLines = 0;
				int innerCount = 0;
				for (i++; i < tmp.size(); i++)
				{
					if (tmp.elementAt(i).equals("{innerclass"))
					{
						innerCount++;
						innerLines++;
					} else if (tmp.elementAt(i).equals("innerclass}"))
					{
						if (innerCount > 0)
							innerCount--;
						else
							break;

					} else
						innerLines++;
					state = state + "\n" + tmp.elementAt(i);
				}

				Class temp;
				try
				{
					temp = innerClasses.get(innerSoFar);
					temp.setPanelString(state);
				} catch (ArrayIndexOutOfBoundsException e)
				{

					temp = new Class();
					innerClasses.add(innerSoFar, temp);
					temp.setIsInnerClass(true);
					temp.setPanelString(state);
					innerSoFar++;
				}

				int height = innerLines * Constants.getFontsize()
						+ Constants.getDistLineToText()
						+ Constants.getDistTextToLine()
						+ Constants.getDistTextToText() * --innerLines;

				if (this.isSelected())
					temp.onSelected();
				else
					temp.onDeselected();

				temp.setLocation(5, yPos);

				if (_isTemplate)
					temp.setSize(this.getWidth() - this.getWidth() / 10 - 10, height);
				else
					temp.setSize(this.getWidth() - 10, height);

				temp.removeMouseListener(UniversalListener.getInstance());
				temp.removeMouseMotionListener(UniversalListener.getInstance());
				temp.paint(g.create(5, yPos, this.getWidth() - 5, temp.getHeight()));
				yPos = yPos + temp.getHeight() + Constants.getDistLineToText();

				// A.Mueller end

			} else
			{
				yPos += Constants.getFontsize();
				if (CENTER)
				{
					// A.Mueller start
					if (_isTemplate)
						Constants.write(g2, s, ((int) this.getWidth() - this.getWidth() / 10) / 2, yPos, true);
					else
						Constants.write(g2, s, (int) this.getWidth() / 2, yPos,	true);
					// <OLDCODE> Constants.write(g2,s,(int)this.getWidth()/2,
					// yPos, true); </OLDCODE>
					// A.Mueller end
				} else
				{
					Constants.write(g2, s, Constants.getFontsize() / 2, yPos, false);
				}
				yPos += Constants.getDistTextToText();
			}
		}

		Rectangle r = this.getBounds();
		// A.Mueller start
		if (!_isTemplate)
		{
			g2.drawRect(0, 0, (int) r.getWidth() - 1, (int) r.getHeight() - 1);
		}
		_isTemplate = false;
		// <OLDCODE>g.drawRect(0,0,(int)r.getWidth()-1,(int)r.getHeight()-1);</OLDCODE>
		// A.Mueller end

		/*
		 * if (_selected) {
		 * g.drawRect(1,1,(int)r.getWidth()-3,(int)r.getHeight()-3); }
		 */
	}

	// A.Mueller start
	@Override
	public StickingPolygon getStickingBorder()
	{ // LME: define the polygon on which relations stick on
		if (!_isTemplate)
			return super.getStickingBorder();

		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(0, _templateHeight / 2), new Point(this.getWidth()
				- _templateWidth, _templateHeight / 2), Constants.RESIZE_TOP);
		p.addLine(new Point(this.getWidth() - _templateWidth,
				_templateHeight / 2), new Point(this.getWidth()
				- _templateWidth, 0), Constants.RESIZE_TOP);
		p.addLine(new Point(this.getWidth() - _templateWidth, 0), new Point(
				this.getWidth(), 0), Constants.RESIZE_TOP);
		p.addLine(new Point(this.getWidth(), 0), new Point(this.getWidth(),
				_templateHeight), Constants.RESIZE_RIGHT);
		p.addLine(new Point(this.getWidth(), _templateHeight), new Point(this.getWidth()
				- this.getWidth() / 10, _templateHeight), Constants.RESIZE_RIGHT);
		p.addLine(new Point(this.getWidth() - this.getWidth() / 10, this.getHeight() - 1), new Point(this.getWidth() - this.getWidth()
				/ 10, _templateHeight), Constants.RESIZE_RIGHT);
		p.addLine(new Point(0, _templateHeight / 2), new Point(0, this.getHeight()), Constants.RESIZE_LEFT);
		p.addLine(new Point(0, this.getHeight() - 1), new Point(this.getWidth()
				- this.getWidth() / 10, this.getHeight() - 1), Constants.RESIZE_BOTTOM);

		return p;
	}
	// A.Mueller end

}