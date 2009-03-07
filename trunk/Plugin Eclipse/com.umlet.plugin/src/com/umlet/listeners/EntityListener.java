package com.umlet.listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPopupMenu;

import com.umlet.constants.Constants;
import com.umlet.control.Umlet;
import com.umlet.control.command.AddEntity;
import com.umlet.control.command.Command;
import com.umlet.control.command.Macro;
import com.umlet.control.command.Move;
import com.umlet.control.command.MoveLinePoint;
import com.umlet.control.command.Resize;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.element.base.Entity;
import com.umlet.element.base.Relation;
import com.umlet.element.base.relation.RelationLinePoint;

public class EntityListener extends UniversalListener {

	private static HashMap<DiagramHandler,EntityListener> entitylistener = new HashMap<DiagramHandler,EntityListener>();
	
	public static EntityListener getInstance(DiagramHandler handler) {
		if(!entitylistener.containsKey(handler))
			entitylistener.put(handler, new EntityListener(handler));
		return entitylistener.get(handler);
	}
	
	protected boolean IS_DRAGGING = false;
	private boolean IS_RESIZING = false;
	private int RESIZE_DIRECTION = 0;
	private boolean IS_FIRST_DRAGGING_OVER = false;
	private boolean IS_FIRST_RESIZE_OVER = false;
	private Vector<Command> ALL_MOVE_COMMANDS = null;
	private Vector<MoveLinePoint> ALL_RESIZE_COMMANDS = null;
	private boolean DESELECT_MULTISEL = false;
	
	protected EntityListener(DiagramHandler handler) {
		super(handler);
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		super.mouseDragged(me);
		if(this.doReturn())
			return;
		
		Entity e = (Entity)me.getComponent();
		int MAIN_UNIT = Umlet.getInstance().getMainUnit();
		
//		 delta
		int delta_x = 0;
		int delta_y = 0;
		if (IS_RESIZING) {
			if (RESIZE_DIRECTION == 3 || RESIZE_DIRECTION == 2
					|| RESIZE_DIRECTION == 6) {
				delta_x = (e.getX() + e.getWidth()) % MAIN_UNIT;
			}
			if (RESIZE_DIRECTION == 12 || RESIZE_DIRECTION == 4
					|| RESIZE_DIRECTION == 6) {
				delta_y = (e.getY() + e.getHeight()) % MAIN_UNIT;
			}
		}

		Point newp = this.getNewCoordinate();
		Point oldp = this.getOldCoordinate();
		
		int diffx = newp.x - oldp.x - delta_x;
		int diffy = newp.y - oldp.y - delta_y;

		if (IS_DRAGGING == true) {
			DESELECT_MULTISEL = false;
			if (IS_FIRST_DRAGGING_OVER == false) {
				Vector<Entity> entitiesToBeMoved = new Vector<Entity>(
						this.selector.getSelectedEntities());

				Vector<Move> moveCommands = new Vector<Move>();
				for (Entity eToBeMoved : entitiesToBeMoved)
					moveCommands.add(new Move(eToBeMoved, diffx, diffy));

				Vector<MoveLinePoint> linepointCommands = new Vector<MoveLinePoint>();
				for (int i = 0; i < entitiesToBeMoved.size(); i++) {
					Entity tmpEntity = entitiesToBeMoved.elementAt(i);
					if (tmpEntity instanceof Relation)
						continue;
					Vector<RelationLinePoint> affectedRelationPoints = tmpEntity
							.getAffectedRelationLinePoints(15);
					for (int j = 0; j < affectedRelationPoints.size(); j++) {
						RelationLinePoint tmpRlp = affectedRelationPoints
								.elementAt(j);
						if (entitiesToBeMoved
								.contains(tmpRlp.getRelation()))
							continue;
						linepointCommands.add(new MoveLinePoint(tmpRlp
								.getRelation(), tmpRlp.getLinePointId(),
								diffx, diffy));
					}
				}
				Vector<Command> allCommands = new Vector<Command>();
				allCommands.addAll(moveCommands);
				allCommands.addAll(linepointCommands);

				ALL_MOVE_COMMANDS = allCommands;
				IS_FIRST_DRAGGING_OVER = true;
			} else {
				Vector<Command> tmpVector = new Vector<Command>();
				for (int i = 0; i < ALL_MOVE_COMMANDS.size(); i++) {
					Command tmpCommand = ALL_MOVE_COMMANDS.elementAt(i);
					if (tmpCommand instanceof Move) {
						Move m = (Move) tmpCommand;
						tmpVector
								.add(new Move(m.getEntity(), diffx, diffy));
					} else if (tmpCommand instanceof MoveLinePoint) {
						MoveLinePoint m = (MoveLinePoint) tmpCommand;
						tmpVector.add(new MoveLinePoint(m.getRelation(), m
								.getLinePointId(), diffx, diffy));
					}
				}
				ALL_MOVE_COMMANDS = tmpVector;
			}

			this.controller.executeCommand(
					new Macro(ALL_MOVE_COMMANDS));

		} else if (IS_RESIZING == true) {
			
			e.setManualResized(true);
			if (RESIZE_DIRECTION == 9 || RESIZE_DIRECTION == 8
					|| RESIZE_DIRECTION == 12)
				if ((e.getWidth() - diffx) < this.handler.getFontsize())
					return;
				else if (RESIZE_DIRECTION == 8)
					diffy = 0;
			if (RESIZE_DIRECTION == 3 || RESIZE_DIRECTION == 2
					|| RESIZE_DIRECTION == 6)
				if ((e.getWidth() + diffx) < this.handler.getFontsize())
					return;
				else if (RESIZE_DIRECTION == 2)
					diffy = 0;

			if (RESIZE_DIRECTION == 9 || RESIZE_DIRECTION == 1
					|| RESIZE_DIRECTION == 3)
				if ((e.getHeight() - diffy) < this.handler.getFontsize())
					return;
				else if (RESIZE_DIRECTION == 1)
					diffx = 0;
			if (RESIZE_DIRECTION == 12 || RESIZE_DIRECTION == 4
					|| RESIZE_DIRECTION == 6)
				if ((e.getHeight() + diffy) < this.handler.getFontsize())
					return;
				else if (RESIZE_DIRECTION == 4)
					diffx = 0;

			Vector<Command> resizeCommands = new Vector<Command>();
			resizeCommands
					.add(new Resize(e, RESIZE_DIRECTION, diffx, diffy));
			if (IS_FIRST_RESIZE_OVER == false) {
				ALL_RESIZE_COMMANDS = new Vector<MoveLinePoint>();

				Vector<RelationLinePoint> affectedRelationLinePoints = e
						.getAffectedRelationLinePoints(RESIZE_DIRECTION);
				for (int i = 0; i < affectedRelationLinePoints.size(); i++) {
					RelationLinePoint rlp = affectedRelationLinePoints
							.elementAt(i);
					ALL_RESIZE_COMMANDS.add(new MoveLinePoint(rlp
							.getRelation(), rlp.getLinePointId(), diffx,
							diffy));
				}
				IS_FIRST_RESIZE_OVER = true;
			} else {
				Vector<MoveLinePoint> tmpVector = new Vector<MoveLinePoint>();
				for (int i = 0; i < ALL_RESIZE_COMMANDS.size(); i++) {
					MoveLinePoint m = ALL_RESIZE_COMMANDS.elementAt(i);
					tmpVector.add(new MoveLinePoint(m.getRelation(), m
							.getLinePointId(), diffx, diffy));
				}
				ALL_RESIZE_COMMANDS = tmpVector;
			}
			resizeCommands.addAll(ALL_RESIZE_COMMANDS);
			this.controller.executeCommand(
					new Macro(resizeCommands));
		}
	}

	@Override
	protected Point getOffset(MouseEvent me) {
		return new Point(me.getX() + me.getComponent().getX(), me.getY() + me.getComponent().getY());
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		super.mouseMoved(me);
		Entity e = (Entity) me.getComponent();
		int ra = e.getResizeArea(me.getX(), me.getY());
		ra = ra & e.getPossibleResizeDirections(); // LME
		if (0 != ra) {
			if (ra == 1 | ra == 4)
				Umlet.getInstance().getGUI().setCursor(Constants.tbCursor);
			if (ra == 2 | ra == 8)
				Umlet.getInstance().getGUI().setCursor(Constants.lrCursor);
			if (ra == 3 | ra == 12)
				Umlet.getInstance().getGUI().setCursor(Constants.neCursor);
			if (ra == 6 | ra == 9)
				Umlet.getInstance().getGUI().setCursor(Constants.nwCursor);
		} else
			Umlet.getInstance().getGUI().setCursor(Constants.handCursor);
	}

	private void showContextMenu(Entity me, int x, int y) 
	{
		JPopupMenu contextMenu = Umlet.getInstance().getGUI().getContextMenu(me);
		if(contextMenu != null)
			contextMenu.show(me, x, y);
		if(!this.selector.getSelectedEntities().contains(me))
			this.selector.singleSelect(me);	
	}
	
	@Override
	public void mousePressed(MouseEvent me) 
	{
		super.mousePressed(me);
		Entity tmp = (Entity)me.getComponent();
		
		if (me.getButton() == MouseEvent.BUTTON3) {
			this.showContextMenu(tmp, me.getX(), me.getY());
		}
		else if(me.getButton() == MouseEvent.BUTTON1)
		{
			if(me.getClickCount() == 2)
				this.mouseDoubleClicked(tmp);
			else if (me.getClickCount() == 1) 
			{
				int ra = tmp.getResizeArea(me.getX(), me.getY());
				ra = ra & tmp.getPossibleResizeDirections(); // LME
	
				if (ra != 0) {
					IS_RESIZING = true;
					RESIZE_DIRECTION = ra;
				} else {
					IS_DRAGGING = true;
					if ((me.getModifiers() & MouseEvent.CTRL_MASK) != 0) {
						if (tmp.isSelected())
							DESELECT_MULTISEL = true;
						else
							this.selector.select(tmp);
					}
				}	
				
				if(!this.selector.getSelectedEntities().contains(tmp))
					this.selector.singleSelect(tmp);
				else
					this.selector.updateSelectorInformation();
			}
		}
	}
	
	public void mouseDoubleClicked(Entity me) {
		Entity e = me.CloneFromMe();
		Command cmd;
		int MAIN_UNIT = Umlet.getInstance().getMainUnit();
		cmd = new AddEntity(e, me.getX() + MAIN_UNIT * 2, me.getY()
				+ MAIN_UNIT * 2);
		this.controller.executeCommand(cmd);
		this.selector.singleSelect(e);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		Entity e = (Entity)  me.getComponent();

		int mod = me.getModifiers();
		if ((mod & MouseEvent.CTRL_MASK) != 0) {
			if (e.isSelected() && DESELECT_MULTISEL)
				this.selector.deselect(e);
		}

		DESELECT_MULTISEL = false;
		IS_DRAGGING = false;
		IS_RESIZING = false;
		IS_FIRST_DRAGGING_OVER = false;
		IS_FIRST_RESIZE_OVER = false;
		ALL_MOVE_COMMANDS = null;
		ALL_RESIZE_COMMANDS = null;
	}

	
	
}
