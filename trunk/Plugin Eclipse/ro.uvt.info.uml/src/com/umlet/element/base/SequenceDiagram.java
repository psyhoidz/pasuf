package com.umlet.element.base;

import java.awt.*;
import java.util.*;
import com.umlet.control.*;

import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


//An interaction represents a synchronous/asynchronous message
//that is sent between two objects.
class Interaction
{
    private int srcObj;
    private boolean srcObjHasControl;
    private int arrowKind; //1=SYNC, 2= ASYNC, 3=EDGE, 4=FILLED
    private int lineKind; //1=SOLID, 2=DOTTED
    private int destObj;
    private boolean destObjHasControl;
    private String methodName;


    public Interaction(int srcObj, boolean srcObjHasControl, int arrowKind, int lineKind,
		int destObj, boolean destObjHasControl, String methodName)
    {
    	this.srcObj= srcObj;
    	this.srcObjHasControl= srcObjHasControl;
    	this.arrowKind= arrowKind;
    	this.lineKind = lineKind;
    	this.destObj= destObj;
    	this.destObjHasControl= destObjHasControl;
    	this.methodName= methodName;
    }


    public boolean hasControl(int objNum) {
		if ((srcObjHasControl && srcObj== objNum) ||
		    (destObjHasControl && destObj== objNum))
		    return true;
		else return false;
    }


    public boolean equals(Object o) {
        if (!(o instanceof Interaction)) return false;
        Interaction i = (Interaction)o;

        return srcObj==i.srcObj && srcObjHasControl==i.srcObjHasControl &&
        	arrowKind==i.arrowKind && destObj==i.destObj && 
			destObjHasControl==i.destObjHasControl &&
			(methodName == null |  methodName.equals(i.methodName));
    }

    public int hashCode() {
    	return (methodName!=null?methodName.hashCode():1) + srcObj + 
	    	(srcObjHasControl?1:0) + arrowKind + destObj + (destObjHasControl?1:0);
    }

    public int getSrcObj() {return srcObj;}
    public boolean getSrcObjHasControl() {return srcObjHasControl;}
    public int getArrowKind() {return arrowKind;}
    public int getLineKind() {return lineKind;}
    public int getDestObj() {return destObj;}
    public boolean getDestObjHasControl() {return destObjHasControl; }
    public String getMethodName() {return methodName; }
}


//Contains all interactions entered by the user and
//offers various comfort-functions for finding and 
//working with interactions 
class InteractionManagement
{
    private Set<Interaction>[] level;
	//private Set[] level;

    InteractionManagement(int numLevels) {
    	level= new HashSet[numLevels];
    	for (int i=0; i < numLevels; i++) {
    		level[i]= new HashSet<Interaction>();
    	}
    }

    public boolean controlBoxExists(int levelNum, int objNum) {
    	Iterator<Interaction> it= level[levelNum-1].iterator();
		while (it.hasNext()) {
			Interaction ia= it.next();
		    if (ia.hasControl(objNum)) return true;
		}
		return false;
    }

    public void add(int numLevel, Interaction i) {
    	level[numLevel-1].add(i);
    }

    public Set<Interaction> getInteractionsInLevel(int levelNum) {
    	return level[levelNum-1];
    }

    public int getNumLevels() {
    	return level.length;
    }
}


public class SequenceDiagram extends com.umlet.element.base.Entity {

    public int controlFlowBoxWidth = 20;

    //the dimensions for the rectangle(s) (=objects) in the first line
    public final int rectDistance= 60; //distance between two columns
    public int rectHeight; //computed
    public int rectWidth; //computed
    //int extraTextSpace = 0;

    public final int borderDistance= 10; //d between the component bordeR and the diagram
    private int yOffsetforTitle=0;

    public int rectToFirstLevelDistance= 0;
    public int levelHeight= 30;//LME//60;

    //these two constants are important for the arrowhead
    public final int arrowX= 5; 
    public final int arrowY= 5;

    public final int SYNC=   1;
    public final int ASYNC=  2;
    public final int EDGE=	 3; //LME
    public final int FILLED= 4; //LME
    public final int SOLID=  1; //LME
    public final int DOTTED= 2; //LME



    private int levelNum= 0; 
    private InteractionManagement im;


    public SequenceDiagram() {
    	rectHeight= 2*Constants.getDistLineToText() + Constants.getFontsize() + 10;
    }


    public void paint(Graphics g) {
    	// Some unimportant initialization stuff; setting color, font 
    	// quality, etc. You should not have to change this.
    	Graphics2D g2=(Graphics2D) g; g2.setFont(Constants.getFont());
    	g2.setColor(_activeColor); Constants.getFRC(g2);
    	
    	//draw the border
    	g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);

    	levelNum=1;
    	
    	Vector<String> lines=Constants.decomposeStrings(this.getPanelAttributes(), "\n");
    	
    	if (lines.size() == 0) return;
    	if(lines.elementAt(0).startsWith("title:")) {
    		String title = lines.elementAt(0).substring("title:".length());
    		if(title != null && title.length()>0) {
	    		Constants.write(g2,title,10,Constants.getFontsize()+Constants.getDistLineToText(),false);
	    		int titlewidth=Constants.getPixelWidth(g2,title);
	    		int ty = 8+Constants.getFontsize()+Constants.getDistTextToLine();
	    		g2.drawLine(0,ty,titlewidth+10,ty);
	    		g2.drawLine(titlewidth+10,ty,titlewidth+ty+10,0);
	    		lines.remove(0);
	    		yOffsetforTitle=25;
    		}
    	} else yOffsetforTitle=0;
    	
    	
    	for (int i=1; i < lines.size(); i++) {
    		String element = lines.elementAt(i);
    		if(element.indexOf("iframe{")>=0) {
    			element = "9999->0:"+element; //dummy: space for interactionframe
    			lines.set(i,element);
    		} else if(element.indexOf("iframe}")>=0) {
    			element = "9999<-0:"+element; //dummy: space for interactionframe
    			lines.set(i,element);
    		}
    		if (lines.elementAt(i).matches("\\A\\s*\\z")) continue;
    		levelNum++;
    	}

    	String firstLine= lines.elementAt(0);
		Vector<String> obj= Constants.decomposeStrings(firstLine, "|");
		int numObjects= obj.size();

		//parse the messages
		int curLevel= 0;
		im= new InteractionManagement(levelNum);
		String boxStrings="";
		for (int i=1; i < lines.size(); i++) {
		    if (lines.elementAt(i).matches("\\A\\s*\\z")) continue;
		    curLevel++;
		    Vector<String> interactions= Constants.decomposeStrings(lines.elementAt(i), ";");
	
		    for (int j=0; j < interactions.size(); j++) {
		    	Pattern p= 
		    		//Pattern.compile("\\A(\\d+)(->>|->|-/>|.>>|.>|./>|->>>|.>>>)(\\d+)(:((\\d+)(,(\\d+))*))*(?::(.*))?\\z");
		    		Pattern.compile("\\A(\\d+)(->>|->|-/>|.>>|.>|./>|->>>|.>>>|<<-|<-|</-|<<.|<.|</.|<<<-|<<<.)(\\d+)(:((\\d+)(,(\\d+))*))*(?::(.*))?\\z");
		    		//1->2:1,2:methodName
		    		//1->2:abc
		    	Matcher m= p.matcher(interactions.elementAt(j));
		    	if (!m.matches()) continue;
		    	int srcObj= Integer.parseInt(m.group(1));
		    	
		    	int arrowKind= -1;
		    	int lineKind= -1;
		    	boolean reverse=false; //arrow direction flag
		    	if (m.group(2).equals("->")) 	    {arrowKind= ASYNC; lineKind=SOLID;}
		    	else if (m.group(2).equals("->>"))  {arrowKind= SYNC; lineKind=SOLID;}
		    	else if (m.group(2).equals("-/>"))  {arrowKind= EDGE; lineKind=SOLID;} //LME
		    	else if (m.group(2).equals("->>>")) {arrowKind= FILLED; lineKind=SOLID;} //LME
		    	else if (m.group(2).equals(".>"))   {arrowKind= ASYNC; lineKind=DOTTED;} //LME
		    	else if (m.group(2).equals(".>>"))  {arrowKind= SYNC; lineKind=DOTTED;} //LME
		    	else if (m.group(2).equals("./>"))  {arrowKind= EDGE; lineKind=DOTTED;} //LME
		    	else if (m.group(2).equals(".>>>")) {arrowKind= FILLED; lineKind=DOTTED;} //LME
		    	else if (m.group(2).equals("<-")) 	{arrowKind= ASYNC; lineKind=SOLID; reverse=true;}
		    	else if (m.group(2).equals("<<-"))  {arrowKind= SYNC; lineKind=SOLID; reverse=true;}
		    	else if (m.group(2).equals("</-"))  {arrowKind= EDGE; lineKind=SOLID; reverse=true;} //LME
		    	else if (m.group(2).equals("<<<-")) {arrowKind= FILLED; lineKind=SOLID; reverse=true;} //LME
		    	else if (m.group(2).equals("<."))   {arrowKind= ASYNC; lineKind=DOTTED; reverse=true;} //LME
		    	else if (m.group(2).equals("<<."))  {arrowKind= SYNC; lineKind=DOTTED; reverse=true;} //LME
		    	else if (m.group(2).equals("</."))  {arrowKind= EDGE; lineKind=DOTTED; reverse=true;} //LME
		    	else if (m.group(2).equals("<<<.")) {arrowKind= FILLED; lineKind=DOTTED; reverse=true;} //LME
	
		    	int destObj= Integer.parseInt(m.group(3));
		    	String group=m.group(5);
		    	if(group==null) {
		    		group="#";
		    		String element = interactions.elementAt(j);
		    		if(element.indexOf("iframe")>=0) group+=element.substring(element.indexOf("iframe")); //append info for interactionframe
		    	}
		    	boxStrings += ";"+group; //LME: get alive Objects
		    	
		    	boolean srcObjHasControl= group.contains(String.valueOf(srcObj));
		    	boolean destObjHasControl= group.contains(String.valueOf(destObj));
		    	String methodName= m.group(9);
		    	
		    	//LME: removed (in V6) since not necessary
		    	//if(destObj==srcObj) levelNum++; //LME: expand the Entity's size

		    	if ((srcObj <= 0) || (destObj<= 0) || (srcObj > numObjects) || (destObj > numObjects)) continue;
	
		    	if(!reverse) im.add(curLevel, new Interaction(srcObj,srcObjHasControl, arrowKind, lineKind, destObj, destObjHasControl, methodName)); //normal arrow direction  1->2
		    	else 		 im.add(curLevel, new Interaction(destObj, destObjHasControl, arrowKind, lineKind, srcObj,srcObjHasControl, methodName)); //reverse arrow direction 1<-2
		    } //#for
		}	
		//end message parsing

		

		//find out the width of the column with the longest text
		double maxWidth= 0;
		for (int i=0; i < numObjects; i++) {
		    String s= obj.elementAt(i);
		    if (s.startsWith("_") && s.endsWith("_") && s.length()>2) {
		    	s=s.substring(1,s.length()-1);
		    }
		    TextLayout layout= new TextLayout(s, Constants.getFont(), g2.getFontRenderContext());
		    maxWidth= Math.max(layout.getBounds().getWidth(), maxWidth);
		}

		rectWidth= (int)Math.floor(maxWidth+1)+ 2* Constants.getDistLineToText() + Constants.getFontsize();

		//draw the first line of the sequence diagram
		int ypos= borderDistance+yOffsetforTitle;
		int xpos= borderDistance;
		for (int i=0; i < numObjects; i++) {
		    boolean underline= false;
		    String s= obj.elementAt(i);
		    if (s.startsWith("_") && s.endsWith("_") && s.length()>2) {
		    	underline=true;
		    	s=s.substring(1,s.length()-1);
		    }
		    TextLayout layout= new TextLayout(s, Constants.getFont(), 
						      g2.getFontRenderContext());
	
		    g2.drawRect(xpos, ypos, rectWidth-1, rectHeight-1);
	
		    int dx= (rectWidth-2 - (int)Math.floor(layout.getBounds().getWidth()+1)) / 2;
		    int dy= (rectHeight-2 - (int)Math.floor(layout.getBounds().getHeight()+1)) / 2;
		    int tx= xpos + dx;
		    int ty= ypos + dy + (int)layout.getBounds().getHeight();
	
		    layout.draw(g2, tx, ty);
	
		    if (underline)
				g2.drawLine(tx, 
					    ty+Constants.getDistTextToLine()/2, 
					    tx+(int)layout.getBounds().getWidth(), 
					    ty+Constants.getDistTextToLine()/2);
	
		    xpos += rectWidth + rectDistance;
		}

		//draw the messages
		int maxTextXpos = drawMessages(g2);
		maxTextXpos+=3*Constants.getDistTextToLine(); //add extra space
		if(boxStrings!=null && boxStrings.length()>1) {
			try {
				drawControlFlowBoxesWithLines(g2, boxStrings.substring(1),numObjects); //LME: 1,2;1,2;... cut first ;-character
			} catch(ArrayIndexOutOfBoundsException e) {
				//do nothing: this exception is thrown, when entering text, 
				//that is not rendered to an control flow box
			}
		}

		//set our component to the correct size
		int rWidth = rectWidth * numObjects + rectDistance * (numObjects-1) + 2 * borderDistance;
		int rHeight = 2 * borderDistance + yOffsetforTitle + rectHeight + rectToFirstLevelDistance + levelNum * levelHeight;
		rWidth=(rWidth>maxTextXpos?rWidth:maxTextXpos);
		//align the borders to the grid
		rWidth  += Umlet.getInstance().getMainUnit()-rWidth%Umlet.getInstance().getMainUnit();
		rHeight += Umlet.getInstance().getMainUnit()-rHeight%Umlet.getInstance().getMainUnit();
		setSize(rWidth, rHeight);
    }


    private int drawMessages(Graphics2D g2) {
    	int maxTextXpos = 0;
    	for (int i=0; i < im.getNumLevels(); i++) {
    		Set<Interaction> interactions= im.getInteractionsInLevel(i+1);
    		Iterator<Interaction> it= interactions.iterator();
    		while (it.hasNext()) {
    			Interaction ia= it.next();
    			if (ia.getSrcObj() == ia.getDestObj()) {
    				//draw an arc-arrow
    				int xTextOffset=0;
    				int w= 30;
    				int h= (int)(levelHeight * 0.66);
    				int x= hCenterForObj(ia.getSrcObj()) - w/2;
    				//nt y= vCenterForLevel(i+1) - h/2;
    				int ay= vCenterForLevel(i+1)+5; //+ levelHeight/2 -1;
		    	    if (im.controlBoxExists(i+1, ia.getSrcObj())) {
		    	    	x += controlFlowBoxWidth/2;
		    	    	xTextOffset=controlFlowBoxWidth/2;
		    	    }
		    	    g2.drawArc(x,ay,w,h, 90,-180);
		    	    Point p1= new Point(x+w/2, ay+h);
		    	    Point d1= new Point(x+w/2 + 3, p1.y -6);
		    	    Point d2= new Point(x+w/2+4, p1.y +4);

		    	    if (ia.getArrowKind() == ASYNC) { //Pfeil offen
		    	    	g2.drawLine(p1.x,p1.y, d1.x, d1.y);
		    	    	g2.drawLine(p1.x,p1.y, d2.x,d2.y);
		    	    } else if(ia.getArrowKind() == SYNC){
		    	    	int xs[] = {p1.x, d1.x, d2.x};
		    	    	int ys[] = {p1.y, d1.y, d2.y};
		    	    	Color oldColor= g2.getColor();
		    	    	g2.setColor(_fillColor);
		    	    	g2.fillPolygon(xs,ys,3);
		    	    	g2.setColor(oldColor);
		    	    	g2.drawPolygon(xs,ys,3);
		    	    } else if(ia.getArrowKind() == EDGE) {
		    	    	g2.drawLine(p1.x,p1.y, d2.x,d2.y);
		    	    } else if(ia.getArrowKind()==FILLED) {
		    	    	Polygon p = new Polygon();
		    			p.addPoint(p1.x, p1.y);
		    			p.addPoint(d1.x, d1.y);
		    			p.addPoint(d2.x, d2.y);
		    			g2.fillPolygon(p);
		    	    }

		    	    //print the methodname
		    	    if (ia.getMethodName() != null && !ia.getMethodName().equals("")){
		    	    	int fx1= x +w +2;
		    	    	int fy1= ay;
		    	    	int fx2= hCenterForObj(ia.getSrcObj()) + rectWidth/2;
		    	    	int fy2= ay + h;
		    	    	int tx = printMethodName(g2, ia.getMethodName(), fx1+xTextOffset,fx2+xTextOffset,
		    	    			fy1, fy2, true, false);
		    	    	maxTextXpos=maxTextXpos>tx?maxTextXpos:tx;
		    	    }

    			} else {
    				//draw an arrow from the source-object to the destination object
    				int begX= hCenterForObj(ia.getSrcObj());
    				int endX= (ia.getSrcObj() < ia.getDestObj())? hCenterForObj(ia.getDestObj())-1: hCenterForObj(ia.getDestObj())+1;
    				int arrowY= vCenterForLevel(i+1) + levelHeight/2 -1;

    				if (ia.getSrcObjHasControl()) { //LME: shrink arrow if box exists	
    					begX += (ia.getSrcObj() < ia.getDestObj()) ? (controlFlowBoxWidth/2) : (-controlFlowBoxWidth/2);
    				}
    				if(ia.getDestObjHasControl()) { //LME: shrink arrow if box exists
    					endX += (ia.getSrcObj() < ia.getDestObj()) ? (-controlFlowBoxWidth/2) : (controlFlowBoxWidth/2);
    				}

    				drawArrow(g2, new Point(begX,arrowY), new Point(endX, arrowY), ia.getArrowKind(), ia.getLineKind());

    				if (ia.getMethodName() != null && !ia.getMethodName().equals("")) {
    					final int b= 2;
    					if (ia.getSrcObj() < ia.getDestObj()) { 
    						int tx = printMethodName(g2, ia.getMethodName(), begX+b, endX-arrowX-b, arrowY-1-levelHeight/1, arrowY-1, false,true);
    						maxTextXpos=maxTextXpos>tx?maxTextXpos:tx;
    					} else {
    						int tx = printMethodName(g2, ia.getMethodName(), endX+arrowX+b, begX-b, arrowY-1 - levelHeight/2, arrowY-1, false,true);
    						maxTextXpos=maxTextXpos>tx?maxTextXpos:tx;
    					}
    				}
    			}
    		}
    	}
    	return maxTextXpos;
    }

    //prints the given methodName in an intelligent manner into
    //the supplied rectangle. 
    //The method may put pixels anywhere into the supplied rectangle
    //including the borders. (In other words eg. the point begX/endY
    //may be set by this method.)
    private int printMethodName(Graphics2D g2, String methodName, 
				 int begX, int endX, int begY, int endY,
				 boolean centerVertically, boolean centerHorizontically) {

    	if (methodName == null || methodName.equals("")) {
    		System.err.println("SequenceDiagram->printMethodName was called with an invalid argument.");
    		return 0;
    	}

    	Font font= Constants.getFont();
    	String displayMethodName= methodName;

    	TextLayout layout= new TextLayout(displayMethodName, font, g2.getFontRenderContext());
	
    	if ((int)layout.getBounds().getHeight() > endY-begY) {
    		//TODO: increase levelHeight and paint it again
    		System.err.println("Error in SequenceDiagram->printMethodName: "+
			       "Cannot print the methodName because there is "+
			       "not enough vertical space. Maybe the font size "+
			       "is too big.");
    		return 0;
    	}

    	/*if (layout.getBounds().getWidth() > endX-begX) {
    		while (methodName.length() > 0 && layout.getBounds().getWidth() > endX-begX) {
    			methodName= methodName.substring(0, methodName.length()-1);
    			displayMethodName= methodName + "...";
    			layout= new TextLayout(displayMethodName, font,
				       g2.getFontRenderContext());
    		}
    	}*/    	
    	
    	//draw it horizontally centered
    	int dx=endX-begX-20+Constants.getDistLineToText();
    	if(centerHorizontically) dx= (endX-begX - (int)layout.getBounds().getWidth()) / 2;
    	int dy= (centerVertically)? 
    			(endY-begY - (int)layout.getBounds().getHeight())/2 : 1;
    	
    	
    	layout.draw(g2, begX + dx, endY - dy );
/* Expand Entity bounds if text exceeds width     	
    	System.out.println(dx+Constants.getFontsize()*displayMethodName.length());
    	int eX = Umlet.getInstance().getEditedEntity().getX();
    	int eW = Umlet.getInstance().getEditedEntity().getWidth();
    	Rectangle2D r2d=layout.getBounds();
    	int rTextPos = (int)r2d.getWidth() + (int)r2d.getX()+ begX+dx;
*/    	
    	return begX+dx+(int)layout.getBounds().getWidth(); //LMEXX
    }

    
    public void drawControlFlowBoxesWithLines(Graphics2D g2, String s, int numObjects) { //LME
    	int level=1;
    	StringTokenizer mainTokens = new StringTokenizer(s,";");
    	
    	int tokNum = mainTokens.countTokens();
    	int[][] tField = new int[numObjects][tokNum+2];

    	Vector<Integer> interactionframes = new Vector<Integer>();
    	HashMap<String,String> interactionframesText = new HashMap<String,String>();
    	
    	//collect all tokens into an array: create another view to sequentially access data of a specific object
    	//1,2;#;1,3;1,3;1;3 will be transfomed to
    	//	tField:	[110]
    	//			[000]
    	//			[101]
    	//			[101]
    	//			[100]
    	//			[001]
    	while(mainTokens.hasMoreTokens()) {
    		String main = mainTokens.nextToken();
    		if(main.indexOf("#")>=0) { //if no box, clear entire row
    			for(int i=0;i<numObjects;i++) tField[i][level-1]=0; //clear
    			if(main.indexOf("#iframe{")>=0 || main.indexOf("#iframe}")>=0) {
    				if(main.indexOf("#iframe{")>=0) interactionframes.add(new Integer(level));
    				else interactionframes.add(new Integer(level*(-1))); //distinguish betweeen start and end of the iframe
    				if(main.indexOf("iframe{:")>=0) {
    					interactionframesText.put(""+level,main.substring(main.indexOf("iframe{:")+8)); //put text into hashmap
    				}
    			}
    			level++;
    		} else {
    			StringTokenizer innerT = new StringTokenizer(main,",");
    			for(int i=0;i<numObjects;i++) tField[i][level-1]=0; //clear
    			while(innerT.hasMoreTokens()) {
    				String is = innerT.nextToken();
    				int objNum = Integer.parseInt(is);
    				tField[objNum-1][level-1]=1;
    			}
    			level++;
    		} //#else
        } //#while

    	
    	for(int actObjNum=0;actObjNum<numObjects;actObjNum++) {
	    	int offset=2;
	    	int objNum=actObjNum+1;
	    	int x1 = hCenterForObj(objNum) - controlFlowBoxWidth/2; 
	    	int startLevel=-1,boxSize=0;
	    	
	    	int lineX= hCenterForObj(actObjNum+1);
	    	int lineY1= borderDistance + yOffsetforTitle + rectHeight;

	    	for(int i=0;i<tokNum+1;i++) {
	    		if(tField[actObjNum][i]==1) {
	    			if(startLevel==-1) startLevel=i;
	    			boxSize++;
	    		}
	    		if((tField[actObjNum][i]==0)&&(startLevel!=-1)) {
	    			int y1 = vCenterForLevel(startLevel+offset) - levelHeight-1;
	    	    	g2.drawRect(x1,y1,controlFlowBoxWidth-1, levelHeight*boxSize); //draw the box
	    	    	
	    	    	g2.setStroke(Constants.getStroke(1,1));	//#draw the line between the boxes
	    		    g2.drawLine(lineX, lineY1, lineX, y1);	//#
	    		    g2.setStroke(Constants.getStroke(0,1));	//#
	    		    lineY1=y1+levelHeight*boxSize;			//#
	    	    	startLevel=-1; boxSize=0;	    	    	
	    		}
	    	} //#for(int i
	    	//LME: draw the tail
	    	int lineY2=borderDistance+yOffsetforTitle+rectHeight + levelNum* levelHeight + rectToFirstLevelDistance;
	    	g2.setStroke(Constants.getStroke(1,1));
		    g2.drawLine(lineX, lineY1, lineX,lineY2 );
		    g2.setStroke(Constants.getStroke(0,1));
    	} //#for(int actObjNum	
    	
    	//LME: draw the interaction frames
    	int fullSets = interactionframes.size()-(interactionframes.size()%2);
    	if(fullSets>=2) {
    		int pos=0;
    		while(pos<fullSets) {
    			pos=recurseInteractionFrames(g2,interactionframes,interactionframesText,pos,0);
    			pos++;
    		}
    	}
    }

    /**
     * Recusion level step of interactionframes
     * ie:
     * iframe{
     *   iframe{
     *   }
     *   iframe{
     *   }
     * } 
     */
    public int recurseInteractionFrames(Graphics2D g2, Vector<Integer>interactionframes, HashMap<String,String> interactionframesText, int pos, int recursionLevel) {
    	int pos1=interactionframes.elementAt(pos);
    	int posX;
    	while((pos<interactionframes.size())&&(posX=interactionframes.elementAt(pos))>0) { //traverse through iframes an same level
    		pos1=posX;
    		pos++;
    		pos = recurseInteractionFrames(g2, interactionframes, interactionframesText, pos, recursionLevel+1); //step on level deeper into recursion
	    	if(pos1<=0) return pos;
	    	int pos2 = interactionframes.elementAt(pos) * (-1);
	    	drawInteractionFrame(g2, pos1, pos2, recursionLevel, interactionframesText.get(""+pos1));
	    	pos++;
    	}
    	return pos;
    }
    
    
    
    private void drawInteractionFrame(Graphics2D g2, int pos1, int pos2, int recursionLevel, String text) {
    	int pos11 = (pos1+1)*levelHeight+ yOffsetforTitle;
		int h = (pos2-pos1)*levelHeight;
		int x=Constants.getDistTextToText()*2+recursionLevel*4;
		g2.drawRect(x,pos11,this.getWidth()-Constants.getDistTextToText()*4-1-recursionLevel*8,h);
		int uLinePos = pos11+Constants.getDistLineToText()+Constants.getFontsize()+Constants.getDistTextToLine();
		int textPos = pos11+Constants.getDistLineToText()+Constants.getFontsize();
		
		int textWidth = 0;
		if(text==null||text.equals("")) text=" ";
		g2.drawString(text,x+10,textPos);
		int pW = Constants.getPixelWidth(g2,text);
        textWidth=(pW>textWidth)?(pW):(textWidth);

		g2.drawLine(x,uLinePos,x+textWidth+15,uLinePos);
		g2.drawLine(x+textWidth+15,uLinePos,x+textWidth+25,pos11+10);
		g2.drawLine(x+textWidth+25,pos11,x+textWidth+25,pos11+10);
    }


    public void drawArrow(Graphics2D g2, Point srcObj, Point destObj, int arrowKind, int lineKind) {
    	Point p1, p2;
		if (srcObj.x < destObj.x){
			p1 = new Point(destObj.x-arrowX, destObj.y+arrowY);
			p2 = new Point(destObj.x-arrowX, destObj.y-arrowY);
	    } else {
	    	p1 = new Point(destObj.x+arrowX, destObj.y+arrowY);
	    	p2 = new Point(destObj.x+arrowX, destObj.y-arrowY);
	    }
		 
		if(arrowKind==SYNC) {		
			g2.drawLine(p1.x, p1.y, p2.x, p2.y);
 			g2.drawLine(destObj.x, destObj.y, p1.x, p1.y);
 			g2.drawLine(destObj.x, destObj.y, p2.x, p2.y);

 			if(lineKind==DOTTED) g2.setStroke(Constants.getStroke(1,1));
 			g2.drawLine(srcObj.x,srcObj.y,p1.x, destObj.y);
		} else if(arrowKind==ASYNC) {
			g2.drawLine(destObj.x, destObj.y, p1.x, p1.y);
			g2.drawLine(destObj.x, destObj.y, p2.x, p2.y);

			if(lineKind==DOTTED) g2.setStroke(Constants.getStroke(1,1));
			g2.drawLine(srcObj.x,srcObj.y,destObj.x, destObj.y);
		} else if(arrowKind==EDGE) {
			g2.drawLine(destObj.x, destObj.y, p2.x, p2.y);
			
			if(lineKind==DOTTED) g2.setStroke(Constants.getStroke(1,1));
			g2.drawLine(srcObj.x,srcObj.y,destObj.x, destObj.y);
		} else if(arrowKind==FILLED) {
			Polygon p = new Polygon();
			p.addPoint(p1.x, p1.y);
			p.addPoint(p2.x, p2.y);
			p.addPoint(destObj.x, destObj.y);
			g2.fillPolygon(p);
			
 			if(lineKind==DOTTED) g2.setStroke(Constants.getStroke(1,1));
 			g2.drawLine(srcObj.x,srcObj.y,p1.x, destObj.y);
		}
		g2.setStroke(Constants.getStroke(0,1));
    }


    protected int hCenterForObj(int objNum) {
    	return (objNum*rectWidth + (objNum-1)*rectDistance + borderDistance -
    			rectWidth/2);
    }

    protected int vCenterForLevel(int level) {
    	return (level*levelHeight + rectToFirstLevelDistance + 
    			rectHeight + borderDistance+yOffsetforTitle - levelHeight/2);
    }


    public int getPossibleResizeDirections() { return 0; } //deny size changes: dynamic resize is done by the diagram itself

    public StickingPolygon getStickingBorder() {
    	StickingPolygon p = new StickingPolygon();
		
		p.addLine(new Point(0,0), new Point(this.getWidth()-1,0),
			  Constants.RESIZE_TOP);
		p.addLine(new Point(this.getWidth()-1,0), 
			  new Point(this.getWidth()-1,this.getHeight()-1),
			  Constants.RESIZE_RIGHT);
		p.addLine(new Point(this.getWidth()-1,this.getHeight()-1), 
			  new Point(0,this.getHeight()-1),Constants.RESIZE_BOTTOM);
		p.addLine(new Point(0,this.getHeight()-1), new
			  Point(0,0),Constants.RESIZE_LEFT);
		
		return p;
    }
}