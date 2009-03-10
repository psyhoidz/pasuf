package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.util.Vector;
import java.awt.Point;
import com.umlet.control.Constants;
import com.umlet.element.base.Entity;
import com.umlet.control.StickingPolygon;

public class Node extends Entity {
	
	public Node()
	{
		super();
		this.setTransparentSelection(true);
	}
	public Node(String s)
	{
		super(s);
		this.setTransparentSelection(true);
	}
	
	public void paint (Graphics g)
	{
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
	    g2.setColor(_activeColor);
	    Constants.getFRC(g2);

		
		
		 Vector tmp=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		    int yPos=Constants.getDistLineToText();
		    yPos = yPos+this.getHeight()/15;
		    for (int i=0; i<tmp.size(); i++) {
		      String s=(String)tmp.elementAt(i);
		      yPos+=Constants.getFontsize();
		      if (s.startsWith("center:"))
		      {
		    	  s = s.substring(7);
		    	  Constants.write(g2,s,this.getWidth()/2, yPos, true);
		      }
		      else Constants.write(g2,s,Constants.getFontsize()/2, yPos, false);
		      
		      yPos+=Constants.getDistTextToText();
		    }

		
		g2.drawLine(0,this.getHeight()/15,0,this.getHeight()-1);
		g2.drawLine(0,this.getHeight()-1,this.getWidth()-this.getWidth()/15,this.getHeight()-1);
		Polygon p = new Polygon();
		p.addPoint(this.getWidth()-this.getWidth()/15,this.getHeight());
		p.addPoint(this.getWidth()-this.getWidth()/15,this.getHeight()/15);
		p.addPoint(this.getWidth()-1,0);
		p.addPoint(this.getWidth()-1,this.getHeight()-this.getHeight()/15);
		
		Polygon p1 = new Polygon();
		p1.addPoint(0,this.getHeight()/15);
		p1.addPoint(this.getWidth()/15,0);
		p1.addPoint(this.getWidth(),0);
		p1.addPoint(this.getWidth()-this.getWidth()/15,this.getHeight()/15);
		
		g2.setColor(new Color(200,200,200));
		g2.fillPolygon(p);
		g2.fillPolygon(p1);
		g2.setColor(_activeColor);
		g2.drawPolygon(p1);
		g2.drawPolygon(p);

	}
/*
	@Override
	public void changeLocation(int diffX, int diffY)
	{
		Rectangle insideThis = this.getBounds();
		insideThis.setSize((int)insideThis.getWidth()-2,(int)insideThis.getHeight()-2);
		Vector entities = Selector.getInstance().getAllEntitiesOnPanel();
		for (int i=0; i<entities.size(); i++)
		{
			Entity temp = (Entity)entities.get(i);
			if (insideThis.contains(temp.getBounds()) && temp != this)
				{
					Vector<RelationLinePoint> relPoints = temp.getAffectedRelationLinePoints(15);
					for (int j=0; j< relPoints.size(); j++)
					{
						if (!this.getBounds().contains(relPoints.get(j).getRelation().getBounds())) Controller.getInstance().executeCommandWithoutUndo(new MoveLinePoint(relPoints.get(j).getRelation(),relPoints.get(j).getLinePointId(),diffX,diffY));
					}
					Controller.getInstance().executeCommandWithoutUndo(new Move(temp, diffX,diffY));
				}
		}
		super.changeLocation(diffX,diffY);

	}
*/
	@Override
	public StickingPolygon getStickingBorder()
	{
		StickingPolygon p = new StickingPolygon();
		p.addLine(new Point(0,this.getHeight()/15), new Point(0,this.getHeight()),Constants.RESIZE_LEFT);
		p.addLine(new Point(0,this.getHeight()), new Point(this.getWidth()-this.getWidth()/15, this.getHeight()),Constants.RESIZE_BOTTOM);
		p.addLine(new Point(this.getWidth()-this.getWidth()/15, this.getHeight()), new Point(this.getWidth(),this.getHeight()-this.getHeight()/15), Constants.RESIZE_BOTTOM);
		p.addLine(new Point(this.getWidth(),this.getHeight()-this.getHeight()/15),new Point(this.getWidth(),0), Constants.RESIZE_RIGHT);
		p.addLine(new Point(this.getWidth()/15,0), new Point(this.getWidth(),0), Constants.RESIZE_TOP);
		p.addLine(new Point(this.getWidth()/15,0), new Point(0, this.getHeight()/15), Constants.RESIZE_TOP);
		return p;
	}
}
