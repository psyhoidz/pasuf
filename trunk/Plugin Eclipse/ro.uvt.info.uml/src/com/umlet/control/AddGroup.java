//Class by A.Mueller Oct.05

package com.umlet.control;

import com.umlet.element.base.*;


public class AddGroup extends Command {
  Group _group;

  public AddGroup() {
    _group = new Group();
  }

  public void execute() {
  	super.execute();
  	_group.groupSelected();
  }

  public void undo() {
  	super.undo();
    _group.ungroup();
  }
}