package com.umlet.control;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.umlet.element.base.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Selector {
  private Entity _ssiEntity;
  private int _ssiX=-99999;
  private int _ssiY=-99999;
  public void setSingleSelectorInformation(Entity e, int x, int y) {
    _ssiEntity=e;
    _ssiX=x;
    _ssiY=y;
  }
  public boolean hasSameSelectionPattern(Entity e, int x, int y) {
    return (_selectedEntities.contains(e) && (_selectedEntities.size()==1) && (e==_ssiEntity) && (x==_ssiX) && (y==_ssiY));
  }





  private Vector<Entity> _selectedEntities=new Vector<Entity>();
  public Vector<Entity> getSelectedEntities() {
    return _selectedEntities;
  }
  public Vector<Entity> getSelectedEntitiesOnPanel() {
    Vector<Entity> ret =new Vector<Entity>();
    for (int i=0; i<getSelectedEntities().size(); i++) {
      Entity e=(Entity)getSelectedEntities().elementAt(i);
      if (e.getParent()==Umlet.getInstance().getPanel()) ret.add(e);
    }
    return ret;
  }
  //[UB]: we assume panel.getComponents only returns Entities
  //A.Mueller start
  //bugfix: @UB it does not return only Entities
  public Vector<Entity> getAllEntitiesOnPanel(JPanel panel) {
		Component[] tmp=panel.getComponents();
		Vector<Entity> ret=new Vector<Entity>();
		 for (int i=0; i<tmp.length; i++) {
		   if (tmp[i] instanceof Entity) ret.add((Entity)tmp[i]);
		 }
		return ret;  	
	  }
	  public Vector<Entity> getAllEntitiesOnPanel() {
	    return getAllEntitiesOnPanel(Umlet.getInstance().getPanel());
	  }
  /*<OLDCODE>
  public Vector<Entity> getAllEntitiesOnPanel(JPanel panel) {
		Component[] tmp=panel.getComponents();
		Vector<Entity> ret=new Vector<Entity>();
		 for (int i=0; i<tmp.length; i++) {
		   ret.add((Entity)tmp[i]);
		 }
		return ret;  	
	  }
	  public Vector getAllEntitiesOnPanel() {
	    return getAllEntitiesOnPanel(Umlet.getInstance().getPanel());
	  }
	  </OLDCODE>*/
  //A.Mueller end
  public Vector<Relation> getAllRelationsOnPanel() {
    Component[] tmp=Umlet.getInstance().getPanel().getComponents();
    Vector<Relation> ret=new Vector<Relation>();
    for (int i=0; i<tmp.length; i++) {
      if (tmp[i] instanceof Relation)
        ret.add((Relation)tmp[i]);
    }
    return ret;
  }

  private static Selector _instance;
  public static Selector getInstance() {
    if (_instance==null) _instance = new Selector();
    return _instance;
  }

  private Selector() {
  }

  void deselectAll() {
    for (int i=0;i<_selectedEntities.size();i++) {
      Entity e=_selectedEntities.elementAt(i);
      e.onDeselected();
    }

    _selectedEntities=new Vector<Entity>();
    updatePropertyPanel();
  }

  void selectXXX(Entity e) {
    if (_selectedEntities.contains(e)) {
      return;
    }
    _selectedEntities.add(e);

    e.onSelected();
    updatePropertyPanel();
  }

/*  void selectOrDeselect(Entity e) {
    if (_selectedEntities.contains(e)) {
      _selectedEntities.removeElement(e);
      e.onDeselected();
    } else {
      _selectedEntities.add(e);
      e.onSelected();
    }
    updatePropertyPanel();
  }*/

  void deselect(Entity e) {
    if (_selectedEntities.contains(e)) {
      _selectedEntities.removeElement(e);
      e.onDeselected();
      updatePropertyPanel();
    }
  }


  void updatePropertyPanel() {
    if (_selectedEntities.size()==1) {
      Umlet.getInstance().setPropertyPanelToEntity(_selectedEntities.elementAt(0));
      Umlet.getInstance().setSourceCodePanelToEntity((Entity)_selectedEntities.elementAt(0)); //LME: update code panel too
    } else {
    	Umlet.getInstance().setPropertyPanelToEntity(null);
    	Umlet.getInstance().setSourceCodePanelToEntity(null); //LME: clear code panel
    }
  }


  public void singleSelect(Entity e, boolean singleSelect) {
    boolean hasBeenSelected=false;
    if (_selectedEntities.contains(e)) {
      hasBeenSelected=true;
    }

    if (singleSelect) deselectAll();

    _selectedEntities.add(e);
    e.onSelected();
    updatePropertyPanel();
  }
  public void multiSelect(Container panel, Point upperLeft, Dimension size) {
     Vector entities=getAllEntitiesOnPanel((JPanel) panel);
     for (int i=0; i<entities.size(); i++) {
     	Entity e =(Entity) entities.get(i);
     	//A.Mueller start
     	if (e.isInRange(upperLeft, size) && !e.isSelected()) {
     	//<OLDCODE>if (e.isInRange(upperLeft, size)) { </OLDCODE>
     	//A.Mueller end
			singleSelect(e, false);
     	}
     }
  }
}
