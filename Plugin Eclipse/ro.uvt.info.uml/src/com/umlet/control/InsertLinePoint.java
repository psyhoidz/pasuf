package com.umlet.control;

import java.awt.*;
import java.util.*;

import com.umlet.element.base.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class InsertLinePoint extends Command {
  private Relation _relation; public Relation getRelation() { return _relation; }
  private int _where; public int getWhere() { return _where; }
  private int _x; public int getX() { return _x; }
  private int _y; public int getY() { return _y; }

  public InsertLinePoint(Relation r, int i, int x, int y) {
    _relation=r;
    _where=i;
    _x=x;
    _y=y;
  }

  public void execute() {
	super.execute();
    Vector<Point> tmp=_relation.getLinePoints();
    tmp.insertElementAt(new Point(_x,_y), _where);
    _relation.repaint();
  }
  public void undo() {
	super.undo();
    Vector<Point> tmp=_relation.getLinePoints();
    tmp.removeElementAt(_where);
    _relation.repaint();
  }
}