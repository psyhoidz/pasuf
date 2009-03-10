package com.umlet.control;

import com.umlet.element.base.*;

class Connector {
  private Entity mConnectedToEntity;
  private Entity mConnectedEntity;

  public Connector(Entity cTo, Entity cEnt) {
    mConnectedToEntity=cTo;
    mConnectedEntity=cEnt;
  }
  public Entity getConnectedToEntity() {
    return mConnectedToEntity;
  }
  public Entity getConnectedEntity() {
    return mConnectedEntity;
  }
}