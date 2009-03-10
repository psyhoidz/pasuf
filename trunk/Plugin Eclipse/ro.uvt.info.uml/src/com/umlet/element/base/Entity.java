package com.umlet.element.base;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import com.umlet.control.*;
import com.umlet.element.base.detail.*;
import java.io.*;





/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

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
  /*public String getStateAndHiddenState() {
    Vector tmp=new Vector();
    tmp.add(this.getState());
    tmp.add(this.getHiddenState());

    String ret=Constants.composeStrings(tmp, Constants.DELIMITER_STATE_AND_HIDDEN_STATE);
    return ret;
  }*/
  private void setStateAndHiddenState(String s) {
    Vector<String> tmp=Constants.decomposeStringsIncludingEmptyStrings(s, Constants.DELIMITER_STATE_AND_HIDDEN_STATE);
    if (tmp.size()!=2) {
      throw new RuntimeException();
    }
    String state=tmp.elementAt(0);
    String hiddenState=tmp.elementAt(1);

    this.setHiddenState(hiddenState);
    this.setState(state);
  }

  protected Color _selectedColor=Color.blue;
  protected Color _deselectedColor=Color.black;
//A.Mueller start
  public float _zoomFactor = 1f;
  private Group group = null;
  private boolean transparentSelection = false;
  
  public void zoom(float factor){
	  _zoomFactor = _zoomFactor*factor;
  }
  
  public void setGroup(Group group)
  {
	  this.group = group;
  }
  public boolean isPartOfGroup()
  {
	  if (this.group != null) return true;
	  return false;
  }
  public boolean isPartOfGroup(Group g)
  {
	  if (this.group == g) return true;
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
  public void setTransparentSelection(boolean mode)
  {
	  transparentSelection = mode;
  }
  public boolean getTransparentSelection() {
	  return transparentSelection;
  }
	@Override
	public boolean contains(Point p)
	{
		if (!transparentSelection) return super.contains(p.x,p.y);
		Area temp = new Area(this.getVisibleRect());
		Rectangle insideThis = this.getVisibleRect();
		insideThis.setSize((int)insideThis.getWidth()-2,(int)insideThis.getHeight()-2);
		Vector<Entity> entities = Selector.getInstance().getAllEntitiesOnPanel();
	     for (int i=0; i<entities.size(); i++) {
	    	 Entity e;
	     	e =entities.get(i);
	     	if (e != this) {
				if (insideThis.contains(e.getVisibleRect()))
				{
					Rectangle toSubtract = e.getVisibleRect();
					Point loc = new Point (e.getX()-this.getX(),e.getY()-this.getY());
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
		java.lang.Class cx= this.getClass(); //get class of dynamic object
		Entity c = (Entity)cx.newInstance();
		c.setState(this.getPanelAttributes()); //copy states
		c.setBounds(this.getBounds());
		//A.Mueller start
        c.setTransparentSelection(this.getTransparentSelection());
        //A.Mueller end
		c.sourceFilePath=this.sourceFilePath;
		c._javaSource = this._javaSource;
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
  public String getAdditionalAttributes() {
    /*Vector tmp=new Vector();
    tmp.add(""+this.getX());
    tmp.add(""+this.getY());
    tmp.add(""+this.getWidth());
    tmp.add(""+this.getHeight());
    String ret=Constants.composeStrings(tmp, Constants.DELIMITER_FIELDS);*/
	//A.Mueller start
    //store the Transparent selection...
    return "transparentSelection="+this.getTransparentSelection();
    /*<OLDCODE>
    return "";
    </OLDCODE*/
    //A.Mueller end
  }
  public void setAdditionalAttributes(String s) {
	  //A.Mueller start
      //transparent selection...
      if (s == null) return;
      Vector<String>tmp = Constants.decomposeStrings(s,Constants.DELIMITER_FIELDS);
      for (int i=0; i< tmp.size(); i++){
          if (tmp.get(i).startsWith("transparentSelection")){
              String[] ts = tmp.get(i).split("=");
              if (ts.length != 2) return;
              if (ts[1].equals("true")) this.setTransparentSelection(true);
              else this.setTransparentSelection(false);
          }
      }
      //A.Mueller end
  }
  public void setHiddenState(String s) {
    Vector<String> tmp=Constants.decomposeStrings(s, Constants.DELIMITER_FIELDS);
    if (tmp.size()!=4) throw new RuntimeException("UMLet: Error in Entity.setHiddenState(), state value = "+s);
    int x=Integer.parseInt(tmp.elementAt(0));
    int y=Integer.parseInt(tmp.elementAt(1));
    int w=Integer.parseInt(tmp.elementAt(2));
    int h=Integer.parseInt(tmp.elementAt(3));
    this.setBounds(x,y,w,h);
  }

  public Entity(String s) {
  	this.setSize(100,100); //LME: initial size
    this.setStateAndHiddenState(s);
    this.addMouseListener(UniversalListener.getInstance());
    this.addMouseMotionListener(UniversalListener.getInstance());
    this.setVisible(true);
  }
  public Entity() {
  	this.setSize(100,100); //LME: initial size
    this.addMouseListener(UniversalListener.getInstance());
    this.addMouseMotionListener(UniversalListener.getInstance());
    this.setVisible(true);
  }

  public void changeLocation(int diffx, int diffy) {
	  // A.Mueller start
	  if (!isPartOfGroup()) this.setLocation(this.getX()+diffx, this.getY()+diffy);
	  /*<OLDCODE>
	  this.setLocation(this.getX()+diffx, this.getY()+diffy);
	  </OLDCODE>*/
	 //A.Mueller end
  }
  public void changeSize(int diffx, int diffy) {
	  // A.Mueller start
	  if (!isPartOfGroup()) this.setSize(this.getWidth()+diffx, this.getHeight()+diffy);
	  /*<OLDCODE>
	  this.setSize(this.getWidth()+diffx, this.getHeight()+diffy);
	  </OLDCODE>*/
	 //A.Mueller end
  }
  public void zoomSize(double percent) {
    this.setSize((int)((double)this.getWidth()*percent), (int)((double)this.getHeight()*percent));
  }
  public void zoomLocation(double percent, int centerX, int centerY) {
    int newX=centerX+(int)(((double)(this.getX()-centerX))*percent);
    int newY=centerY+(int)(((double)(this.getY()-centerY))*percent);
    this.setLocation(newX, newY);
  }
  // bit 1 top, 2 right, 3 bottom, 4 left
  public int getResizeArea(int x, int y) {
    //if (this instanceof Interface) return 0; LME: moved to Interface
    int ret=0;
    if (y<5) ret=1;
    else if (this.getHeight()-y<5) ret=4;

    if (x<5) ret=ret|8;
    else if (this.getWidth()-x<5) ret=ret|2;
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
    p.addLine(new Point(0,0), new Point(this.getWidth()-1,0),Constants.RESIZE_TOP);
    p.addLine(new Point(this.getWidth()-1,0), new Point(this.getWidth()-1,this.getHeight()-1),Constants.RESIZE_RIGHT);
    p.addLine(new Point(this.getWidth()-1,this.getHeight()-1), new Point(0,this.getHeight()-1),Constants.RESIZE_BOTTOM);
    p.addLine(new Point(0,this.getHeight()-1), new Point(0,0),Constants.RESIZE_LEFT);
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
  	
  	/*int[] xP=lines.xpoints;
	int[] yP=poly.ypoints;
	int aX=this.getX(), aY=this.getY(); //absolute xy coords
	Point p1,p2;
  	
  	for(int i=0;i<poly.npoints-1;i++) {
  		p1=new Point(xP[i]+aX,yP[i]+aY);
  		p2=new Point(xP[i+1]+aX,yP[i+1]+aY);
  		if(isOnLine(p1,p2,p)) return 1;
  	}
  	p1=new Point(xP[poly.npoints-1]+aX,yP[poly.npoints-1]+aY);
  	p2=new Point(xP[0]+aX,yP[0]+aY);
  	if(isOnLine(p1,p2,p)) return 1;
  	return 0;
  	*/
  	
/*
    int ret=0;

    int tmpX=p.x-this.getX();
    int tmpY=p.y-this.getY();

    if (tmpX>-4 && tmpX<this.getWidth()+4) {
        if (tmpY>-4 && tmpY<4) ret+=1;//Constants.ELEMENT_TOP;
        if (tmpY>this.getHeight()-4 && tmpY<this.getHeight()+4) ret+=4;//Constants.ELEMENT_BOTTOM;
      }
    if (tmpY>-4 && tmpY<this.getHeight()+4) {
        if (tmpX>-4 && tmpX<4) ret+=8;//Constants.ELEMENT_LEFT;
        if (tmpX>this.getWidth()-4 && tmpX<this.getWidth()+4) ret+=2;//Constants.ELEMENT_RIGHT;
      }
    return ret;

 */
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
 
  public void drawStickingPolygon(Graphics g, StickingPolygon poly) { //LME: draw the sticking polygon
  	if(poly != null) {
	  	Point [][] lines = poly.getLines();
	  	Graphics2D g2=(Graphics2D) g; 
		g2.setColor(Color.red);
		g2.setStroke(Constants.getStroke(2,1));
	  	for(int i=0;i<lines.length;i++) {
	  		g2.drawLine(lines[i][0].x,lines[i][0].y,lines[i][1].x,lines[i][1].y);
	  	}
  	}
  }
  
  
  
  public Vector<RelationLinePoint> getAffectedRelationLinePoints(int action) {
  	Vector<RelationLinePoint> ret=new Vector<RelationLinePoint>();
  	Vector<Relation> allRelations=Selector.getInstance().getAllRelationsOnPanel();
  	for (int i=0; i<allRelations.size(); i++) {
  	  Relation r=allRelations.elementAt(i);
  	  int tmpInt=r.getConnectedLinePoint(this, action);
  	  if (tmpInt>0) {
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
  protected String sourceFilePath="";
  public String getSourceFilePath() { return sourceFilePath; }
  protected String _javaSource=""; //contains entire java source of the custom class
  public String getJavaSource() { return _javaSource; }
  public void loadJavaSource(String sourceFilePath) { //LME3
  		this.sourceFilePath=sourceFilePath;
		if((sourceFilePath!=null)&&(sourceFilePath.endsWith(".java"))) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(sourceFilePath));
				String line;
				if(_javaSource!="") _javaSource=""; //Löschen falls notwendig
				while((line=br.readLine())!=null) {
					_javaSource += line+"\r\n"; 
				}
			}
			catch(Exception e) {
				System.err.println("loadJavaSource():"+e);
				this.sourceFilePath="";
			}
		}
  }
  
  public void saveJavaSource(String sourceCode) { //LME3
  		_javaSource = sourceCode; //store new code
		//System.out.println("Save the code");
		if((sourceFilePath!=null)&&(sourceFilePath.endsWith(".java"))) {
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(sourceFilePath,false));
				bw.write(sourceCode);
				bw.flush();
				bw.close();
			} catch (IOException e) {
				System.err.println("saveJavaSource():"+e);
			}
			
  		} else System.out.println("NOT \"SAVEABLE\"");
  }
  
  public void setInProgress(Graphics g, boolean flag) {
  	if(flag) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(Color.red);
		Constants.getFRC(g2);
		Constants.write(g2,"in progress...",this.getWidth()/2-40,this.getHeight()/2+Constants.getFontsize()/2, false);
		//System.out.println("setInProgress");
  	} else {
  		//System.out.println(".");
  		repaint();
  	}
  }
}
