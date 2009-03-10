package com.umlet.control;

import com.umlet.element.base.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class AddEntity extends Command {
  Entity _entity;
  int _x;
  int _y;

  public AddEntity(Entity e, int x, int y) {
    _entity=e;
    _x=x;
    _y=y;
  }

  public void execute() {
  	super.execute();
    Umlet.getInstance().getPanel().add(_entity);
    _entity.setLocation(_x,_y);
    Umlet.getInstance().getPanel().repaint();
  }

  public void undo() {
  	super.undo();
    Umlet.getInstance().getPanel().remove(_entity);
    Umlet.getInstance().getPanel().repaint();
  }
}