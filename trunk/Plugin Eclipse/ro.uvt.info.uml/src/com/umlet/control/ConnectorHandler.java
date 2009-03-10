package com.umlet.control;

import java.util.*;

import com.umlet.element.base.*;

class ConnectorHandler {
  private ConnectorHandler() {}
  private Vector<Connector> mConnectors=new Vector<Connector>();  

  private static ConnectorHandler instance=null;
  public static ConnectorHandler getInstance() {
    if (instance==null) instance=new ConnectorHandler();
    return instance;
  }
  public void addConnector(Entity cTo, Entity cEnt) {
    if (!existsConnector(cTo, cEnt))
      mConnectors.add(new Connector(cTo, cEnt));
  }
  public void removeConnector(Entity cTo, Entity cEnt) {
    Connector tmpConnector=getConnector(cTo, cEnt);
    if (tmpConnector==null) return;
    mConnectors.removeElement(tmpConnector);
  }
  public boolean existsConnector(Entity cTo, Entity cEnt) {
    return getConnector(cTo, cEnt)!=null;
  }
  public Connector getConnector(Entity cTo, Entity cEnt) {
    Connector ret=null;
    for (int i=0; i<mConnectors.size(); i++) {
      Connector tmpConnector=mConnectors.elementAt(i);
      if (tmpConnector.getConnectedToEntity()==cTo && tmpConnector.getConnectedEntity()==cEnt) return tmpConnector;
    }
    return ret;
  }


 
  public Vector<Connector> getConnectorsByConnectedToEntity(Entity e) {
    Vector<Connector> ret=new Vector<Connector>();
      for (int i=0; i<mConnectors.size(); i++) {
        Connector tmpConnector=mConnectors.elementAt(i);
        Entity tmpEntity=tmpConnector.getConnectedToEntity();
        if (tmpEntity==e) ret.add(tmpConnector);
      }
    return ret;
  }
}