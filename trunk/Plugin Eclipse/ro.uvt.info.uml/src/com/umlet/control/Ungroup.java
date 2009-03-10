//Class by A.Mueller Oct.05

package com.umlet.control;

import com.umlet.element.base.*;
import java.util.Vector;


public class Ungroup extends Command {
  Group _group;

  public Ungroup(Group group) {
    _group = group;
  }

  public void execute() {
  	super.execute();
  	_group.ungroup();
  }

  public void undo() {
  	super.undo();
    Selector.getInstance().deselectAll();
    Vector<Entity> temp = _group.getMembers();
    for (int i=0; i<temp.size(); i++)
    {
    	Selector.getInstance().selectXXX(temp.get(i));
    }
    _group = new Group();
    _group.groupSelected();
  }
}