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

public class RemoveElement extends Command {
  Vector _entities;

  public RemoveElement(Vector v) {
    _entities=v;
  }

  public void execute() {
	super.execute();
    for (int i=0; i<_entities.size(); i++) {
      Entity e=(Entity)_entities.elementAt(i);
      Umlet.getInstance().getPanel().remove(e);
    }
    Umlet.getInstance().getPanel().repaint();
    Selector.getInstance().deselectAll();
  }
  public void undo() {
	super.undo();
    for (int i=0; i<_entities.size(); i++) {
      Entity e=(Entity)_entities.elementAt(i);
      Umlet.getInstance().getPanel().add(e);
    }
    Umlet.getInstance().getPanel().repaint();
  }
}