// The UMLet source code is distributed under the terms of the GPL; see license.txt
package com.umlet.element.base;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import com.umlet.constants.Constants;
import com.umlet.control.*;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.StickingPolygon;
import com.umlet.element.base.relation.RelationLinePoint;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("serial")
public abstract class Entity extends JComponent {
  public int[] getCoordinates() {
  	int[] ret=new int[4];
  	ret[0]=getX();
  	ret[1]=getY();
  	ret[2]=getWidth();
  	ret[3]=getHeight();
  	return ret;
  }	
  
  public String getClassCode() {
    return this.getClass().getName().toUpperCase().substring(0,4);
  }

  protected Color _selectedColor=Color.blue;
  protected Color _deselectedColor=Color.black;
//A.Mueller start
  public float _zoomFactor = 1f;
  private Group group = null;
  private boolean autoresizeandmanualresizeenabled;
  
  public boolean isManualResized() {
	  autoresizeandmanualresizeenabled = true;
	  return this.isManResized();
  }
  
  private boolean isManResized() {
	  Vector<String> lines = Constants.decomposeStringsWithComments(this.getState(), Constants.newline);
	  for(String line : lines) {
		  if(line.startsWith(Constants.noautoresize))
			  return true;
	  }
	  return false;
  }
  
  public void setManualResized(boolean manual) {
	  if(autoresizeandmanualresizeenabled) {
		  if(!this.isManResized())
		  {
			  this.setState(this.getState() + Constants.newline + Constants.noautoresize);
			  if(this.equals(Umlet.getInstance().getEditedEntity()))
				  Umlet.getInstance().setPropertyPanelToEntity(this);
		  }
	  }
  }
  
  public String getAdditionalAttributes() {
	  return "";
  }
  
  public void setAdditionalAttributes(String s) {
	  
  }
  
  public Group getGroup()
  {
	  return this.group;
  }

  protected void setGroup(Group group)
  {
	  this.group = group;
  }
  
  public boolean isPartOfGroup()
  {
	  if (this.group != null) return true;
	  return false;
  }
  
  //returns true if the entity is part of group g
  //or if g = null and the entity is in no group
  //false otherwise
  public boolean isPartOfGroup(Group g)
  {
	  if(g == null) {
		  if(this.group == null)
			  return true;
		  else
			  return false;
	  }
	  else if (g.equals(this.group)) 
		  return true;
	  else
		  return false;
  }
  public void removeGroup()
  {
	  this.group = null;
  }
  public String getState()
  {
	  return _state;
  }
  
	@Override
	public boolean contains(Point p)
	{
		Area temp = new Area(this.getVisibleRect());
		Rectangle insideThis = this.getVisibleRect();
		insideThis.setSize((int)insideThis.getWidth()-2,(int)insideThis.getHeight()-2);
//		only get entites that are not in groups (other entites are represented by their groups!
		Vector<Entity> entities = this.handler.getDrawPanel().getNotInGroupEntitiesOnPanel(); 
	     for (Entity e : entities) {
	    	if(e instanceof Relation) { // a relation is always on top
	    		if(e.contains(new Point(p.x + this.getX() - e.getX(),p.y + this.getY() - e.getY())))
	    			return false;
	    	}
	    	else if (!e.equals(this)) {
	     		Rectangle toSubtract = e.getVisibleRect();
	     		Point sub_loc = toSubtract.getLocation();
	     		toSubtract.setLocation(insideThis.getLocation());	     		
				if (insideThis.contains(toSubtract))
				{
					Point loc = new Point (e.getX()-this.getX()+sub_loc.x,e.getY()-this.getY()+sub_loc.y);
					toSubtract.setLocation(loc);
					temp.subtract(new Area(toSubtract));
				}
	     	}
	     }
	     return temp.contains(new Double(p.x),new Double(p.y));
	}
	
	@Override
	public boolean contains(int x, int y)
	{
		return this.contains(new Point(x,y));
	}
	
  //A.Mueller End
  //begin B. Buckl 
  protected Color _fillColor=Color.white; 
	//end B. Buckl 
	
  protected Color _activeColor=_deselectedColor;

  public Entity CloneFromMe() {
  	try { //LME
		java.lang.Class<? extends Entity> cx= this.getClass(); //get class of dynamic object
		Entity c = cx.newInstance();
		c.setState(this.getPanelAttributes()); //copy states
		c.setBounds(this.getBounds());
		return c;
	} catch (InstantiationException e) {
		System.err.println("UMLet -> Entity/"+this.getClass().toString()+": "+e);
	} catch (IllegalAccessException e) {
		System.err.println("UMLet -> Entity/"+this.getClass().toString()+": "+e);
	}
    return null;
  }


  protected boolean _selected=false;
  public boolean isSelected() { return _selected; }
  protected String _state="";
  public String getPanelAttributes() {
    return _state;
  }
  public void setState(String s) {
    _state=s;
  }

  protected DiagramHandler handler;
  private boolean enabled;

  public Entity() {
  	this.setSize(100,100); //LME: initial size
    this.setVisible(true);
    this.enabled = true;
    this.autoresizeandmanualresizeenabled = false;
  }

  public void assignToDiagram(DiagramHandler handler) {
	  if(this.handler != null) {
		  this.removeMouseListener(this.handler.getEntityListener(this));
		  this.removeMouseMotionListener(this.handler.getEntityListener(this));
	  }
	  this.handler = handler;
	  this.addMouseListener(this.handler.getEntityListener(this));
	  this.addMouseMotionListener(this.handler.getEntityListener(this));
  }
  
  	@Override
	public void setEnabled(boolean en)
  	{
		super.setEnabled(en);
		if(!en && enabled) {
			this.removeMouseListener(this.handler.getEntityListener(this));
			this.removeMouseMotionListener(this.handler.getEntityListener(this));
			enabled=false;
		}
		else if (en && !enabled){
			if(!this.isPartOfGroup()) {
				this.addMouseListener(this.handler.getEntityListener(this));
				this.addMouseMotionListener(this.handler.getEntityListener(this));
			}
			enabled=true;
		}
	}

public DiagramHandler getHandler() {
	  return this.handler;
  }
  
  public void changeLocation(int diffx, int diffy) {
	  //only move entities that are not in groups (the group takes care of moving it)
	  if(!this.isPartOfGroup())
		  this.setLocation(this.getX()+diffx, this.getY()+diffy);
  }
  
  protected void changeLocation(int diffx, int diffy, boolean fromgroup) {
	  if(fromgroup)
		  this.setLocation(this.getX()+diffx, this.getY()+diffy);
  }
 
  public void changeSize(int diffx, int diffy) {
	  // Allow resizing only if not part of a group!
	  if (!isPartOfGroup()) this.setSize(this.getWidth()+diffx, this.getHeight()+diffy);
  }
  
  public int getResizeArea(int x, int y) {
    int ret=0;
    if(x <= 5 && x >= 0)
    	ret = Constants.RESIZE_LEFT;
    else if (x <= this.getWidth() && x >= this.getWidth()-5)
    	ret = Constants.RESIZE_RIGHT;
    
    if(y <= 5 && y >= 0)
    	ret = ret | Constants.RESIZE_TOP;
    else if (y <= this.getHeight() && y >= this.getHeight()-5)
    	ret = ret | Constants.RESIZE_BOTTOM;
    return ret;
  }

  public int getPossibleResizeDirections() { //LME
    return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
  }
  
  
  public void onSelected() {
    _selected=true;
    _activeColor=_selectedColor;
    this.repaint();
  }
  public void onDeselected() {
    _selected=false;
    _activeColor=_deselectedColor;
    this.repaint();
  }


  public StickingPolygon getStickingBorder() { //LME: define the polygon on which relations stick on
    StickingPolygon p = new StickingPolygon();
    p.addLine(new Point(0,0), new Point(this.getWidth()-1,0));
    p.addLine(new Point(this.getWidth()-1,0), new Point(this.getWidth()-1,this.getHeight()-1));
    p.addLine(new Point(this.getWidth()-1,this.getHeight()-1), new Point(0,this.getHeight()-1));
    p.addLine(new Point(0,this.getHeight()-1), new Point(0,0));
    return p;
  }
  
  public int doesCoordinateAppearToBeConnectedToMe(Point p) {
  	//LME: switched to sticking polygon
  	StickingPolygon sPoly = this.getStickingBorder();
  	if(sPoly != null) {
	  	Point[][] lines = sPoly.getLines();
	  	int aX=this.getX(), aY=this.getY(); //absolute xy coords
	  	Point p1,p2;
	  	for(int i=0;i<lines.length;i++) {
	  		p1=new Point(lines[i][0].x+aX,lines[i][0].y+aY);
	  		p2=new Point(lines[i][1].x+aX,lines[i][1].y+aY);
	  		if(isOnLine(p1,p2,p)) return sPoly.getStickingInfo(new Point(lines[i][0].x,lines[i][0].y),new Point(lines[i][1].x,lines[i][1].y));
	  	}
  	}
  	return 0;
  }
  
  public void drawStickingPolygon(Graphics2D g2) { //LME: draw the sticking polygon
	StickingPolygon poly = this.getStickingBorder();
  	if(poly != null) {
	  	Point [][] lines = poly.getLines();
	  	Color c = g2.getColor();
	  	Stroke s = g2.getStroke();
		g2.setColor(this._selectedColor);
		g2.setStroke(Constants.getStroke(1,1));
	  	for(int i=0;i<lines.length;i++) {
	  		g2.drawLine(lines[i][0].x,lines[i][0].y,lines[i][1].x,lines[i][1].y);
	  	}
	  	g2.setColor(c);
	  	g2.setStroke(s);
  	}
  }
  
  public boolean isOnLine(Point p1, Point p2, Point p) { //LME: is p on the line(p1,p2) (+/-5)
  	Line2D line=new Line2D.Double(p1,p2);
    double d=line.ptLineDist(p); //calculate distance 
  	Polygon poly1 = new Polygon(); //create 2 rectangles with their size +/-4 pixels
  	poly1.addPoint(p1.x-4,p2.y+4);
  	poly1.addPoint(p2.x+4,p2.y+4);
  	poly1.addPoint(p2.x+4,p1.y-4);
  	poly1.addPoint(p1.x-4,p1.y-4);
  	Polygon poly2 = new Polygon();
  	poly2.addPoint(p1.x+4,p2.y-4);
  	poly2.addPoint(p2.x-4,p2.y-4);
  	poly2.addPoint(p2.x-4,p1.y+4);
  	poly2.addPoint(p1.x+4,p1.y+4);
  	return (d<5)&&((poly1.contains(p)||(poly2.contains(p)))); //inside maximum distance AND inside one of the rectangles
  }
   
  public Vector<RelationLinePoint> getAffectedRelationLinePoints(int action) {
  	Vector<RelationLinePoint> ret=new Vector<RelationLinePoint>();
  	Vector<Relation> allRelations=this.handler.getDrawPanel().getAllRelationsOnPanel();
  	for (int i=0; i<allRelations.size(); i++) {
  	  Relation r=allRelations.elementAt(i);
  	  int tmpInt=r.getConnectedLinePoint(this, action);
      //if (tmpInt>0){
      //L.Trescher start
  	  if (tmpInt>0 && (!r.isPartOfGroup())){
  	  //L.Trescher end
	    int first=tmpInt%1000-1;
	    if (first>-1) {
	      ret.add(new RelationLinePoint(r,first));
	    }
	    int last=tmpInt/1000-1;
	    if (last>-1) {
	      ret.add(new RelationLinePoint(r,last));
        }
  	  }
  	}
  	return ret;
  }
  
  public boolean isInRange(Point upperLeft, Dimension size) {
  	  Rectangle2D rect1=new Rectangle2D.Double(upperLeft.getX(), upperLeft.getY(), size.getWidth(), size.getHeight());
	  Rectangle2D rect2=new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	  return (rect1.intersects(rect2));
  }
  
  public void setInProgress(Graphics g, boolean flag) {
  	if(flag) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(this.handler.getFont());
		g2.setColor(Color.red);
		this.handler.getFRC(g2);
		this.handler.writeText(g2,"in progress...",this.getWidth()/2-40,this.getHeight()/2+this.handler.getFontsize()/2, false);
  	} else {
  		repaint();
  	}
  }
  
  private String bgColor="", fgColor="";
  public String getFGColorString() {return fgColor;}
  public String getBGColorString() {return bgColor;}
  
  public boolean supportsColors=false;  
  public Composite[] colorize(Graphics2D g2) {
	  supportsColors=true;
	  bgColor="";
	  fgColor="";
	  Vector<String> v = Constants.decomposeStringsWithComments(_state, Constants.newline);
	  for(int i=0;i<v.size();i++) {
		  String line=v.get(i);
		  if(line.indexOf("bg=")>=0) {
			  bgColor=line.substring("bg=".length());
		  } else if(line.indexOf("fg=")>=0) {
			  fgColor=line.substring("fg=".length());
		  }
	  }
	  if(fgColor.equals("red")) _deselectedColor=Color.RED;
	  else if(fgColor.equals("green")) _deselectedColor=Color.GREEN;
	  else if(fgColor.equals("blue")) _deselectedColor=Color.BLUE;
	  else if(fgColor.equals("yellow")) _deselectedColor=Color.YELLOW;
	  else if(fgColor.equals("white")) _deselectedColor=Color.WHITE;
	  else if(fgColor.equals("black")) _deselectedColor=Color.BLACK;
	  else if(fgColor.equals("gray")) _deselectedColor=Color.GRAY;
	  else if(fgColor.equals("orange")) _deselectedColor=Color.ORANGE;
	  else if(fgColor.equals("magenta")) _deselectedColor=Color.MAGENTA;
	  else if(fgColor.equals("pink")) _deselectedColor=Color.PINK;
	  else {
		  try {
			  _deselectedColor=Color.decode(fgColor);
		  } catch(NumberFormatException nfe) {
			  _deselectedColor=Color.BLACK;
		  }
	  }
	  
	  if(bgColor.equals("red")) _fillColor=Color.RED;
	  else if(bgColor.equals("green")) _fillColor=Color.GREEN;
	  else if(bgColor.equals("blue")) _fillColor=Color.BLUE;
	  else if(bgColor.equals("yellow")) _fillColor=Color.YELLOW;
	  else if(bgColor.equals("white")) _fillColor=Color.WHITE;
	  else if(bgColor.equals("black")) _fillColor=Color.BLACK;
	  else if(bgColor.equals("gray")) _fillColor=Color.GRAY;
	  else if(bgColor.equals("orange")) _fillColor=Color.ORANGE;
	  else if(bgColor.equals("magenta")) _fillColor=Color.MAGENTA;
	  else if(bgColor.equals("pink")) _fillColor=Color.PINK;
	  else {
		  try {
			  _fillColor=Color.decode(bgColor);
		  } catch(NumberFormatException nfe) {
			  _fillColor=Color.WHITE;
		  }
	  }
	  
	  float alphaFactor = 0.5f;
	  if(bgColor.equals("") || bgColor.equals("default")) alphaFactor=0.0f;
	  
	  Composite old = g2.getComposite();
	  AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactor);
	  Composite composites[] = {old, alpha};
	  return composites;
  }
  
  public void setColor(String colorString, boolean isForegroundColor) {
	  String new_state = "";
	  Vector<String> textlines = Constants.decomposeStringsWithComments(this.getPanelAttributes(), "\n");
	  String prefix = (isForegroundColor?"fg=":"bg=");
	  for(int i=0;i<textlines.size();i++) {
		  if(!textlines.get(i).startsWith(prefix))
			  new_state+=textlines.get(i)+"\n";
	  }
	  if(!colorString.equals("default")) new_state +=prefix+colorString;
	  this.setState(new_state);
	  this.getHandler().getDrawPanel().getSelector().updateSelectorInformation(); //update the property panel to display changed attributes
	  this.repaint();
  }

  public final void paint(Graphics g)
  {
	  Graphics2D g2 = (Graphics2D)g;
	  if(this._selected && Constants.show_stickingpolygon && !this.isPartOfGroup())
		  this.drawStickingPolygon(g2);
	  this.paintEntity(g2);
  }
  
  public abstract void paintEntity(Graphics g);
}
