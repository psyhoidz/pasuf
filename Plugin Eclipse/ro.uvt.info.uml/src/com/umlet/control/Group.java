//Class by A.Mueller Oct.05

package com.umlet.control;

import com.umlet.element.base.Entity;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.geom.Area;

import com.umlet.element.base.detail.RelationLinePoint;

public class Group extends Entity
{
	private Vector<Entity> entities;

	private boolean firstPaint = true;

	public Group()
	{
		super();
		entities = new Vector<Entity>();
	}

	public void groupSelected()
	{
		entities = Selector.getInstance().getSelectedEntities();
		adjustSize();
		Vector<Entity> toRemove = new Vector<Entity>();

		for (int i = 0; i < entities.size(); i++)
		{
			if (entities.get(i) instanceof Group)
			{
				toRemove.add(entities.get(i));
				entities.get(i).getParent().remove(entities.get(i));
			} else
			{

				entities.get(i).setGroup(this);
				entities.get(i).removeMouseListener(
						UniversalListener.getInstance());
				entities.get(i).removeMouseMotionListener(
						UniversalListener.getInstance());

			}
		}
		for (int i = 0; i < toRemove.size(); i++)
		{
			entities.remove(toRemove.get(i));
		}
		Selector.getInstance().deselectAll();
		Selector.getInstance().selectXXX(this);
		if (!entities.isEmpty())
			Controller.getInstance().executeCommandWithoutUndo(
					new AddEntity(this, this.getLocation().x, this
							.getLocation().y));

	}

	public void ungroup()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			entities.get(i).setGroup(null);
			entities.get(i).addMouseListener(UniversalListener.getInstance());
			entities.get(i).addMouseMotionListener(
					UniversalListener.getInstance());
		}
		Vector<Entity> temp = new Vector<Entity>();
		temp.add(this);
		Controller.getInstance().executeCommandWithoutUndo(
				new RemoveElement(temp));
	}

	public Vector<Entity> getMembers()
	{
		return entities;
	}

	public void setMembers(Vector<Entity> members)
	{
		entities = members;
	}

	public void paint(Graphics g)
	{
		if (firstPaint)
		{
			if (entities.isEmpty())
			{
				// assume everything underneath is in group...
				Rectangle insideThis = this.getBounds();
				Vector temp = Selector.getInstance().getAllEntitiesOnPanel(
						(JPanel) this.getParent());
				for (int i = 0; i < temp.size(); i++)
				{
					Entity e = (Entity) temp.get(i);
					if (e != this)
					{
						if (insideThis.contains(e.getBounds()))
						{
							e.removeMouseListener(UniversalListener
									.getInstance());
							e.removeMouseMotionListener(UniversalListener
									.getInstance());
							entities.add(e);
						}
					}
				}

			}
			firstPaint = false;
		}
		if (!this.isSelected())
			return;
		Graphics2D g2 = (Graphics2D) g;
		Constants.getFRC(g2);
		g2.setColor(java.awt.Color.green);
		g2.setStroke(Constants.getStroke(1, 1));
		g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		g2.setStroke(Constants.getStroke(0, 1));
	}

	public void adjustSize()
	{
		if (entities.isEmpty())
			return;
		Enumeration e = entities.elements();
		int maxX = 0;
		int maxY = 0;
		int minX = 1000000000;
		int minY = 1000000000;
		while (e.hasMoreElements())
		{
			Entity temp = (Entity) e.nextElement();
			maxX = Math.max(temp.getX() + temp.getWidth(), maxX);
			maxY = Math.max(temp.getY() + temp.getHeight(), maxY);
			minX = Math.min(temp.getX(), minX);
			minY = Math.min(temp.getY(), minY);
		}
		this.setLocation(minX, minY);
		this.setSize((maxX - minX), (maxY - minY));
	}

	@Override
	public void changeLocation(int diffX, int diffY)
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Vector<RelationLinePoint> relPoints = entities.get(i)
					.getAffectedRelationLinePoints(25);
			for (int j = 0; j < relPoints.size(); j++)
			{
				if (!entities.contains(relPoints.get(j).getRelation()))
					Controller.getInstance().executeCommandWithoutUndo(
							new MoveLinePoint(relPoints.get(j).getRelation(),
									relPoints.get(j).getLinePointId(), diffX,
									diffY));
			}
			entities.get(i).setGroup(null);
			entities.get(i).changeLocation(diffX, diffY);
			entities.get(i).setGroup(this);
		}
		super.changeLocation(diffX, diffY);
	}

	@Override
	public void changeSize(int diffX, int diffY)
	{
			float factor = diffX*100;
			factor = factor/this.getWidth();
			factor=factor/100;

		factor =factor+1f;
		
		System.out.println("resizing by: "+factor);
		System.out.println("diffx/diffy: "+diffX+"/"+diffY);
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.setGroup(null);
			Controller.getInstance().executeCommandWithoutUndo(new Move(e,(int)(e.getX()*factor-e.getX()),(int)(e.getY()*factor-e.getY())));
			Controller.getInstance().executeCommandWithoutUndo(new Resize(e,6,(int)(e.getWidth()*factor-e.getWidth()),(int)(e.getHeight()*factor-e.getHeight())));
			e.setGroup(this);
			
		}
		adjustSize();
	}

	@Override
	public void onSelected()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			if (!entities.get(i).isSelected())
				Selector.getInstance().selectXXX(entities.get(i));
		}
		super.onSelected();
	}

	@Override
	public void onDeselected()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Selector.getInstance().deselect(entities.get(i));
		}
		super.onDeselected();
	}

	@Override
	public StickingPolygon getStickingBorder()
	{
		return null;
	}

	@Override
	public Entity CloneFromMe()
	{
		Selector.getInstance().deselectAll();
		Group temp = new Group();
		Vector<Entity> cloned = new Vector<Entity>();
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i).CloneFromMe();
			e.removeMouseListener(UniversalListener.getInstance());
			e.removeMouseMotionListener(UniversalListener.getInstance());
			Command cmd;
			int MAIN_UNIT = Umlet.getInstance().getMainUnit();

			if (entities.get(i).getParent() == Umlet.getInstance()
					.getPalettePanel())
			{
				cmd = new AddEntity(e, entities.get(i).getX() + MAIN_UNIT * 2
						- this.getX(), entities.get(i).getY() + MAIN_UNIT * 2
						- this.getY());
			} else
			{
				cmd = new AddEntity(e, entities.get(i).getX() + MAIN_UNIT * 2,
						entities.get(i).getY() + MAIN_UNIT * 2);
			}

			e.setState(entities.get(i).getState());
			cloned.add(e);
			Controller.getInstance().executeCommandWithoutUndo(cmd);
		}
		temp.setMembers(cloned);
		temp.adjustSize();
		return temp;
	}

}
