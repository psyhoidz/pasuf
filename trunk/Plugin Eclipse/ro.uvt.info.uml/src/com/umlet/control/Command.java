package com.umlet.control;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class Command {

  public void execute() {
	Umlet.getInstance().setChanged(true);
  }
  public void undo() {
	Umlet.getInstance().setChanged(true);
  }
  public boolean isMergeableTo(Command c) { return false; }
  public Command mergeTo(Command c) { return null; }
}