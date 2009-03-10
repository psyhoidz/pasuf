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

public class CreateInterface extends Command {
  String _state;
  int _x;
  int _y;
  Interface _c;

  public CreateInterface(int x, int y, String s) {
    _x=x;
    _y=y;
    _state=s;
    _c=new Interface();
    _c.setState(s);
  }

  public void execute() {
	super.execute();
    Umlet.getInstance().getPanel().add(_c);
    _c.setBounds(_x,_y,120,120);
    //_c.setLocation(_x,_y);
    _c.setVisible(true);

    Umlet.getInstance().getPanel().repaint();
  }
  public void undo() {
	super.undo();
    Umlet.getInstance().getPanel().remove(_c);
    Umlet.getInstance().getPanel().repaint();
  }
}