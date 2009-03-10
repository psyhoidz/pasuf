package com.umlet.element.base;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;


import com.umlet.control.Constants;

public class ActivityDiagram extends Entity {
	static final long serialVersionUID = 0;
    private static final int distance = 80;
    private static int INITIAL_OFFSET = 10; //offset from border
    private int yOffsetforTitle=0;
    private HashMap<String,ElementObject> elementsH = null;
    private int[] columnOffset = new int[20];
    private Vector<Verb> verbindungen = null;
    
	public ActivityDiagram() {
    }
	
	private String getHashKey(String elementName) {
		String elementIdent = "";
		
		if(elementName.startsWith("if(")) return elementName.substring(0,elementName.indexOf("){")+1);
		if(elementName.startsWith("forkH:")) return elementName.substring(0,elementName.indexOf("{"));
		if(elementName.startsWith("join:")) {
			if(elementName.length()>"join:".length()) return elementName.substring("join:".length());
			else return elementName;
		}
		if(elementName.startsWith("fend:")) {
			if(elementName.length()>"fend:".length()) return elementName.substring("fend:".length());
			else return elementName;
		}
		
		//### handle syncH, sigSend, sigRec, sigTime
		String head=null;
		if(elementName.startsWith("syncH:")) {
			head = "syncH:";
		} else if(elementName.startsWith("sigSend:")) {
			head = "sigSend:";
		} else if(elementName.startsWith("sigRec:")) {
			head = "sigRec:";
		} else if(elementName.startsWith("sigTime:")) {
			head = "sigTime:";
		}
		if(head!=null) {
			String text = elementName.substring(head.length());
			if(text.indexOf(':')>0) elementIdent = text.substring(text.indexOf(':')+1);
			return (!elementIdent.equals(""))?(elementIdent):text;
		}
		
		//### handle actions
		if(elementName.indexOf(':')>0) elementIdent = elementName.substring(elementName.indexOf(':')+1);
		return (!elementIdent.equals(""))?(elementIdent):elementName;
	}
	
	
	
	public void paint(Graphics g) {
    	// Some unimportant initialization stuff; setting color, font 
    	// quality, etc. You should not have to change this.
    	Graphics2D g2=(Graphics2D) g; g2.setFont(Constants.getFont());
    	g2.setColor(_activeColor); Constants.getFRC(g2);
    	
    	//draw the border
    	g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
    	
    	//### init columns ###
    	for(int i=0;i<columnOffset.length;i++) {
    		columnOffset[i] = 125*(i+1)-50;
    	}
    	verbindungen = new Vector<Verb>(); //clear verbindungen vector

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
	    		//INITIAL_OFFSET+=25;
	    		yOffsetforTitle=25;
    		}
    	}
    	
    	//### identify all elements
    	elementsH = new HashMap<String,ElementObject>(); //[elementText][elementIdent]
    	for (int i=0; i < lines.size(); i++) {
    		String line = lines.elementAt(i);
    		Vector<String> elementsOfLine = Constants.decomposeStrings(line,"->", "<-");
    		for(int el=0;el<elementsOfLine.size();el++) {
    			String element = elementsOfLine.elementAt(el);
    			if(element.startsWith("if(")){ //DECISION##########################################
//    				System.err.println("IF: "+element);
    				//### get decisions ###
    				Vector<String> ifs = Constants.decomposeStrings(element,"if(");
    				for(int j=0;j<ifs.size();j++) {
    					//System.err.println("___if: if("+ifs.get(j));
    					ElementObject eo = new ElementObject();
    					String key = getHashKey("if("+ifs.get(j));
    					eo.name=key;
    					eo.ident=key;
    					elementsH.put(key,eo);
    				}
    				
    				//### get elements referenced by the decisions ###
    				String s=element;
    				while(s.indexOf("{")>=0 && s.indexOf("}")>0) {
    					int p2=s.indexOf("}");
    					//if(p2>0) {
    						String elementIdent=s.substring(s.indexOf("{")+1,p2);
    						if(elementIdent.startsWith("if(") && elementIdent.indexOf("{")>=0) {
    							elementIdent=elementIdent.substring(elementIdent.indexOf("{")+1);
    							
    						}
    						s=s.substring(p2+1);
    						String key = getHashKey(elementIdent);
    						//System.err.println("////:"+elementIdent+ " / "+key);
    						if(!elementsH.containsKey(key)) {
    							ElementObject eo = new ElementObject();
    							eo.name=key;
    							eo.ident=key;
    							elementsH.put(key,eo);
    						}
    					//}
    				}
    			} else if(element.startsWith("forkH:")) { //FORK######################################
//    				System.err.println("FORK: "+element);
    				ElementObject eo = new ElementObject();
					String key = getHashKey(element);
					eo.name=key;
					eo.ident=key;
					elementsH.put(key,eo);
//					System.out.println("FORK:key:"+key);
    				
    				//### get elements referenced by the fork ###
					String destinations = element.substring(element.indexOf("{"));
					Vector<String> dests = Constants.decomposeStrings(destinations,"}{");
					for(int j=0;j<dests.size();j++) {
						String d=dests.get(j);
						if(d.startsWith("{")) d=d.substring(1);
						else if(d.endsWith("}")) d=d.substring(0,d.length()-1);
						
						key = getHashKey(d);
						if(!elementsH.containsKey(key)) {
							eo = new ElementObject();
							eo.name=key;
							eo.ident=key;
							elementsH.put(key,eo);
						}
					}
    			} else if(element.startsWith("syncH:")) {
    				ElementObject eo = new ElementObject();
					String key = getHashKey(element);
					eo.name=element; //key?
					eo.ident=key;
					elementsH.put(key,eo);
    			}else if(element.startsWith("join:")) { //JOIN#######################################
    				ElementObject eo = new ElementObject();
					String key = getHashKey(element);
					eo.name=element; //key?
					eo.ident=key;
					elementsH.put(key,eo);
    			} else { //OTHER/ACTION###############################################################
    				String key = getHashKey(element);
    				if(!elementsH.containsKey(key)) {
    					ElementObject eo = new ElementObject();
    					eo.name = element;
    					eo.ident = key;
    					elementsH.put(key,eo);
    				}
    			}
    		}
    	}
    	
    	
    	//### draw strang
    	int columnCounter = 0;
    	for (int i=0; i < lines.size(); i++) {
    		String line = lines.elementAt(i);
    		Vector<String> elementsOfLine = Constants.decomposeStrings(line,"->");

    		int y=INITIAL_OFFSET+yOffsetforTitle;
    		ElementObject fromEl=null;
    		
    		Iterator<String> elementIterator = elementsOfLine.iterator();
    		int elPos=0;
    		while(elementIterator.hasNext()) {
    			boolean newColUsed=false;
    			String elementX = elementIterator.next();
    			Vector<String> revElements = Constants.decomposeStrings(elementX,"<-");
    			System.out.println("revElements: "+revElements);
    			for(int el=0;el<revElements.size();el++) {
	    			String element=revElements.elementAt(el);
	    			String key = getHashKey(element);
	    			
	    			ElementObject actEl = elementsH.get(key);
	    			if(actEl!=null) {
		    			if(elPos<elementsOfLine.size()-1 || el<revElements.size()-1) actEl.conn++; //wenn nicht letztes el in zeile, conn erhöhen
						if(actEl!=null && actEl.x==0 && actEl.y==0 && actEl.w==0 && actEl.h==0) {
							int column=columnCounter;
							if(fromEl!=null) column = fromEl.colNr+fromEl.conn-1;
							y=processElement(g2,y,fromEl,actEl,element, column);
							newColUsed=true; //nur wenn neues Element in columne eingefügt
						} else {
							y=actEl.y+distance;//+actEl.h+
						}
						
		    			doArrow(fromEl, actEl,0,0,null, revElements.size()>1); 
		    			
		    			fromEl=actEl;
	    			}
	    			if(newColUsed) columnCounter++;
    			}
    			elPos++;
    		}
    		
    	}
//    	System.err.println(columnCounter);
    	Collection coll = elementsH.values();
    	Iterator it = coll.iterator();
    	while(it.hasNext()) {
    		ElementObject el = (ElementObject)it.next();
    		if(el!=null) el.draw(g2);
    	}
    	
    	//get all destination-referenced syncs
    	Vector<ElementObject> eov = new Vector<ElementObject>();
    	for(int i=0;i<verbindungen.size();i++) {
    		Verb v = verbindungen.get(i);
    		if(v!=null && v.dst!=null && v.dst.type != null && v.dst.type.equals("syncbar")) {
    			if(!eov.contains(v.dst)) eov.add(v.dst);
    		}
    	}
    	
    	//### sort the verbs of the bar, so the arrows are not mixed, but ordered from left to right 
    	//### according to the column their source objects are
    	TreeSet<Verb> sortedVerbs = new TreeSet<Verb>(new Comparator<Verb>() { //sorted TreeSet using an anonymous Comparator-class 
    			public int compare(Verb v1, Verb v2) {
    				if     (v1.src.colNr >v2.src.colNr) return 1;
    				else if(v1.src.colNr==v2.src.colNr) return 0;
    				else /*if(v1.src.colNr <v2.src.colNr)*/ return -1;
    			}
    		}
    	);
    	
    	Vector<Verb> unsortedVerbs = new Vector<Verb>();
    	for(int i=0;i<eov.size();i++) {
    		for(int vi=0;vi<verbindungen.size();vi++) {
    			Verb v=verbindungen.get(vi);
    			ElementObject e1 = v.dst;
    			ElementObject e2 = eov.get(i);
    			if(e1.equals(e2)&&v.src!=null) {
    				sortedVerbs.add(v); //collect verbs
    				unsortedVerbs.add(v);
    			}
    		}
    		Iterator<Verb> iter = sortedVerbs.iterator();
    		int j=0;
    		while(iter.hasNext()) { //replace unsorted verbs with sorted ones
    			Verb vs = iter.next();
    			int idx = verbindungen.indexOf(unsortedVerbs.get(j));
    			if(idx>=0) verbindungen.set(idx,new Verb(vs.src, vs.dst, vs.name, vs.amount, vs.amountPos, vs.direction));
    			j++;
    		}
    	}

  	
    	
    	//### draw the arrows ###
    	for(int i=0;i<verbindungen.size();i++) {
    		Verb v = verbindungen.get(i);
    		drawArrow(g2, v.src,v.dst,v.amount,v.amountPos,v.name,v.direction);
    	}
    	System.err.println("------------------------------------------------------------------------------------");
	}
	
//###########################################################################################################
	private int processElement(Graphics2D g2, int row, ElementObject fromEl, ElementObject actEl, String element, int colNr) {
		if(actEl==null || element==null) {System.err.println("ACTEL NULL"); return 0;}
		if(actEl.x==0 && actEl.y==0 && actEl.w==0 && actEl.h==0) { //nur zeichnen wenn noch nicht gezeichnet
			if(element.equals("start")) doStart(row, actEl, colNr); //draw start element
			else if(element.equals("end")) doEnd(row, actEl, colNr); //draw end element
			else if(element.startsWith("fend:")) doFlowEnd(row, actEl, colNr); //draw flow-end element
			else if(element.startsWith("if(")) processIf(g2, row, element, fromEl, colNr);
			else if(element.startsWith("forkH:")) processFork(g2, row, element, fromEl, colNr);
			else if(element.startsWith("syncH:")) doSyncBar(row, actEl, colNr); //draw bar element
			else if(element.startsWith("join:")) doDecisionJoin(row, actEl, colNr); //draw join element
			else if(element.startsWith("sigTime:")) doTimeSignal(row, element, actEl, colNr); //draw time signal element
			else doAction(g2, row, element, actEl, colNr); //draw action element
		}
		return actEl.y+distance;
	}
	
	private void moveAllExistingElements() {
		Collection coll = elementsH.values();
    	Iterator it = coll.iterator();
    	while(it.hasNext()) {
    		ElementObject el = (ElementObject)it.next();
    		if(el!=null) el.colNr++;
    	}
	}
	
	private void processFork(Graphics2D g2, int row, String element, ElementObject fromEl, int colNr) {
		String destinations = element.substring(element.indexOf("{"));
		Vector<String> dests = Constants.decomposeStrings(destinations,"}{");

		String key = getHashKey(element);
		ElementObject actEl = elementsH.get(key);
		actEl.conn++;

		ArrayList<String> destsX = new ArrayList<String>();
		for(int j=0;j<dests.size();j++) {
			String d=dests.get(j);
			if(d.startsWith("{")) d=d.substring(1);
			else if(d.endsWith("}")) d=d.substring(0,d.length()-1);
			destsX.add(d);
		}
		
		if(destsX.size()==2) {
			doForkBar(row,actEl, colNr+(destsX.size()-2));
		} else if(destsX.size()>=3) {
			if(fromEl!=null && fromEl.conn==1 && actEl!=null && actEl.colNr==0) moveAllExistingElements();
			fromEl=actEl;
			doForkBar(row,actEl, colNr+(destsX.size()-2));
		} else {
			//forks are not centered but placed n-1
		}
			
		actEl.conn=destsX.size();
		fromEl = actEl;
		row = actEl.y+distance;

		for(int i=0;i<destsX.size();i++) {
			String elementIdent = destsX.get(i);
			key = getHashKey(elementIdent);
			actEl = elementsH.get(key);
			processElement(g2,row,fromEl, actEl,elementIdent, colNr+i);
			doArrow(fromEl,actEl,destsX.size()+1,i+1,null, false);
		}
	}

	
	
	private void processIf(Graphics2D g2, int row, String element, ElementObject fromEl, int colNr) {
		Vector<String> ifs = Constants.decomposeStrings(element,"if(");
		for(int j=0;j<ifs.size();j++) {
			String key = getHashKey("if("+ifs.get(j));
			ElementObject actEl = elementsH.get(key);
			actEl.conn++;
			
			//### get conditions from decision-elements
			ArrayList<String> conditions = new ArrayList<String>();
			char[] cArr = element.toCharArray();
			int startPos=0,endPos=0;
			for(int ci=0;ci<cArr.length;ci++) {
				if(cArr[ci]=='(') startPos=ci;
				if(cArr[ci]==')') endPos=ci;
				if(startPos!=0 && endPos!=0) {
					conditions.add("["+element.substring(startPos+1,endPos)+"]");
					startPos=0;
					endPos=0;
				}
			}
			
			//### get referenced elements
			String s=element;
			ArrayList<String> ifelements = new ArrayList<String>();
			while(s.indexOf("{")>=0 && s.indexOf("}")>0) {
				int p2 = 0;
				boolean nest = false;
				if(s.substring(s.indexOf("{")+1).startsWith("if(")) {
					nest=true;
					p2=s.indexOf("}}");
				}
				if(p2<=0) p2=s.indexOf("}");
				if(p2>0) {
					String elementIdent=s.substring(s.indexOf("{")+1,p2+(nest?1:0));
					s=s.substring(p2+1);
					ifelements.add(elementIdent);
				}
			}

			if(ifelements.size()==2) {
				doDecisionJoin(row,actEl, colNr);
			} else if(ifelements.size()==3) {
				if(fromEl!=null && fromEl.conn==1 && actEl!=null && actEl.colNr==0) moveAllExistingElements();
				fromEl=actEl;
				doDecisionJoin(row,actEl, colNr+1);
			} else {
				//System.err.println("IF FEHLERHAFT");
				//currently there is no support for decisions with more than 3 destinations
			}

			actEl.conn=ifelements.size();
			fromEl = actEl;
			row = actEl.y+distance;//actEl.y+actEl.h+distance;

			for(int i=0;i<ifelements.size();i++) {
				String elementIdent = ifelements.get(i);
				key = getHashKey(elementIdent);
				actEl = elementsH.get(key);
				/*boolean nest=elementIdent.startsWith("if("); //nested decisions to be done soon
				if(nest) {
					actEl.colNr++;
					ElementObject ifEl = new ElementObject();
					ifEl=actEl;
					processElement(g2,row,fromEl, ifEl,elementIdent, colNr+i);//+(nest?0:0));
				}
				else*/ processElement(g2,row,fromEl, actEl,elementIdent, colNr+i);//+(nest?1:0));
				if(i<conditions.size()) doArrow(fromEl,actEl,ifelements.size()+1,i+1,conditions.get(i), false);
			}
		}
	}

//#### drawings ##########################
//#### drawings ##########################
//#### drawings ##########################
	
	private void doSyncBar(int row, ElementObject el, int colNr) {
		el.type="syncbar";
		el.w=80;
		el.h=5;
		el.x=columnOffset[colNr]-el.w/2;
		el.y=row;
		el.colNr=colNr;
	}
	
	private void doForkBar(int row, ElementObject el, int colNr) {
		el.type="forkbar";
		el.w=80;
		el.h=5;
		el.x=columnOffset[colNr]-el.w/2;
		el.y=row;
		el.colNr=colNr;
	}
	
	private void doDecisionJoin(int row, ElementObject el, int colNr) {
		el.type="decisionjoin";
		el.w=30;
		el.h=el.w;
		el.x=columnOffset[colNr]-el.w/2;
		el.y=row;
		el.colNr=colNr;
	}
	
	private void doStart(int row, ElementObject el, int colNr) {
		el.type="start";
		el.w=20;
		el.h=el.w;
		el.x=columnOffset[colNr]-el.w/2;
		el.y=row;
		el.colNr=colNr;
	}
	

	private void doEnd(int row, ElementObject el, int colNr) {
		el.type="end";
		el.w=20;
		el.h=el.w;
		el.x=columnOffset[colNr]-el.w/2;
		el.y=row;
		el.colNr=colNr;
	}
	
	private void doFlowEnd(int row, ElementObject el, int colNr) {
		el.type="flowend";
		el.w=20;
		el.h=el.w;
		el.x=columnOffset[colNr]-el.w/2;
		el.y=row;
		el.colNr=colNr;
	}
	
	private void doTimeSignal(int row, String element, ElementObject el, int colNr) {
		el.type="sigTime";
		el.x=columnOffset[colNr];
		el.y=row;
		el.w=0;
		el.h=30;
		el.colNr=colNr;
		int d1 = element.indexOf(":")+1;
		int d2 = element.indexOf(":",d1);
		if(d2>0) { //two ':'?
			el.text=element.substring(d1,d2);
		}
		el.text = element.substring("sigTime:".length());
	}
	
	private void doAction(Graphics2D g2, int row, String element, ElementObject el, int colNr) {
		String textLine = element;
		if(element.startsWith("sigSend:")) {
			el.type="sigSend";
			textLine = textLine.substring("sigSend:".length());
		} else if(element.startsWith("sigRec:")) {
			textLine = textLine.substring("sigRec:".length());
			el.type="sigRec";
		}
		else el.type="action";
		int textWidth = 100;
		Vector<String> lines = Constants.decomposeStrings(textLine,"\\");
		textWidth = new ElementObject().getActionTextWidth(g2, textLine);
		el.x=columnOffset[colNr]-textWidth/2-3;
		el.y=row;

		//if the action got more than 2 lines of text, resize and move the rectangle by half a line height
		int rectHeight = 40; //initial rectheight
		int slh = (Constants.getFontsize()+Constants.getDistTextToText())*lines.size()+2*Constants.getDistTextToLine();
		if(slh>rectHeight) { //correct rechHeight and y-Pos
			rectHeight=slh;
			el.y-=(slh-40)/2;
		}
		
		//### draw text line(s)
		int stringY = el.y+Constants.getFontsize()+Constants.getDistTextToLine(); //initial ypos of text
		if(lines.size()==1) stringY+=(Constants.getFontsize()+Constants.getDistTextToText())/2; //vertical offset, if text has just one line (center)
		for(int i=0;i<lines.size();i++) {
			int d = Constants.getFontsize()+Constants.getDistTextToText();
			stringY+=d;
		}

		el.w=textWidth+2*Constants.getDistTextToLine();
		el.h=rectHeight;
		el.colNr=colNr;
		el.text=textLine;
	}
	
	
	
	private void doArrow(ElementObject src, ElementObject dst, int amount, int amountPos, String name, boolean direction) {
		if(src==null || dst==null) return; //skip 
		Verb v = new Verb(src, dst, name, amount, amountPos, direction);
		verbindungen.add(v);
		if(dst.type!=null && dst.type.equals("syncbar")&& src!=null) {
			dst.in_amount++;
		}
	}
	
	private void drawArrowHead(Graphics2D g2, int x, int y, int winkel) {
		AffineTransform at = g2.getTransform();
		AffineTransform at2 = (AffineTransform)at.clone();
		at2.rotate(winkel*Math.PI/180, x, y);
		g2.setTransform(at2);
		g2.setColor(_activeColor);
		g2.drawLine(x,y,x-5,y-10);
		g2.drawLine(x,y,x+5,y-10);
		g2.setTransform(at);
	}
	
	private void drawArrow(Graphics2D g2, ElementObject src, ElementObject dst,int amount, int amountPos, String name, boolean direction) {
		if(src!=null && dst!=null && src.type!=null && dst.type!=null) {
			if(src==dst) return; //self calls are not allowed
			int quad=0;
			int srcMX = src.x+src.w/2;
			int srcMY = src.y+src.h/2;
			int dstMX = dst.x+dst.w/2;
			int dstMY = dst.y+dst.h/2;
			if((srcMX<=dstMX+5 && srcMX>=dstMX-5)&&(srcMY>dstMY)) quad=1;
			else if((srcMX<=dstMX+5 && srcMX>=dstMX-5)&&(srcMY<dstMY)) quad=5;
			else if((srcMX<dstMX)&&(srcMY<=dstMY+5 && srcMY>=dstMY-5)) quad=3;
			else if((srcMX>dstMX)&&(srcMY<=dstMY+5 && srcMY>=dstMY-5)) quad=7;

			else if((srcMX<dstMX)&&(srcMY>dstMY)) quad=2;
			else if((srcMX<dstMX)&&(srcMY<dstMY)) quad=4;
			else if((srcMX>dstMX)&&(srcMY<dstMY)) quad=6;
			else if((srcMX>dstMX)&&(srcMY>dstMY)) quad=8;
			
			int distance = 1*20;
			if(quad==1) {
				if(Math.abs(src.y-dst.y) > distance*2) {
					/* +-->
					 * |
					 * +--
					 */
					int d=10;
					g2.drawLine(src.x,src.y+src.h/2,src.x-d,src.y+src.h/2);
					g2.drawLine(src.x-d,src.y+src.h/2,src.x-d,dst.y+dst.h/2);
					g2.drawLine(src.x-d,dst.y+dst.h/2,dst.x,dst.y+dst.h/2);
					if(!direction) drawArrowHead(g2, dst.x,dst.y+dst.h/2, 270);
					else drawArrowHead(g2,src.x,src.y+src.h/2, 270);
				}
			} else if(quad==2) {
				int d=10;
				if(src.type.equals("decisionjoin") || dst.conn==0) {
					/*    A
					 *    |
					 * ---+
					 */
					g2.drawLine(src.x+src.w,src.y+src.h/2,dst.x+dst.w/2,src.y+src.h/2);
					g2.drawLine(dst.x+dst.w/2,src.y+src.h/2, dst.x+dst.w/2, dst.y+dst.h);
					if(!direction) drawArrowHead(g2, dst.x+dst.w/2, dst.y+dst.h, 180);
					else drawArrowHead(g2, src.x+src.w,src.y+src.h/2, 90);
					if(name!=null && !name.equals("[]")) { //print if-condition
						//int textWidth=Constants.getPixelWidth(g2,name);
						Constants.write(g2, name, src.x+src.w+Constants.getDistTextToLine(), src.y+src.h/2-Constants.getDistTextToLine()-1, false);
					}
				} else {
					/*   +-->
					 *   |
					 * --+
					 */
					g2.drawLine(src.x+src.w,src.y+src.h/2,src.x+src.w+d,src.y+src.h/2);
					g2.drawLine(src.x+src.w+d,src.y+src.h/2,src.x+src.w+d,dst.y+dst.h/2);
					g2.drawLine(src.x+src.w+d,dst.y+dst.h/2,dst.x,dst.y+dst.h/2);
					if(!direction) drawArrowHead(g2, dst.x,dst.y+dst.h/2, 270);
					else drawArrowHead(g2, src.x+src.w,src.y+src.h/2, 90);
				}
			} else if(quad==3) {
				/*
				 * --->
				 */
				g2.drawLine(src.x+src.w,src.y+src.h/2,dst.x,dst.y+dst.h/2);
				if(!direction) drawArrowHead(g2, dst.x,dst.y+dst.h/2, 270);
				else drawArrowHead(g2, src.x+src.w,src.y+src.h/2, 90);
				if(name!=null && !name.equals("[]")) { //print if-condition
					//int textWidth=Constants.getPixelWidth(g2,name);
					Constants.write(g2, name, src.x+src.w+Constants.getDistTextToLine(), src.y+src.h/2-Constants.getDistTextToLine()-1, false);
				}
			}else if(quad==4) {
				/* ---+
				 *    |
				 *    +---->
				 */
				//g2.drawLine(src.x+src.w,src.y+src.h/2,src.x+src.w+d1,src.y+src.h/2);
				//g2.drawLine(src.x+src.w+d1,src.y+src.h/2,src.x+src.w+d1,dst.y-d1);
				//g2.drawLine(src.x+src.w+d1,dst.y-d1,dst.x+dst.w/2,dst.y-d1);
				//g2.drawLine(dst.x+dst.w/2,dst.y-d1,dst.x+dst.w/2,dst.y);
				if(src.type.equals("forkbar")) {
					/* |
					 * +--+
					 *    v
					 */
					int p=src.w/(amount+1);
					int pos=0;
					pos=src.x+p*(amountPos+1);
					int sd_dist = Math.abs((src.y+src.h)-(dst.y))/2; 
					g2.drawLine(pos,src.y+src.h,pos,src.y+src.h+sd_dist);
					g2.drawLine(pos,src.y+src.h+sd_dist,dst.x+dst.w/2,src.y+src.h+sd_dist);
					g2.drawLine(dst.x+dst.w/2,src.y+src.h+sd_dist,dst.x+dst.w/2,dst.y);
					drawArrowHead(g2, dst.x+dst.w/2,dst.y, 0);
				} else {
					if(src.conn==1) {
						if(dst.type.equals("syncbar") || dst.type.equals("forkbar") || dst.type.equals("decisionjoin")) {
							/*  |
							 *  +--+
							 *     |
							 *     v
							 */
							dst.in_amountPos++;
							int p=dst.w/(dst.in_amount+1);
							int pos=dst.x+p*(dst.in_amountPos); //vertical position on bar
							int mitte=0;
							if(dst.in_amount%2==0) mitte=dst.in_amount/2; else mitte=(dst.in_amount-1)/2+1;							
							int mb = Math.abs(mitte-dst.in_amountPos)*8; //amount of y-pixels to remove from horizontal part of the arrow
							if(dst.type.equals("forkbar") || dst.type.equals("decisionjoin")) {
								pos = dst.x+dst.w/2;
								mb=0; //if arrow is incoming to a decision or fork, don't modify
							}
							int sd_dist = Math.abs((src.y+src.h)-(dst.y))/2 + mb; //y-position of the horizontal part of the arrow
							
							
							
							g2.drawLine(src.x+src.w/2,src.y+src.h,src.x+src.w/2,src.y+src.h+sd_dist);
							g2.drawLine(src.x+src.w/2,src.y+src.h+sd_dist,pos,src.y+src.h+sd_dist);
							g2.drawLine(pos,src.y+src.h+sd_dist,pos,dst.y);
							if(!direction) drawArrowHead(g2, pos,dst.y, 0);
							else drawArrowHead(g2, src.x+src.w/2,src.y+src.h, 180);
						} else {
						   /*  |
						 	*  +-->
						 	*/
							g2.drawLine(src.x+src.w/2,src.y+src.h,src.x+src.w/2,dst.y+dst.h/2);
							g2.drawLine(src.x+src.w/2,dst.y+dst.h/2,dst.x,dst.y+dst.h/2);
							if(!direction) drawArrowHead(g2, dst.x,dst.y+dst.h/2, 270);
							else drawArrowHead(g2, src.x+src.w/2,src.y+src.h, 180);
						}
					} else if(src.conn>1) {
						/*	--+
						 * 	  |
						 *    v
						 */
						if(name!=null && !name.equals("[]")) { //print if-condition
							//int textWidth=Constants.getPixelWidth(g2,name);
							Constants.write(g2, name, src.x+src.w+Constants.getDistTextToLine(), src.y+src.h/2-Constants.getDistTextToLine()-1, false);
						}
						g2.drawLine(src.x+src.w,src.y+src.h/2,dst.x+dst.w/2,src.y+src.h/2);
						g2.drawLine(dst.x+dst.w/2,src.y+src.h/2,dst.x+dst.w/2,dst.y);
						if(!direction) drawArrowHead(g2, dst.x+dst.w/2,dst.y, 0);
						else drawArrowHead(g2, src.x+src.w,src.y+src.h/2, 90);
					}
				}
			} else if(quad==5) {
				if(src.type.equals("forkbar") && amount%2!=0) {
					/* |            |
					 * +--+ oder +--+
					 *    |      |
					 *    v      v
					 */
					int p=src.w/(amount+1);
					int pos=0;
					pos=src.x+p*(amountPos+1);
					int sd_dist = Math.abs((src.y+src.h)-(dst.y))/2; 
					g2.drawLine(pos,src.y+src.h,pos,src.y+src.h+sd_dist);
					g2.drawLine(pos,src.y+src.h+sd_dist,dst.x+dst.w/2,src.y+src.h+sd_dist);
					g2.drawLine(dst.x+dst.w/2,src.y+src.h+sd_dist,dst.x+dst.w/2,dst.y);
					drawArrowHead(g2, dst.x+dst.w/2, dst.y, 0);
				} else {
					if(dst.type.equals("syncbar")) {
						/* |            |
						 * +--+ oder +--+
						 *    |      |
						 *    v      v
						 */
						dst.in_amountPos++;
						int p=dst.w/(dst.in_amount+1);
						int pos=dst.x+p*(dst.in_amountPos); //vertical position on bar
						int mitte=0; //center arrow pos
						if(dst.in_amount%2==0) mitte=dst.in_amount/2; else mitte=(dst.in_amount-1)/2+1;							
						int mb = Math.abs(mitte-dst.in_amountPos)*8; //amount of y-pixels to remove from horizontal part of the arrow
						int sd_dist = Math.abs((src.y+src.h)-(dst.y))/2 - mb; //y-position of the horizontal part of the arrow  
						g2.drawLine(src.x+src.w/2,src.y+src.h,src.x+src.w/2,src.y+src.h+sd_dist);
						g2.drawLine(src.x+src.w/2,src.y+src.h+sd_dist,pos,src.y+src.h+sd_dist);
						g2.drawLine(pos,src.y+src.h+sd_dist,pos,dst.y);
						drawArrowHead(g2, pos,dst.y, 0);
					} else {
						/*  |
						 *  v
						 */
						if(amount>0) {
							if(name!=null && !name.equals("[]")) { //print if-condition
								int textWidth=Constants.getPixelWidth(g2,name);
								Constants.write(g2, name, src.x+src.w/2+textWidth/2+Constants.getDistTextToLine(), src.y+src.h+Constants.getFontsize()+Constants.getDistLineToText(), true);
							}
						}
						g2.drawLine(src.x+src.w/2,src.y+src.h,dst.x+dst.w/2,dst.y);
						if(!direction) drawArrowHead(g2, dst.x+dst.w/2, dst.y, 0);
						else drawArrowHead(g2, src.x+src.w/2,src.y+src.h, 180);
					}
				}
			} else if(quad==6) {
				if(amount>0) {
					if(src.type.equals("forkbar")) {
						/*     |
						 *  +--+
						 *  |
						 *  v
						 */
						int p=src.w/(amount+1);
						int pos=0;
						pos=src.x+p*(amountPos);
						int sd_dist = Math.abs((src.y+src.h)-(dst.y))/2+(amountPos-1)*10; 
						g2.drawLine(pos,src.y+src.h,pos,src.y+src.h+sd_dist);
						g2.drawLine(pos,src.y+src.h+sd_dist,dst.x+dst.w/2,src.y+src.h+sd_dist);
						g2.drawLine(dst.x+dst.w/2,src.y+src.h+sd_dist,dst.x+dst.w/2,dst.y);
						drawArrowHead(g2, dst.x+dst.w/2, dst.y, 0);
					} else { 
						if(amount>1 && amountPos==1) { //erstes if_element
							/* +---
							 * |
							 * v
							 */
							g2.drawLine(src.x,src.y+src.h/2,dst.x+dst.w/2,src.y+src.h/2);
							g2.drawLine(dst.x+dst.w/2,src.y+src.h/2,dst.x+dst.w/2,dst.y);
							if(!direction) drawArrowHead(g2, dst.x+dst.w/2, dst.y, 0);
							else drawArrowHead(g2, src.x,src.y+src.h/2, 270);
							if(name!=null && !name.equals("[]")) { //print if-condition
								int textWidth=Constants.getPixelWidth(g2,name);
								Constants.write(g2, name, src.x-textWidth-Constants.getDistTextToLine(), src.y+src.h/2-Constants.getDistTextToLine()-1, false);
							}
						} else {
							//this arrow-line shall indicate missing arrow types
							//g2.drawLine(src.x+src.w,src.y+src.h,dst.x+dst.w/2,dst.y);
							g2.drawLine(src.x+src.w/2,src.y+src.h/2,dst.x+dst.w/2,dst.y+dst.h/2);
							//drawArrowHead(g2, dst.x+dst.w/2, dst.y, 0);
						}
					}
				} else {
					if(dst.type.equals("syncbar")) {
						/*     |
						 *  +--+
						 *  |
						 *  v
						 */
						dst.in_amountPos++;
						int p=dst.w/(dst.in_amount+1);
						int pos=dst.x+p*(dst.in_amountPos); //vertical position on bar
						int mitte=0; //center arrow pos
						if(dst.in_amount%2==0) mitte=dst.in_amount/2; else mitte=(dst.in_amount-1)/2+1;							
						int mb = Math.abs(mitte-dst.in_amountPos)*8; //amount of y-pixels to remove from horizontal part of the arrow
						int sd_dist = Math.abs((src.y+src.h)-(dst.y))/2 + mb; //y-position of the horizontal part of the arrow 
						g2.drawLine(src.x+src.w/2,src.y+src.h,src.x+src.w/2,src.y+src.h+sd_dist);
						g2.drawLine(src.x+src.w/2,src.y+src.h+sd_dist,pos,src.y+src.h+sd_dist);
						g2.drawLine(pos,src.y+src.h+sd_dist,pos,dst.y);
						if(!direction) drawArrowHead(g2, pos,dst.y, 0);
						else drawArrowHead(g2, src.x+src.w/2,src.y+src.h, 180);
					} else {
						/*    |
						 *  <-+
						 */
						g2.drawLine(src.x+src.w/2,src.y+src.h,src.x+src.w/2,dst.y+dst.h/2);
						g2.drawLine(src.x+src.w/2,dst.y+dst.h/2,dst.x+dst.w,dst.y+dst.h/2);
						if(!direction) drawArrowHead(g2, dst.x+dst.w,dst.y+dst.h/2, 90);
						else drawArrowHead(g2, src.x+src.w/2,src.y+src.h, 180);
					}
				}
			} else if(quad==7) {
				/*
				 * <---
				 */
				g2.drawLine(src.x,src.y+src.h/2,dst.x+dst.w,dst.y+dst.h/2);
				drawArrowHead(g2, dst.x+dst.w,dst.y+dst.h/2, 90);
			} else if(quad==8) {
				int d=10;
				/* <--+
				 *    |
				 *    +--
				 */
				g2.drawLine(src.x,src.y+src.h/2,src.x-d,src.y+src.h/2);
				g2.drawLine(src.x-d,src.y+src.h/2,src.x-d,dst.y+dst.h/2);
				g2.drawLine(src.x-d,dst.y+dst.h/2,dst.x+dst.w,dst.y+dst.h/2);
				if(!direction) drawArrowHead(g2, dst.x+dst.w,dst.y+dst.h/2, 90);
				else drawArrowHead(g2, src.x,src.y+src.h/2, 270);
			} else {
				System.err.println("QUAD fehlerhaft...");
				//should never happen
			}
		}
	}


	class ElementObject {
		public String name = "";
		public String ident = "";
		public int x=0;
		public int y=0;
		public int w=0;
		public int h=0;
		public int conn=0;
		public int colNr=0;
		public String type=null;
		public String text=null;
		public int in_amount=0;
		public int in_amountPos=0;
		
		public void draw(Graphics2D g2) {
			if(type==null) return;
			if(type.equals("syncbar") || type.equals("forkbar")) {
				x=columnOffset[colNr]-w/2;
				g2.fillRect(x,y,w,h);
			} else if(type.equals("decisionjoin")) {
				x=columnOffset[colNr]-w/2;
				g2.drawLine(x+w/2,y,x+w,y+h/2);
				g2.drawLine(x+w/2,y,x,y+h/2);
				g2.drawLine(x,y+h/2,x+w/2,y+h);
				g2.drawLine(x+w-1,y+h/2,x+w/2-1,y+h);
			} else if(type.equals("start")) {
				x=columnOffset[colNr]-w/2;
				g2.fillOval(x,y,20,20);
			} else if(type.equals("end")) {
				x=columnOffset[colNr]-w/2;
				g2.drawOval(x,y,20,20);
				g2.fillOval(x+4,y+4,13,13);
			} else if(type.equals("flowend")) {
				x=columnOffset[colNr]-w/2;
				int d=3;
				g2.drawOval(x,y,20,20);
				g2.drawLine(x+d,y+d,x+w-d,y+h-d);
				g2.drawLine(x+w-d,y+d,x+d,y+h-d);
			} else if(type.equals("sigTime")) {
				g2.drawLine(x-15,y,x+15,y+h);
				g2.drawLine(x+15,y,x-15,y+h);
				g2.drawLine(x-15,y,x+15,y);
				g2.drawLine(x-15,y+h,x+15,y+h);
				
				Vector<String> lines=null;
				if(text!=null && text.length()>0) lines = Constants.decomposeStrings(text,"\\");
				int stringY = y+h+Constants.getDistLineToText()*5;
				if(lines!=null) {
					for(int i=0;i<lines.size();i++) {
						String line = lines.elementAt(i);
						if(line.endsWith(":"+ident)) line = line.substring(0,line.indexOf(":"+ident)); //truncate identifier string
						if(line!=null && line.length()>0) Constants.write(g2, line, x, stringY, true);
						int d = Constants.getFontsize()+Constants.getDistTextToText();
						stringY+=d;
					}
				}
				
			} else if(type.equals("action") || type.equals("sigSend") || type.equals("sigRec")) {
				int textWidth = 100;
				
				Vector<String> lines = null;
				if(text!=null && text.length()>0) lines = Constants.decomposeStrings(text,"\\");
	
				textWidth = getActionTextWidth(g2, text);
				x=columnOffset[colNr]-textWidth/2-3;
				
				//when the action has more than 2 lines of text, resize and move the rectangle by half a line height
				int rectHeight = 40; //initial rectheight
				if(lines!=null) {
					int slh = (Constants.getFontsize()+Constants.getDistTextToText())*lines.size()+2*Constants.getDistTextToLine();
					if(slh>rectHeight) { //correct rechHeight and y-Pos
						rectHeight=slh;
						y-=(slh-40)/2;
					}
				
					//### draw text line(s)
					int stringY = y+Constants.getFontsize()+Constants.getDistTextToLine(); //initial ypos of text
				
					if(lines.size()==1) stringY+=(Constants.getFontsize()+Constants.getDistTextToText())/2; //vertical offset, if text has just one line (center)
					for(int i=0;i<lines.size();i++) {
						String line = lines.elementAt(i);
						if(line.endsWith(":"+ident)) line = line.substring(0,line.indexOf(":"+ident)); //truncate identifier string
						if(line!=null && line.length()>0) Constants.write(g2, line, x+textWidth/2, stringY, true);
						int d = Constants.getFontsize()+Constants.getDistTextToText();
						stringY+=d;
					}
				}
	
				
				//rectHeight=(slh>rectHeight)?(slh):rectHeight;
				w=textWidth+2*Constants.getDistTextToLine();
				h=rectHeight;
				
				if(type.equals("action")) g2.drawRoundRect(x,y,w,h,30,30);
				else if(type.equals("sigSend")) {
					g2.drawLine(x,y,x+w-Constants.getFontsize(),y); 		// ---
					g2.drawLine(x,y+h,x+w-Constants.getFontsize(),y+h);		// ---
					g2.drawLine(x,y,x,y+h);									// |
					g2.drawLine(x+w-Constants.getFontsize(),y,x+w,y+h/2);	//	   \
					g2.drawLine(x+w,y+h/2,x+w-Constants.getFontsize(),y+h); //     /
				} else if(type.equals("sigRec")) {
					g2.drawLine(x,y,x+w,y); 								// ---
					g2.drawLine(x,y+h,x+w,y+h);								// ---
					g2.drawLine(x+w,y,x+w,y+h);								//     |
					g2.drawLine(x,y,x+Constants.getFontsize(),y+h/2);		// \
					g2.drawLine(x+Constants.getFontsize(),y+h/2,x,y+h);		// /
				}
			}
		}
		
		public int getActionTextWidth(Graphics2D g2, String text) {
			int textWidth = 100;
			if(text==null) return 0;
			Vector<String> lines = Constants.decomposeStrings(text,"\\");
	
			//### get max text width
			for(int i=0;i<lines.size();i++) {
				String line = lines.elementAt(i);
				TextLayout l=new TextLayout(line, Constants.getFont(), Constants.getFRC(g2));
		    	Rectangle2D r2d=l.getBounds();
		    	textWidth=((int)r2d.getWidth()>textWidth)?((int)r2d.getWidth()):(textWidth);
			}
			return textWidth;
		}
	}
	


	class Verb {
		ElementObject src, dst;
		int amount = 0;
		int amountPos = 0;
		String name=null;
		boolean direction=false;
		
		public Verb(ElementObject src, ElementObject dst, String name, int amount, int amountPos, boolean direction) {
			this.src = src;
			this.dst = dst;
			this.name = name;
			this.amount = amount;
			this.amountPos = amountPos;
			this.direction=direction;
		}
	}
	
}