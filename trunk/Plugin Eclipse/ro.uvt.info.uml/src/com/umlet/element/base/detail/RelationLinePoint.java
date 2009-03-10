package com.umlet.element.base.detail;

import com.umlet.element.base.*;

public class RelationLinePoint {
  private Relation _relation;
  private int _linePointId;
  public Relation getRelation() { return _relation;}
  public int getLinePointId() { return _linePointId; }
  
  public RelationLinePoint(Relation r, int lp) {
    _relation=r;
    _linePointId=lp;
  }
}

