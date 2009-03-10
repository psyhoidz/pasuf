package com.umlet.control;

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

public class Zoom extends Command {
  private Vector _entities;
  public Vector getEntities() {
    return _entities;
  }
  private double _z;
  public double getZ() {
    return _z;
  }
  public Zoom(Vector v, double z) {
    _entities=v;
    _z=z;
  }

  public void execute() {
	super.execute();
    int i;
    for (i=0;i<_entities.size();i++) {
      Entity e=(Entity)_entities.elementAt(i);
      e.zoomSize(_z);
      e.zoomLocation(_z, Umlet.getInstance().getCenterX(), Umlet.getInstance().getCenterY());
    }
  }
  public void undo() {
	super.undo();
    int i;
    for (i=0;i<_entities.size();i++) {
      Entity e=(Entity)_entities.elementAt(i);
      e.zoomSize(1/_z);
      e.zoomLocation(1/_z, Umlet.getInstance().getCenterX(), Umlet.getInstance().getCenterY());
    }
  }
}