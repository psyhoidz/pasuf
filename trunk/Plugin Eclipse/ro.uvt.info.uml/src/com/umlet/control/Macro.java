package com.umlet.control;

import java.util.*;

public class Macro extends Command {
  private Vector _commands;
  public Vector getCommands() { return _commands; }
  
  public Macro(Vector v) {
  	_commands=v;
  }
  
  public void execute() {
	super.execute();
  	for (int i=0; i<_commands.size(); i++) {
  	  Command c=(Command)_commands.elementAt(i);
  	  c.execute();
  	}
  }
  public void undo() {
	super.undo();
  	for (int i=0; i<_commands.size(); i++) {
  	  Command c=(Command)_commands.elementAt(i);
  	  c.undo();
  	}
  }
  public boolean isMergeableTo(Command c) {
  	if (!(c instanceof Macro)) return false;
  	Macro m=(Macro)c;
  	Vector v=m.getCommands();
  	if (this.getCommands().size()!=v.size()) return false;
  	for (int i=0; i<this.getCommands().size(); i++) {
  	  Command c1=(Command)this.getCommands().elementAt(i);
  	  Command c2=(Command)v.elementAt(i);
  	  if (!(c1.isMergeableTo(c2))) return false;
  	}
  	return true;
  }
  public Command mergeTo(Command c) {
  	Macro m=(Macro)c;
  	Vector v=m.getCommands();

  	Vector<Command> vectorOfCommands=new Vector<Command>();
  	Command ret=new Macro(vectorOfCommands);

  	for (int i=0; i<this.getCommands().size(); i++) {
  	  Command c1=(Command)this.getCommands().elementAt(i);
  	  Command c2=(Command)v.elementAt(i);
  	  Command c3=c1.mergeTo(c2);
  	  vectorOfCommands.add(c3);
  	}
  	return ret;
  }
}

