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

public class Resize extends Command {
  int _where, _diffx, _diffy;
  public int getX() { return _diffx; }
  public int getY() { return _diffy; }
  public int getWhere() {
    return _where;
  }
  Entity _entity;
  public Entity getEntity() {
    return _entity;
  }
  public Resize(Entity entity, int where, int diffx, int diffy) {
    _entity=entity;
    _where=where;
    _diffx=diffx;
    _diffy=diffy;
  }
  public void execute() {
    Entity e=_entity;
    if (_where==1) {
      e.changeLocation(0,_diffy);
      e.changeSize(0,-_diffy);
    } else if (_where==3) {
      e.changeLocation(0,_diffy);
      e.changeSize(_diffx,-_diffy);
    } else if (_where==9) {
      e.changeLocation(_diffx,_diffy);
      e.changeSize(-_diffx,-_diffy);
    } else if (_where==8) {
      e.changeLocation(_diffx,0);
      e.changeSize(-_diffx,0);
    } else if (_where==12) {
      e.changeLocation(_diffx,0);
      e.changeSize(-_diffx,_diffy);
    } else if (_where==2) {
      e.changeSize(_diffx,0);
    } else if (_where==4) {
      e.changeSize(0,_diffy);
    } else if (_where==6) {
      e.changeSize(_diffx,_diffy);
    }
  }
  public void undo() {
	super.undo();
    Entity e=_entity;
    if (_where==1) {
      e.changeLocation(0,-_diffy);
      e.changeSize(0,_diffy);
    } else if (_where==3) {
      e.changeLocation(0,-_diffy);
      e.changeSize(-_diffx,_diffy);
    } else if (_where==9) {
      e.changeLocation(-_diffx,-_diffy);
      e.changeSize(_diffx,_diffy);
    } else if (_where==8) {
      e.changeLocation(-_diffx,0);
      e.changeSize(_diffx,0);
    } else if (_where==12) {
      e.changeLocation(-_diffx,0);
      e.changeSize(_diffx,-_diffy);
    } else if (_where==2) {
      e.changeSize(-_diffx,0);
    } else if (_where==4) {
      e.changeSize(0,-_diffy);
    } else if (_where==6) {
      e.changeSize(-_diffx,-_diffy);
    }
  }
  public boolean isMergeableTo(Command c) {
  	if (!(c instanceof Resize)) return false;
  	Resize r=(Resize)c;
  	if (this.getEntity()!=r.getEntity()) return false;
  	if (this.getWhere()!=r.getWhere()) return false;
  	return true;
   }
  public Command mergeTo(Command c) {
    Resize tmp=(Resize)c;
    Resize ret=new Resize(this.getEntity(), this.getWhere(), this.getX()+tmp.getX(), this.getY()+tmp.getY());
    return ret;
  }
}