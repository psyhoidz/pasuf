package com.umlet.element.base;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;

import com.umlet.control.*;
import com.umlet.element.base.detail.*;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company:</p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class Relation extends Entity {
	
	String beginQualifier;
	String endQualifier;
	String beginArrow;
	String endArrow;
	String beginMultiplicity;
	String endMultiplicity;
	String beginRole;
	String endRole;
	String lineType;
	String eerRelDir;

	Vector<String> _strings;

	// A.Mueller start
	String clientServer;
	// A.Mueller end

	// G.Mueller start
	String beginPort;
	String endPort;
	String middleArrow;
	String csdStartText; // Arrow-Text for composite structure diagram
	String csdEndText;
	// G.Mueller end

	private Vector<String> getStrings() {
		if (_strings == null) {
			_strings = new Vector<String>();
		}
		return _strings;
	}

	private void setStrings(Vector<String> v) {
		_strings = v;
	}

	private Point getCenterOfLine() {
		Point ret = new Point();
		if (this.getLinePoints().size() % 2 == 1)
			ret = this.getLinePoints().elementAt(this.getLinePoints().size() / 2);
		else {
			Point p1 = this.getLinePoints().elementAt(this.getLinePoints().size() / 2);
			Point p2 = this.getLinePoints().elementAt(this.getLinePoints().size() / 2 - 1);
			ret.x = (p1.x + p2.x) / 2;
			ret.y = (p1.y + p2.y) / 2;
		}
		return ret;
	}

	public String getAdditionalAttributes() {
		Vector<String> tmp = new Vector<String>();
		// tmp.add(beginQualifier);
		// tmp.add(endQualifier);
		// tmp.add(beginArrow);
		// tmp.add(endArrow);
		// tmp.add(beginMultiplicity);
		// tmp.add(endMultiplicity);
		// tmp.add(beginRole);
		// tmp.add(endRole);
		// tmp.add(lineType);

		/*
		 * tmp.add(""+this.getX()); tmp.add(""+this.getY());
		 * tmp.add(""+this.getWidth()); tmp.add(""+this.getHeight());
		 */

		for (int i = 0; i < this.getLinePoints().size(); i++) {
			Point p = this.getLinePoints().elementAt(i);
			String s1 = "" + p.x;
			String s2 = "" + p.y;
			tmp.add(s1);
			tmp.add(s2);
		}

		String ret = Constants.composeStrings(tmp, Constants.DELIMITER_ADDITIONAL_ATTRIBUTES);
		return ret;
	}

	public void setHiddenState(String s) {
		Vector<String> tmp = Constants.decomposeStringsIncludingEmptyStrings(s, Constants.DELIMITER_FIELDS);
		// 9 attributes, 4 for start/end point, 4 for x,y,w,h
		if (tmp.size() < 17) throw new RuntimeException("UMLet: Error in  Relation.setHiddenState(), state value = "+s);

		beginQualifier = tmp.elementAt(0);
		endQualifier = tmp.elementAt(1);
		beginArrow = tmp.elementAt(2);
		endArrow = tmp.elementAt(3);
		beginMultiplicity = tmp.elementAt(4);
		endMultiplicity = tmp.elementAt(5);
		beginRole = tmp.elementAt(6);
		endRole = tmp.elementAt(7);
		lineType = tmp.elementAt(8);

		int x = Integer.parseInt(tmp.elementAt(9));
		int y = Integer.parseInt(tmp.elementAt(10));
		int w = Integer.parseInt(tmp.elementAt(11));
		int h = Integer.parseInt(tmp.elementAt(12));
		this.setBounds(x, y, w, h);

		for (int i = 13; i < tmp.size(); i = i + 2) {
			int xx = Integer.parseInt(tmp.elementAt(i));
			int yy = Integer.parseInt(tmp.elementAt(i + 1));
			Point p = new Point(xx, yy);
			this.getLinePoints().add(p);
		}
	}

	public void setAdditionalAttributes(String s) {
		Vector<String> tmp = Constants.decomposeStringsIncludingEmptyStrings(s,	Constants.DELIMITER_ADDITIONAL_ATTRIBUTES);
		// 9 attributes, 4 for start/end point, 4 for x,y,w,h
		/*
		 * if (tmp.size()<17) throw new RuntimeException("UMLet: Error in
		 * Relation.setHiddenState(), state value = "+s);
		 * 
		 * beginQualifier=(String)tmp.elementAt(0);
		 * endQualifier=(String)tmp.elementAt(1);
		 * beginArrow=(String)tmp.elementAt(2);
		 * endArrow=(String)tmp.elementAt(3);
		 * beginMultiplicity=(String)tmp.elementAt(4);
		 * endMultiplicity=(String)tmp.elementAt(5);
		 * beginRole=(String)tmp.elementAt(6); endRole=(String)tmp.elementAt(7);
		 * lineType=(String)tmp.elementAt(8);
		 * 
		 * int x=Integer.parseInt((String)tmp.elementAt(9)); int
		 * y=Integer.parseInt((String)tmp.elementAt(10)); int
		 * w=Integer.parseInt((String)tmp.elementAt(11)); int
		 * h=Integer.parseInt((String)tmp.elementAt(12));
		 * this.setBounds(x,y,w,h);
		 */

		for (int i = 0; i < tmp.size(); i = i + 2) {
			int xx = Integer.parseInt(tmp.elementAt(i));
			int yy = Integer.parseInt(tmp.elementAt(i + 1));
			Point p = new Point(xx, yy);
			this.getLinePoints().add(p);
		}
	}

public void setState(String state) {
		beginQualifier = "";
		endQualifier = "";
		beginArrow = "";
		endArrow = "";
		beginMultiplicity = "";
		endMultiplicity = "";
		beginRole = "";
		endRole = "";
		lineType = "-";
		eerRelDir = "";
		// G.Mueller.Start
		middleArrow = "";
		beginPort ="";
		endPort = "";
		//G.Mueller. End
		this.setStrings(null);

		_state = state;

		Vector<String> tmp = Constants.decomposeStrings(state, "\n");

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.startsWith("q1=") & s.length() > 3) {
				beginQualifier = s.substring(3, s.length());
			} else if (s.startsWith("q2=") & s.length() > 3) {
				endQualifier = s.substring(3, s.length());
			} else if (s.startsWith("m1=") & s.length() > 3) {
				beginMultiplicity = s.substring(3, s.length());
			} else if (s.startsWith("m2=") & s.length() > 3) {
				endMultiplicity = s.substring(3, s.length());
			} else if (s.startsWith("r1=") & s.length() > 3) {
				beginRole = s.substring(3, s.length());
			} else if (s.startsWith("r2=") & s.length() > 3) {
				endRole = s.substring(3, s.length());
			} else if (s.startsWith("p1=") & s.length() > 3) {
				beginPort = s.substring(3, s.length());
			} else if (s.startsWith("p2=") & s.length() > 3) {
				endPort = s.substring(3, s.length());
			} else if (s.startsWith("lt=") & s.length() > 3) {

				// Mueller G. Beginn
				
				csdStartText = "";
				csdEndText = "";
				
				// ***
				
				if (s.indexOf("<[") >= 0) {
					beginArrow = "compStart";
					if(s.length() > 6) { 
						csdStartText = getCSDText(s)[0];
						s = s.replace("<[" + csdStartText + "]", "<[]");
					}
				}
				
				
				if (s.indexOf("]>") >= 0) {
					endArrow = "compEnd";
					if(s.length() > 6) {
						csdEndText = getCSDText(s)[1];
						s = s.replace("[" + csdEndText + "]>", "[]>");
					}	
				}
					
				if (s.indexOf("]<") >= 0) {
					beginArrow = beginArrow + "del";
				}
				
				if (s.indexOf(">[") >= 0) {
					endArrow = endArrow + "del";
				}
				
				// Mueller G. End
				
				if(s.indexOf(">>>>>") >= 0) { //LME_A
					endArrow = "<<<";
				} else if (s.indexOf(">>>>") >= 0) {
					endArrow = "X";
				} else if (s.indexOf(">>>") >= 0) {
					endArrow = "x";
				} else if (s.indexOf(">>") >= 0) {
					endArrow = "<<";
				} else if (s.indexOf(">") >= 0) {
					if (endArrow.equals("")) endArrow = "<";
				}
				
				if(s.indexOf("<<<<<") >= 0) { //LME: filled arrow head
					beginArrow = "<<<";
				} else if (s.indexOf("<<<<") >= 0) {
					beginArrow = "X";
				} else if (s.indexOf("<<<") >= 0) {
					beginArrow = "x";
				} else if (s.indexOf("<<") >= 0) {
					beginArrow = "<<";
				} else if (s.indexOf("<") >= 0) {
					if (beginArrow.equals("")) beginArrow = "<";
				}
				
				if (s.indexOf("<EER>") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER1";
				} else if (s.indexOf("<EER") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER2";
				} else if (s.indexOf("EER>") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER3";
				} else if (s.indexOf("EER") >= 0) {
					beginArrow = "";
					endArrow = "";
					eerRelDir = "EER_SUBCLASS";
				}

				// A.Mueller Beginn
				clientServer = "";

				// ***
				
				if (s.indexOf("(()") >= 0) {
					//beginArrow = "";	G.Mueller
					clientServer = "provideRequire";
				} else if (s.indexOf("())") >= 0) {
					//endArrow = ""; G.Mueller
					clientServer = "requireProvide";
				}
				
				
				if (s.indexOf("<(+)") >= 0) {
					beginArrow = "packageStart";
					clientServer = " ";
				}
				else if (s.indexOf("<()") >= 0) {
					clientServer = "start"; // used for setting the startpoint
											// nonstickable
					beginArrow = "require";
				}else if (s.indexOf("<(") >= 0) {
						clientServer = "start"; // used for setting the
												// startpoint
													// not stickable
						beginArrow = "provide";
				
				} else if (s.indexOf("<x") >= 0) {
					beginArrow = "n";
				}
					
				if (s.indexOf("(+)>") >= 0) {
					endArrow = "packageEnd";
					clientServer = " ";
				}
				else if (s.indexOf("()>") >= 0) {
					clientServer = "end"; // used for setting the endpoint
											// nonstickable
					endArrow = "require";
				} else if (s.indexOf(")>") >= 0) {
					clientServer = "end"; // used for setting the endpoint
											// nonstickable
					endArrow = "provide";
				} else if (s.indexOf("x>") >= 0) {
					endArrow = "n";
				}
				// A.Mueller End
					// Mueller G. End
				

				// Mueller G. Start
				
				if (s.indexOf(">()") >= 0 && clientServer.equals("")) {
					middleArrow = "delegationArrowRight";
					if (endArrow.equals("<")) endArrow = "";
				}
				else if (s.indexOf("()<") >= 0 && clientServer.equals("")) {
					middleArrow = "delegationArrowLeft";
					if (beginArrow.equals("<")) beginArrow = "";
				}
				else if (s.indexOf("()") >= 0 && clientServer.equals("")) {
					middleArrow = "delegation";
				}
				else if (s.indexOf("(") >= 0  && clientServer.equals("")) {
					middleArrow = "delegationStart";
					lineType = "-.";
				}
				else if (s.indexOf(")") >= 0  && clientServer.equals("")) {
					middleArrow = "delegationEnd";
					lineType = ".-";
				}
				// G.Mueller: LineTyp check here:
				
				if (s.indexOf(".") >= 0 & s.indexOf("-") >= s.indexOf(".")) {
					lineType = ".-";
				}
				else if (s.indexOf("-") >= 0 & s.indexOf(".") >= s.indexOf("-")) {
					lineType = "-.";
				}
				else if (s.indexOf(".") >= 0) {
					lineType = ".";
				}
				else if (s.indexOf("-") >= 0) {
					lineType = "-";
				}
				
				// Mueller G. End

			} else {
				this.getStrings().add(s);
			}
		}
	}	// Created objects have no sideeffects
	// Only exception: no point is outside shape
	// At least 2 points must be provided
	public static Vector<Point> getIntersectingLineSegment(Area r,
			Vector<Point> points) {
		Vector<Point> ret = new Vector<Point>();

		// If no segment found, take last two points
		Point pp_end = points.elementAt(points.size() - 1);
		Point pp_start = points.elementAt(points.size() - 2);

		for (int i = 1; i < points.size(); i++) {
			pp_end = points.elementAt(i);
			if (!r.contains(pp_end)) {
				// End point of intersecting line found
				pp_start = points.elementAt(i - 1);

				ret.add(pp_start);
				ret.add(pp_end);
				return ret;
			}
		}

		ret.add(pp_start);
		ret.add(pp_end);
		return ret;
	}

	public static Point moveNextTo(Area rFixed, Rectangle rMovable,
			Point pStart, Point pEnd) {
		// These ints can simply be added to line
		int centerDiffX = (int) -rMovable.getWidth() / 2;
		int centerDiffY = (int) -rMovable.getHeight() / 2;

		int vectorX = pEnd.x - pStart.x;
		int vectorY = pEnd.y - pStart.y;

		int startx = pStart.x;
		int starty = pStart.y;
		int endx = pEnd.x;
		int endy = pEnd.y;

		for (int i = 0;; i++) {
			endx += vectorX;
			endy += vectorY;
			rMovable.setLocation(endx + centerDiffX, endy + centerDiffY);
			if (!rFixed.intersects(rMovable))
				break;
		}

		int newx = 0;
		int newy = 0;
		for (int i = 0; i < 10; i++) {
			newx = (endx + startx) / 2;
			newy = (endy + starty) / 2;
			rMovable.setLocation(newx + centerDiffX, newy + centerDiffY);
			if (rFixed.intersects(rMovable)) {
				startx = newx;
				starty = newy;
			} else {
				endx = newx;
				endy = newy;
			}
		}

		Point ret = new Point(newx + centerDiffX, newy + centerDiffY);
		return ret;
	}

	public static boolean lineUp(Vector<Rectangle> shapes,
			Vector<Point> points, int hotspotx, int hotspoty) {
		// Remove point with the same coordinates
		for (int i = points.size() - 1; i > 0; i--) {
			Point p1 = points.elementAt(i);
			Point p2 = points.elementAt(i - 1);
			if (p1.x == p2.x & p1.y == p2.y)
				points.removeElementAt(i);
		}
		if (points.size() <= 1)
			return false;

		if (shapes.size() <= 1)
			return true;

		// Vector ret=new Vector();

		// Rectangle rFixed;
		Rectangle rMovable;
		Area tmpArea = new Area();
		for (int i = 0; i < shapes.size() - 1; i++) {
			Rectangle r = shapes.elementAt(i);
			if (i == 0) { // The hotspot of the first element is set
				Point p = points.elementAt(0);
				r.setLocation(p.x - hotspotx, p.y - hotspoty);
			}
			Area a = new Area(r);
			tmpArea.add(a);

			// rFixed=(Rectangle)shapes.elementAt(i);
			rMovable = shapes.elementAt(i + 1);

			/*
			 * if (i==0) { // The hotspot of the first element is set Point
			 * p=(Point)points.elementAt(0);
			 * rFixed.setLocation(p.x-hotspotx,p.y-hotspoty); }
			 */

			Vector<Point> tmp = getIntersectingLineSegment(tmpArea, points);
			Point startIntersectingLine = tmp.elementAt(0);
			Point endIntersectingLine = tmp.elementAt(1);

			Point res = moveNextTo(tmpArea, rMovable, startIntersectingLine,
					endIntersectingLine);
			// ret.add(res);

			if (rMovable instanceof Arrow) {
				Arrow arrow = (Arrow) rMovable;

				Point diffA = new Point(-startIntersectingLine.x
						+ endIntersectingLine.x, -startIntersectingLine.y
						+ endIntersectingLine.y);
				Point diffB1 = new Point(diffA.y, -diffA.x);
				Point diffB2 = new Point(-diffB1.x, -diffB1.y);
				Point a1 = new Point(2 * diffA.x + diffB1.x, 2 * diffA.y
						+ diffB1.y);
				Point a2 = new Point(2 * diffA.x + diffB2.x, 2 * diffA.y
						+ diffB2.y);

				a1 = Constants.normalize(a1, Constants.getFontsize());
				a2 = Constants.normalize(a2, Constants.getFontsize());

				arrow.setArrowEndA(a1);
				arrow.setArrowEndB(a2);

				// A.Mueller start
				if (arrow.getString().equals("n")) {
					// this is pretty much the same as above, but it
					// was hard to work out what it does so here it
					// is repeated with better names and some
					// comments. I only made the original vector longer and
					// increased the pixelsize(fontsize)
					// to get the point further towards the center.
					Point start = startIntersectingLine;
					Point end = endIntersectingLine;
					// vectorA is the vector between the two points which is the
					// line between the points...
					Point vectorA = new Point(-start.x + end.x, -start.y
							+ end.y);
					// vector down is a vector standing 90 degrees on the line,
					// vector up is the same in the opposite direction..
					Point vectorDown = new Point(vectorA.y, -vectorA.x);
					Point vectorUp = new Point(-vectorDown.x, -vectorDown.y);
					Point newA1 = new Point(4 * vectorA.x + vectorDown.x, 4
							* vectorA.y + vectorDown.y);
					Point newA2 = new Point(4 * vectorA.x + vectorUp.x, 4
							* vectorA.y + diffB2.y);

					// this calculates the proportion of the two dimensions of
					// the point compared to each other
					// (which means propX + propY = 1) and multiplies it with
					// the second parameter...
					newA1 = Constants.normalize(newA1,
							Constants.getFontsize() * 2);
					newA2 = Constants.normalize(newA2,
							Constants.getFontsize() * 2);

					arrow.setCrossEndA(newA1);
					arrow.setCrossEndB(newA2);
				} else if (arrow.getString().equals("require")) {
					Point start = startIntersectingLine;
					Point end = endIntersectingLine;
					Point upperLeft = new Point();
					Point bottomDown = new Point();
					if (start.getX() > end.getX()) {
						upperLeft = new Point(0, -20 / 2);
						bottomDown = new Point(20, 20 / 2);

					} else if (start.getX() < end.getX()) {
						upperLeft = new Point(-20, -20 / 2);
						bottomDown = new Point(0, 20 / 2);
					} else if (start.getX() == end.getX()) {
						if (start.getY() < end.getY()) {
							upperLeft = new Point(-20 / 2, -20);
							bottomDown = new Point(20 / 2, 0);
						} else if (start.getY() > end.getY()) {
							upperLeft = new Point(-20 / 2, 0);
							bottomDown = new Point(20 / 2, 20);
						}
					}
					arrow.setCrossEndA(upperLeft);
					arrow.setCrossEndB(bottomDown);

				} else if (arrow.getString().equals("provide")) {
					Point start = startIntersectingLine;
					Point end = endIntersectingLine;
					Point upperLeft = new Point();
					Point bottomDown = new Point();
					if (start.getX() > end.getX()) {
						upperLeft = new Point(0, -30 / 2);
						bottomDown = new Point(30, 30 / 2);
						arrow.setArcStart(90);
						arrow.setArcEnd(180);

					} else if (start.getX() < end.getX()) {
						upperLeft = new Point(-30, -30 / 2);
						bottomDown = new Point(0, 30 / 2);
						arrow.setArcStart(90);
						arrow.setArcEnd(-180);
					} else if (start.getX() == end.getX()) {
						if (start.getY() < end.getY()) {
							upperLeft = new Point(-30 / 2, -30);
							bottomDown = new Point(30 / 2, 0);
							arrow.setArcStart(0);
							arrow.setArcEnd(-180);
						} else if (start.getY() > end.getY()) {
							upperLeft = new Point(-30 / 2, 0);
							bottomDown = new Point(30 / 2, 30);
							arrow.setArcStart(0);
							arrow.setArcEnd(180);
						}
					}
					arrow.setCrossEndA(upperLeft);
					arrow.setCrossEndB(bottomDown);

					// A.Mueller end
				}

			}

			// ATTENTION: this Recangle will become the rFixed in the next loop
			rMovable.setLocation(res);
		}

		return true;
	}

	public boolean isOnLine(int i) {
		if (i - 1 >= 0 & i + 1 < getLinePoints().size()) {
			Point x1 = getLinePoints().elementAt(i - 1);
			Point x2 = getLinePoints().elementAt(i + 1);
			Point p = getLinePoints().elementAt(i);
			if (pyth(p, x1) + pyth(p, x2) < pyth(x1, x2) + 5)
				return true;
		}
		return false;
	}

	public int getWhereToInsert(Point p) {
		for (int i = 0; i < getLinePoints().size() - 1; i++) {
			Point x1 = getLinePoints().elementAt(i);
			Point x2 = getLinePoints().elementAt(i + 1);
			if (pyth(p, x1) + pyth(p, x2) < pyth(x1, x2) + 5)
				return i + 1;
		}
		return -1;
	}

	public int getLinePoint(Point p) {
		for (int i = 0; i < getLinePoints().size(); i++) {
			Point x = getLinePoints().elementAt(i);
			if (pyth(p, x) < 15)
				return i;
		}
		return -1;
	}

	private int pyth(Point x1, Point x2) {
		int a = x1.x - x2.x;
		int b = x1.y - x2.y;
		return (int) Math.sqrt(a * a + b * b);
	}

	private <T> Vector<T> flipVector(Vector<T> v) {
		Vector<T> ret = new Vector<T>();
		for (int i = v.size() - 1; i >= 0; i--) {
			ret.add(v.elementAt(i));
		}
		return ret;
	}

	public boolean contains(Point p) {
		for (int i = 0; i < getLinePoints().size() - 1; i++) {
			Point x1 = getLinePoints().elementAt(i);
			Point x2 = getLinePoints().elementAt(i + 1);
			if (pyth(p, x1) + pyth(p, x2) < pyth(x1, x2) + 5)
				return true;
		}
		for (int i = 0; i < getLinePoints().size(); i++) {
			Point x = getLinePoints().elementAt(i);
			if (pyth(p, x) < 11)
				return true;
		}
		return false;
	}

	public boolean contains(int x, int y) {
		return contains(new Point(x, y));
	}

	private Vector<Point> _points;

	public Vector<Point> getLinePoints() {
		if (_points == null) {
			_points = new Vector<Point>();
		}
		return _points;
	}

	public void setLinePoints(Vector<Point> v) {
		_points = v;
	}

	public Entity CloneFromMe() {
		Relation c = new Relation();

		c.setState(this.getPanelAttributes());
		c.setAdditionalAttributes(this.getAdditionalAttributes());

		c.setVisible(true);
		c.setBounds(this.getBounds());
		return c;
	}

	public Relation() {
		super();
	}

	public Relation(String s) {
		super(s);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2); // Just to set anti-aliasing, even if no text
		// operations occur

		Vector<Rectangle> startShapes = new Vector<Rectangle>();
		Vector<Rectangle> endShapes = new Vector<Rectangle>();

		startShapes.add(new NoShape());
		endShapes.add(new NoShape());
		
		if (beginQualifier != null && beginQualifier.length() > 0) {
			TextLayout tl = new TextLayout(beginQualifier, Constants.getFont(),
					Constants.getFRC(g2));
			Qualifier q = new Qualifier(beginQualifier, 0, 0, (int) tl
					.getBounds().getWidth()
					+ Constants.getFontsize() * 2, (int) tl.getBounds()
					.getHeight()
					+ Constants.getFontsize() / 2);
			startShapes.add(q);
		}
		if (endQualifier != null && endQualifier.length() > 0) {
			TextLayout tl = new TextLayout(endQualifier, Constants.getFont(),
					Constants.getFRC(g2));
			Qualifier q = new Qualifier(endQualifier, 0, 0, (int) tl
					.getBounds().getWidth()
					+ Constants.getFontsize() * 2, (int) tl.getBounds()
					.getHeight()
					+ Constants.getFontsize() / 2);
			endShapes.add(q);
		}
		if (beginArrow != null && beginArrow.length() > 0) {
			Arrow a = new Arrow(beginArrow);
			startShapes.add(a);
		}
		if (endArrow != null && endArrow.length() > 0) {
			Arrow a = new Arrow(endArrow);
			endShapes.add(a);
		}
		if (beginMultiplicity != null && beginMultiplicity.length() > 0) {
			EmptyShape e = new EmptyShape();
			startShapes.add(e);
			TextLayout tl = new TextLayout(beginMultiplicity, Constants
					.getFont(), Constants.getFRC(g2));
			Multiplicity m = new Multiplicity(beginMultiplicity, 0, 0, (int) tl
					.getBounds().getWidth(), (int) tl.getBounds().getHeight());
			startShapes.add(m);
		}
		if (endMultiplicity != null && endMultiplicity.length() > 0) {
			EmptyShape e = new EmptyShape();
			endShapes.add(e);
			TextLayout tl = new TextLayout(endMultiplicity,
					Constants.getFont(), Constants.getFRC(g2));
			Multiplicity m = new Multiplicity(endMultiplicity, 0, 0, (int) tl
					.getBounds().getWidth(), (int) tl.getBounds().getHeight());
			endShapes.add(m);
		}
		if (beginRole != null && beginRole.length() > 0) {
			EmptyShape e = new EmptyShape();
			startShapes.add(e);
			// A.Mueller start
			// calculating the width if we break lines...

			int position = 0;
			int lineBreaks = 0;
			double broadestText = Constants.getPixelWidth(g2, beginRole);
			while (position != 1) {
				int positionNew = beginRole.indexOf("\\\\", position);
				if (position == 0 && positionNew != -1)
					broadestText = 0;

				if (positionNew != -1) {

					broadestText = Math.max(broadestText, Constants
							.getPixelWidth(g2, beginRole.substring(position,
									positionNew)));
					if (beginRole.lastIndexOf("\\\\") + 2 != beginRole.length())
						broadestText = Math.max(broadestText, Constants
								.getPixelWidth(g2, beginRole.substring(
										beginRole.lastIndexOf("\\\\") + 2,
										beginRole.length())));
					lineBreaks++;
				}

				position = positionNew + 2;
			}
			Role r = new Role(beginRole, 0, 0, (int) broadestText, lineBreaks
					* Constants.getFontsize() + (lineBreaks + 2)
					* Constants.getDistTextToText());

			// <OLDCODE>
			/*
			 * TextLayout tl = new TextLayout(beginRole, Constants.getFont(),
			 * Constants.getFRC(g2)); Role r = new Role(beginRole, 0, 0, (int)
			 * tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
			 */
			// </OLDCODE>
			// A.Mueller end
			startShapes.add(r);
		}
		if (endRole != null && endRole.length() > 0) {
			EmptyShape e = new EmptyShape();
			endShapes.add(e);
			// A.Mueller start
			// calculating the width if we break lines...
			int position = 0;
			int lineBreaks = 0;
			double broadestText = Constants.getPixelWidth(g2, endRole);
			while (position != 1) {
				int positionNew = endRole.indexOf("\\\\", position);
				if (position == 0 && positionNew != -1)
					broadestText = 0;

				if (positionNew != -1) {

					broadestText = Math.max(broadestText, Constants
							.getPixelWidth(g2, endRole.substring(position,
									positionNew)));
					if (endRole.lastIndexOf("\\\\") + 2 != endRole.length())
						broadestText = Math.max(broadestText, Constants
								.getPixelWidth(g2, endRole.substring(endRole
										.lastIndexOf("\\\\") + 2, endRole
										.length())));
					lineBreaks++;
				}

				position = positionNew + 2;
			}
			Role r = new Role(endRole, 0, 0, (int) broadestText, lineBreaks
					* Constants.getFontsize() + (lineBreaks + 2)
					* Constants.getDistTextToText());

			// <OLDCODE>
			/*
			 * TextLayout tl = new TextLayout(endRole, Constants.getFont(),
			 * Constants.getFRC(g2)); Role r = new Role(endRole, 0, 0, (int)
			 * tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
			 */
			// </OLDCODE>
			// A.Mueller end
			endShapes.add(r);
		}
		//	G.Mueller start
		if (beginPort != null && beginPort.length() > 0) {
			
			EmptyShape e = new EmptyShape();
			startShapes.add(e);
			TextLayout tl = new TextLayout(beginPort, Constants.getFont(), Constants.getFRC(g2));
			Port p = new Port(beginPort, 0, 0, (int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
			
			startShapes.add(p);
	
		}
		if (endPort != null && endPort.length() > 0) {
			EmptyShape e = new EmptyShape();
			endShapes.add(e);
			TextLayout tl = new TextLayout(endPort, Constants.getFont(), Constants.getFRC(g2));
			Port p = new Port(endPort, 0, 0, (int) tl.getBounds().getWidth(), (int) tl.getBounds().getHeight());
			endShapes.add(p);
		}
		//G.Mueller end

		//******************************************************************
		
		Vector<Point> startPoints = new Vector<Point>(this.getLinePoints());
		Vector<Point> endPoints = flipVector(startPoints);

		boolean a = lineUp(startShapes, startPoints, 0, 0);
		boolean b = lineUp(endShapes, endPoints, 0, 0);

		if (a == false || b == false) {
			return;
		}
		
		//	G.Mueller change begin
		
		if (lineType.equals("-."))
			g2.setStroke(Constants.getStroke(0, 1));
		else if (lineType.equals(".-"))
			g2.setStroke(Constants.getStroke(1, 1));
		else if (lineType.equals("-"))
			g2.setStroke(Constants.getStroke(0, 1));
		else if (lineType.equals(".")) 
			g2.setStroke(Constants.getStroke(1, 1));
		
		for (int i = 0; i < getLinePoints().size() - 1; i++) {

			if (i == Math.floor((getLinePoints().size() - 1) / 2)) {
				
				Point p1 = getLinePoints().elementAt(i);
				Point p2 = getLinePoints().elementAt(i + 1);
				// G.Mueller start
				Point pm = new Point(p1.x - (p1.x - p2.x) / 2, p1.y - (p1.y - p2.y) / 2);
				g2.drawLine(p1.x, p1.y, pm.x, pm.y);
				if (lineType.equals("-.")) g2.setStroke(Constants.getStroke(1, 1));
				if (lineType.equals(".-")) g2.setStroke(Constants.getStroke(0, 1));
				g2.drawLine(pm.x, pm.y, p2.x, p2.y);
				//g2.drawLine(p1.x, p1.y, p2.x, p2.y);
				// G. Mueller end
				
				// ##########################################################################################
				// ##########################################################################################
				if (eerRelDir.indexOf("EER_SUBCLASS") >= 0) {
					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);
					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y
							- (px1.y - px2.y) / 2);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Constants.getAngle(px1.x, px1.y, px2.x,
							px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);
					g2.setColor(_activeColor);
					g2.setStroke(Constants.getStroke(0, 2));
					g2.drawArc(mitte.x, mitte.y - 10, 20, 20, 90, 180);
					g2.setStroke(Constants.getStroke(0, 1));
					g2.setTransform(at);

				} else if (eerRelDir.indexOf("EER") >= 0) {
					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);
					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y
							- (px1.y - px2.y) / 2);
					int recSize = 20;
					Point r1 = new Point(mitte.x, mitte.y - recSize);
					Point r2 = new Point(mitte.x + recSize, mitte.y);
					Point r3 = new Point(mitte.x, mitte.y + recSize);
					Point r4 = new Point(mitte.x - recSize, mitte.y);

					Polygon po1 = new Polygon();
					po1.addPoint(r1.x, r1.y);
					po1.addPoint(r2.x, r2.y);
					po1.addPoint(r3.x, r3.y);

					Polygon po2 = new Polygon();
					po2.addPoint(r1.x, r1.y);
					po2.addPoint(r3.x, r3.y);
					po2.addPoint(r4.x, r4.y);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Constants.getAngle(px1.x, px1.y, px2.x,
							px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);

					if (eerRelDir.equals("EER1")) {
						g2.setColor(_activeColor);
						g2.fillPolygon(po1);
						g2.fillPolygon(po2);
					} else if (eerRelDir.equals("EER2")) {
						g2.setColor(_fillColor);
						g2.fillPolygon(po2);
						g2.setColor(_activeColor);
						g2.fillPolygon(po1);
					} else if (eerRelDir.equals("EER3")) {
						g2.setColor(_fillColor);
						g2.fillPolygon(po1);
						g2.setColor(_activeColor);
						g2.fillPolygon(po2);
					}
					g2.setColor(_activeColor);
					g2.draw(po1);
					g2.draw(po2);
					g2.setTransform(at);
				}

				// A.Mueller start
				else if (clientServer != null
						&& clientServer.indexOf("rovide") >= 0) {
					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);
					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y
							- (px1.y - px2.y) / 2);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Constants.getAngle(px1.x, px1.y, px2.x,
							px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);

					Point outerArc = new Point(mitte.x - 15, mitte.y - 15);
					Point innerCircle = new Point();

					g2.setColor(Color.white);
					g2.fillOval(outerArc.x, outerArc.y, 30, 30);
					g2.setColor(_activeColor);
					g2.setStroke(Constants.getStroke(0, 1));

					if (clientServer.equals("provideRequire")) {
						g2.drawArc(outerArc.x, outerArc.y, 30, 30, 90, 180);
						innerCircle = new Point(mitte.x - 5, mitte.y - 10);
					} else if (clientServer.equals("requireProvide")) {
						g2.drawArc(outerArc.x, outerArc.y, 30, 30, 90, -180);

						innerCircle = new Point(mitte.x - 15, mitte.y - 10);
					}

					g2.drawOval(innerCircle.x, innerCircle.y, 20, 20);
					g2.setTransform(at);
				}
				// A.Mueller end
				// G.Mueller start
				else if (middleArrow.startsWith("delegation")){
						
					Point px1 = getLinePoints().elementAt(i);
					Point px2 = getLinePoints().elementAt(i + 1);
					Point mitte = new Point(px1.x - (px1.x - px2.x) / 2, px1.y
							- (px1.y - px2.y) / 2);

					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = mitte.x;
					int cy = mitte.y;
					double winkel = Constants.getAngle(px1.x, px1.y, px2.x,
							px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);

					Point circle = new Point(mitte.x - 15, mitte.y - 15);
					if (middleArrow.equals("delegation")) {
						g2.setColor(Color.white);
						g2.fillOval(circle.x + 5, circle.y + 5, 20, 20);
						g2.setColor(_activeColor);
						g2.setStroke(Constants.getStroke(0, 1));
						g2.drawOval(circle.x + 5, circle.y + 5, 20, 20);
					}
					if (middleArrow.startsWith("delegationArrow")) {
						g2.setStroke(Constants.getStroke(0, 1));												
						if (middleArrow.equals("delegationArrowRight")) {
							g2.drawLine(circle.x + 5, circle.y + 15, circle.x - 5, circle.y + 9);
							g2.drawLine(circle.x + 5, circle.y + 15, circle.x - 5, circle.y + 20);
						}
						if (middleArrow.equals("delegationArrowLeft")) {
							g2.drawLine(circle.x + 25, circle.y + 15, circle.x + 35, circle.y + 9);
							g2.drawLine(circle.x + 25, circle.y + 15, circle.x + 35, circle.y + 20);
						}
						
						g2.setColor(Color.white);
						g2.fillOval(circle.x + 5, circle.y + 5, 20, 20);
						g2.setColor(_activeColor);
						g2.setStroke(Constants.getStroke(0, 1));
						g2.drawOval(circle.x + 5, circle.y + 5, 20, 20);
					
					}
					if (middleArrow.equals("delegationStart")) {
						g2.setColor(Color.white);
						g2.fillArc(circle.x, circle.y, 30, 30, 90, 180);
						g2.setColor(_activeColor);
						g2.setStroke(Constants.getStroke(0, 1));
						g2.drawArc(circle.x, circle.y, 30, 30, 90, 180);
					}
					if (middleArrow.equals("delegationEnd")) {
						g2.setColor(Color.white);
						g2.fillArc(circle.x, circle.y, 30, 30, 90, -180);
						g2.setColor(_activeColor);
						g2.setStroke(Constants.getStroke(0, 1));
						g2.drawArc(circle.x, circle.y, 30, 30, 90, -180);					
					}
			
				
					g2.setTransform(at);
				
				}
							
				// G.Mueller end
				
				// ##########################################################################################
				// ##########################################################################################
				if (lineType.equals("-.")) g2.setStroke(Constants.getStroke(1, 1));
				if (lineType.equals(".-")) g2.setStroke(Constants.getStroke(0, 1));
				
			} else {
				
				Point p1 = getLinePoints().elementAt(i);
				Point p2 = getLinePoints().elementAt(i + 1);
				g2.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}

		g2.setStroke(Constants.getStroke(0, 1));
		if (_selected) {
			for (int i = 0; i < getLinePoints().size(); i++) {
				Point p = getLinePoints().elementAt(i);
				g2.drawOval(p.x - 10, p.y - 10, 20, 20);
			}
		}

		Vector<Rectangle> tmp = new Vector<Rectangle>(startShapes);
		tmp.addAll(endShapes);
		for (int i = 0; i < tmp.size(); i++) {
			Rectangle r = tmp.elementAt(i);
			if (r instanceof Qualifier) {

				Qualifier q = (Qualifier) r;		
				
				// begin B. Buckl
				g.setColor(_fillColor);
				g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),
						(int) r.getHeight());
				g.setColor(_activeColor);
				// end
				g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),
						(int) r.getHeight());
				Constants.write(g2, q.getString(), (int) r.getX()
						+ Constants.getFontsize(), (int) r.getY()
						+ Constants.getFontsize(), false);
			
			} else if (r instanceof Arrow) {
				Arrow arrow = (Arrow) r;
				// A.Mueller Start
				if (!arrow.getString().equals("n")
						&& !arrow.getString().equals("require")
						&& !arrow.getString().equals("provide")
						&& !arrow.getString().startsWith("package")
						&& !arrow.getString().startsWith("comp")) {
					// A.Mueller end
					g2.drawLine((int) arrow.getX(), (int) arrow.getY(),
							(int) arrow.getX() + (int) arrow.getArrowEndA().x,
							(int) arrow.getY() + (int) arrow.getArrowEndA().y);
					g2.drawLine((int) arrow.getX(), (int) arrow.getY(),
							(int) arrow.getX() + (int) arrow.getArrowEndB().x,
							(int) arrow.getY() + (int) arrow.getArrowEndB().y);
					// A.Mueller start
				}
				// A.Mueller end

				// System.out.println(arrow.getString());
				
				if (arrow.getString().equals("<<<")) { //LME
					//filled arrow head
					int[] ax = new int[3];
					int[] ay = new int[3];
					ax[0] = (int) arrow.getX();
					ax[1] = (int) arrow.getX() + (int) arrow.getArrowEndA().x;
					ax[2] = (int) arrow.getX() + (int) arrow.getArrowEndB().x;
					ay[0] = (int) arrow.getY();
					ay[1] = (int) arrow.getY() + (int) arrow.getArrowEndA().y;
					ay[2] = (int) arrow.getY() + (int) arrow.getArrowEndB().y;
					Polygon myPg = new Polygon(ax, ay, 3);
					g2.fill(myPg);
					g2.draw(myPg);
				} else if (arrow.getString().equals("<<")) {
					// begin B. Buckl
					int[] ax = new int[3];
					int[] ay = new int[3];
					ax[0] = (int) arrow.getX();
					ax[1] = (int) arrow.getX() + (int) arrow.getArrowEndA().x;
					ax[2] = (int) arrow.getX() + (int) arrow.getArrowEndB().x;
					ay[0] = (int) arrow.getY();
					ay[1] = (int) arrow.getY() + (int) arrow.getArrowEndA().y;
					ay[2] = (int) arrow.getY() + (int) arrow.getArrowEndB().y;
					Polygon myPg = new Polygon(ax, ay, 3);
					g2.setColor(_fillColor);
					g2.fill(myPg);
					g2.setColor(_activeColor);
					g2.draw(myPg);

					// g2.drawLine((int)arrow.getX()+(int)arrow.getArrowEndA().x,
					// (int)arrow.getY()+(int)arrow.getArrowEndA().y,
					// (int)arrow.getX()+(int)arrow.getArrowEndB().x,
					// (int)arrow.getY()+(int)arrow.getArrowEndB().y);
				} // end B. Buckl
				else if (arrow.getString().equals("x")) {
					int[] ax = new int[4];
					int[] ay = new int[4];
					ax[0] = (int) arrow.getX();
					ay[0] = (int) arrow.getY();
					ax[1] = (int) arrow.getX() + (int) arrow.getArrowEndA().x;
					ay[1] = (int) arrow.getY() + (int) arrow.getArrowEndA().y;
					ax[3] = (int) arrow.getX() + (int) arrow.getArrowEndB().x;
					ay[3] = (int) arrow.getY() + (int) arrow.getArrowEndB().y;

					ax[2] = -(int) arrow.getX() + ax[1] + ax[3];
					ay[2] = -(int) arrow.getY() + ay[1] + ay[3];

					// begin B. Buckl
					Polygon myPg = new Polygon(ax, ay, 4);
					g2.setColor(_fillColor);
					g2.fill(myPg);
					g2.setColor(_activeColor);
					g2.draw(myPg);
					// end B. Buckl
				} else if (arrow.getString().equals("X")) {
					int[] ax = new int[4];
					int[] ay = new int[4];
					ax[0] = (int) arrow.getX();
					ay[0] = (int) arrow.getY();
					ax[1] = (int) arrow.getX() + (int) arrow.getArrowEndA().x;
					ay[1] = (int) arrow.getY() + (int) arrow.getArrowEndA().y;
					ax[3] = (int) arrow.getX() + (int) arrow.getArrowEndB().x;
					ay[3] = (int) arrow.getY() + (int) arrow.getArrowEndB().y;

					ax[2] = -(int) arrow.getX() + ax[1] + ax[3];
					ay[2] = -(int) arrow.getY() + ay[1] + ay[3];

					g2.fill(new Polygon(ax, ay, 4));
				}
				// A.Mueller Begin
				else if (arrow.getString().equals("n")) {
					Point a1 = arrow.getCrossEndA();
					Point a2 = arrow.getCrossEndB();
					g2.drawLine((int) (arrow.getX() + arrow.getArrowEndA().x),
							(int) (arrow.getY() + arrow.getArrowEndA().y),
							(int) (arrow.getX() + a2.x),
							(int) (arrow.getY() + a2.y));
					g2.drawLine((int) (arrow.getX() + arrow.getArrowEndB().x),
							(int) (arrow.getY() + arrow.getArrowEndB().y),
							(int) (arrow.getX() + a1.x),
							(int) (arrow.getY() + a1.y));

				} else if (arrow.getString().equals("require")) {

					int width = arrow.getCrossEndB().x - arrow.getCrossEndA().x;
					int height = arrow.getCrossEndB().y
							- arrow.getCrossEndA().y;
					g2.drawOval((int) arrow.getX() + arrow.getCrossEndA().x,
							(int) arrow.getY() + arrow.getCrossEndA().y, width,
							height);

				} else if (arrow.getString().equals("provide")) {
					int width = arrow.getCrossEndB().x - arrow.getCrossEndA().x;
					int height = arrow.getCrossEndB().y
							- arrow.getCrossEndA().y;
					g2.drawArc((int) arrow.getX() + arrow.getCrossEndA().x,
							(int) arrow.getY() + arrow.getCrossEndA().y, width,
							height, arrow.getArcStart(), arrow.getArcEnd());
					// A.Mueller End
					// G.Mueller Start
				} else if (arrow.getString().startsWith("package")) {
					Point px1;
					Point px2;
					if (arrow.getString().equals("packageStart")) {
						px1 = getStartPoint();
						px2 = getLinePoints().elementAt(1);
					} else {
						px1 = getEndPoint();
						px2 = getLinePoints().elementAt(
								getLinePoints().size() - 2);
					}
					AffineTransform at = g2.getTransform();
					AffineTransform at2 = (AffineTransform) at.clone();
					int cx = px1.x;
					int cy = px1.y;
					double winkel = Constants.getAngle(px1.x, px1.y, px2.x,
							px2.y);
					at2.rotate(winkel, cx, cy);
					g2.setTransform(at2);
					g2.setColor(_fillColor);
					g2.fillOval(px1.x, px1.y - 10, 20, 20);
					g2.setColor(_activeColor);
					g2.drawOval(px1.x, px1.y - 10, 20, 20);
					g2.drawLine(px1.x + 10, px1.y - 5, px1.x + 10, px1.y + 5);
					g2.drawLine(px1.x + 15, px1.y, px1.x + 5, px1.y);
					g2.setTransform(at);

					// ***
//					 Wirrer G. Start
                } else if (arrow.getString().startsWith("fill_poly")) {
                    
                    Point px1;
                    Point px2;
                    if (beginArrow.startsWith("fill_poly_start")) {
                        px1 = getStartPoint();
                        px2 = getLinePoints().elementAt(1);
                        AffineTransform at = g2.getTransform();
                        AffineTransform at2 = (AffineTransform) at.clone();
                        double winkel = Constants.getAngle(px1.x, px1.y, px2.x, px2.y);
                        at2.rotate(winkel,px1.x,px1.y);
                        g2.setTransform(at2);
                        int[] x_cord = {px1.x,px1.x+13,px1.x+13};
                        int[] y_cord = {px1.y,px1.y-7,px1.y+7};
                        Polygon x = new Polygon(x_cord,y_cord,3);
                        g2.fillPolygon(x);
                        g2.setTransform(at);
                    }
                    if (endArrow.startsWith("fill_poly_end")) {
                        px1 = getEndPoint();
                        px2 = getLinePoints().elementAt(getLinePoints().size() - 2);
                        AffineTransform at = g2.getTransform();
                        AffineTransform at2 = (AffineTransform) at.clone();
                        double winkel = Constants.getAngle(px2.x, px2.y, px1.x, px1.y);
                        at2.rotate(winkel,px1.x,px1.y);
                        g2.setTransform(at2);
                        int[] x_cord = {px1.x,px1.x-13,px1.x-13};
                        int[] y_cord = {px1.y,px1.y-7,px1.y+7};
                        Polygon x = new Polygon(x_cord,y_cord,3);
                        g2.fillPolygon(x);
                        g2.setTransform(at);
                    }
                // Wirrer G. End    
				} else if (arrow.getString().startsWith("comp")) {

					
					Point px1;
					Point px2;
					int s;

					//if (beginCSDArrow.equals("compStart")) {
					if (beginArrow.startsWith("compStart")) {
					
						s = 20;
						
						if (!csdStartText.equals("")) 
								s = Constants.getPixelWidth(g2, csdStartText);
						if (s < 25) s=20;
						
						px1 = getStartPoint();
						px2 = getLinePoints().elementAt(1);
						g2.setColor(_fillColor);
						g2.fillRect((int)(px1.x - s/2), (int)(px1.y - s/2), s, s);
						g2.setColor(_activeColor);
						g2.drawRect((int)(px1.x - s/2), (int)(px1.y - s/2), s, s);
						g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
						if (csdStartText.equals(">")) {
							int[] tmpX = { px1.x - 5, px1.x + 5, px1.x - 5 };
							int[] tmpY = { px1.y - 5, px1.y, px1.y + 5 };
							g2.fillPolygon(tmpX, tmpY, 3);
						} else if (csdStartText.equals("<")) {
							int[] tmpX = { px1.x + 5, px1.x - 5, px1.x + 5 };
							int[] tmpY = { px1.y - 5, px1.y, px1.y + 5 };
							g2.fillPolygon(tmpX, tmpY, 3);
						} else if (csdStartText.equals("v")) {
							int[] tmpX = { px1.x - 5, px1.x, px1.x + 5 };
							int[] tmpY = { px1.y - 5, px1.y + 5, px1.y - 5 };
							g2.fillPolygon(tmpX, tmpY, 3);
						} else if (csdStartText.equals("^")) {
							int[] tmpX = { px1.x - 5, px1.x, px1.x + 5 };
							int[] tmpY = { px1.y + 5, px1.y - 5, px1.y + 5 };
							g2.fillPolygon(tmpX, tmpY, 3);	
						} else if (csdStartText.equals("=")) {
							g2.drawLine(px1.x - 6, px1.y - 2, px1.x + 6, px1.y - 2);
							g2.drawLine(px1.x + 6, px1.y - 2, px1.x + 1, px1.y - 6);
							g2.drawLine(px1.x - 6, px1.y + 2, px1.x + 6, px1.y + 2);
							g2.drawLine(px1.x - 6, px1.y + 2, px1.x - 1, px1.y + 6);
						} else {
							if (!csdStartText.equals(""))
								Constants.write(g2, csdStartText, px1.x+3,
										px1.y + 5, true);
						}
						
						if (beginArrow.equals("compStartdel")) {
							AffineTransform at = g2.getTransform();
							AffineTransform at2 = (AffineTransform) at.clone();
							int cx = px1.x;
							int cy = px1.y;
							double winkel = Constants.getAngle(px1.x, px1.y, px2.x,
									px2.y);
							at2.rotate(winkel, cx, cy);
							g2.setTransform(at2);
							g2.drawLine((int)(px1.x + s/2+2), px1.y, (int)(px1.x + s/2 + 12), px1.y - 6);
							g2.drawLine((int)(px1.x + s/2+2), px1.y, (int)(px1.x + s/2 + 12), px1.y + 6);
							g2.setTransform(at);
						}
		
					
					}
					//if (endCSDArrow.equals("compEnd")) {
					if (endArrow.startsWith("compEnd")) {

						
						s = 20;
						
						if (!csdEndText.equals("")) 
							s = Constants.getPixelWidth(g2, csdEndText);
						if (s < 20) s=20;
						
						px1 = getEndPoint();
						px2 = getLinePoints().elementAt(
								getLinePoints().size() - 2);
						g2.setColor(_fillColor);
						g2.fillRect((int)(px1.x - s/2), (int)(px1.y - s/2), s, s);
						g2.setColor(_activeColor);
						g2.drawRect((int)(px1.x - s/2), (int)(px1.y - s/2), s, s);
						g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
						if (csdEndText.equals(">")) {
							int[] tmpX = { px1.x - 5, px1.x + 5, px1.x - 5 };
							int[] tmpY = { px1.y - 5, px1.y, px1.y + 5 };
							g2.fillPolygon(tmpX, tmpY, 3);
						} else if (csdEndText.equals("<")) {
							int[] tmpX = { px1.x + 5, px1.x - 5, px1.x + 5 };
							int[] tmpY = { px1.y - 5, px1.y, px1.y + 5 };
							g2.fillPolygon(tmpX, tmpY, 3);
						} else if (csdEndText.equals("v")) {
							int[] tmpX = { px1.x - 5, px1.x, px1.x + 5 };
							int[] tmpY = { px1.y - 5, px1.y + 5, px1.y - 5 };
							g2.fillPolygon(tmpX, tmpY, 3);
						} else if (csdEndText.equals("^")) {
							int[] tmpX = { px1.x - 5, px1.x, px1.x + 5 };
							int[] tmpY = { px1.y + 5, px1.y - 5, px1.y + 5 };
							g2.fillPolygon(tmpX, tmpY, 3);	
						} else if (csdEndText.equals("=")) {
							g2.drawLine(px1.x - 6, px1.y - 2, px1.x + 6, px1.y - 2);
							g2.drawLine(px1.x + 6, px1.y - 2, px1.x + 1, px1.y - 6);
							g2.drawLine(px1.x - 6, px1.y + 2, px1.x + 6, px1.y + 2);
							g2.drawLine(px1.x - 6, px1.y + 2, px1.x - 1, px1.y + 6);
						} else {
							if (!csdEndText.equals(""))
								Constants.write(g2, csdEndText, px1.x+3,
										px1.y + 5, true);
						}
						
						if (endArrow.equals("compEnddel")) {
							AffineTransform at = g2.getTransform();
							AffineTransform at2 = (AffineTransform) at.clone();
							int cx = px1.x;
							int cy = px1.y;
							double winkel = Constants.getAngle(px1.x, px1.y, px2.x,
									px2.y);
							at2.rotate(winkel, cx, cy);
							g2.setTransform(at2);
							g2.drawLine((int)(px1.x + s/2+2), px1.y, (int)(px1.x + s/2 + 12), px1.y - 6);
							g2.drawLine((int)(px1.x + s/2+2), px1.y, (int)(px1.x + s/2 + 12), px1.y + 6);
							g2.setTransform(at);
						}

						
					}
					
					g2.setFont(Constants.getFont()); //reset font
					
				}
				// G.Mueller End

			} else if (r instanceof Multiplicity) {
				Multiplicity m = (Multiplicity) r;
				// g.drawRect((int)r.getX(), (int)r.getY(), (int)r.getWidth(),
				// (int)r.getHeight());
				Constants.write(g2, m.getString(), (int) r.getX(), (int) r
						.getY()
						+ Constants.getFontsize()
						+ 2
						* Constants.getDistTextToText(), false); // B. Buckl
				// added
				// +2*Constants.getDistTextToText()
			} else if (r instanceof Role) {
				Role role = (Role) r;
				// A.Mueller start
				boolean underline = false;
				boolean italic = false;
				String str = role.getString();
				if (str.startsWith("/") && str.endsWith("/") && str.length() > 1) {
					italic = true;
					str = str.substring(1, str.length() - 1);
				}
				if (str.startsWith("_") && str.endsWith("_") && str.length() > 1) {
					underline = true;
					str = str.substring(1, str.length() - 1);
				}

				int position = 0;
				int y = 4 * Constants.getDistLineToText();
				while (position != -1) {
					position = str.indexOf("\\\\");

					if (position != -1) {
						String s = str.substring(0, position);
						if (italic)
							s = "/" + s + "/";
						if (underline)
							s = "_" + s + "_";

						Constants.write(g2, s, (int) r.getX(), (int) r.getY()
								+ y, false);

						y = y + Constants.getFontsize();
						str = str.substring(position + 2, str.length());
					} else {
						if (italic) str = "/"+str+"/";
						if (underline) str = "_"+str+"_";
						Constants.write(g2, str, (int) r.getX(), (int) r.getY()+ y, false);
						
					}
						
				}

				// <OLDCODE>
				/*
				 * Constants.write(g2, role.getString(), (int) r.getX(), (int) r
				 * .getY() + Constants.getFontsize() + 2
				 * Constants.getDistTextToText(), false); // B. Buckl // added //
				 * +2*Constants.getDistTextToText()
				 */
				// </OLDCODE>
				// A.Mueller end
			// G.Mueller Start
			} else if (r instanceof Port) {
				Port p = (Port) r;

				Constants.write(g2, p.getString(), 
						(int) (r.getX()),
						(int) (r.getY()), false);

				
			}
			//G.Mueller end
		}

		if (this.getStrings() != null) {
			if (this.getStrings().size() > 0) {
				Point start = this.getCenterOfLine();
				int yPos = start.y - Constants.getDistTextToText(); // B. Buckl
				// added
				// -Constants.getDistTextToText()
				int xPos = start.x;
				for (int i = 0; i < getStrings().size(); i++) {
					String s = this.getStrings().elementAt(i);

					// A.Mueller Begin...
					if (s.startsWith(">") || s.endsWith(">")
							|| s.startsWith("<") || s.endsWith("<")) {
						// starts or ends with an arrow, check if it is the only
						// one..
						if ((s.indexOf(">") == s.lastIndexOf(">") && s
								.indexOf(">") != -1)
								|| (s.indexOf("<") == s.lastIndexOf("<") && s
										.indexOf("<") != -1)) {
							// decide where and what to draw...
							int fontHeight = g2.getFontMetrics(
									Constants.getFont()).getHeight()
									- g2.getFontMetrics(Constants.getFont())
											.getDescent()
									- g2.getFontMetrics(Constants.getFont())
											.getLeading();
							fontHeight = fontHeight / 3 * 2;
							if (s.endsWith(">")) {

								s = s.substring(0, s.length() - 1);
								int fontWidth = Constants.getPixelWidth(g2, s);
								xPos = xPos - (fontHeight + 4) / 2;
								int startDrawX = xPos + fontWidth / 2 + 4;
								Polygon temp = new Polygon();
								temp.addPoint(startDrawX, yPos);
								temp.addPoint(startDrawX, yPos - fontHeight);
								temp.addPoint(startDrawX + fontHeight - 1, yPos
										- fontHeight / 2);
								g2.fillPolygon(temp);

							} else if (s.endsWith("<")) {
								s = s.substring(0, s.length() - 1);
								int fontWidth = Constants.getPixelWidth(g2, s);
								xPos = xPos - (fontHeight + 4) / 2;
								int startDrawX = xPos + fontWidth / 2 + 4;
								Polygon temp = new Polygon();
								temp
										.addPoint(startDrawX + fontHeight - 1,
												yPos);
								temp.addPoint(startDrawX + fontHeight - 1, yPos
										- fontHeight);
								temp
										.addPoint(startDrawX, yPos - fontHeight
												/ 2);
								g2.fillPolygon(temp);
							} else if (s.startsWith(">")) {
								s = s.substring(1, s.length());
								int fontWidth = Constants.getPixelWidth(g2, s);
								xPos = xPos + (fontHeight + 4) / 2;
								int startDrawX = xPos - fontWidth / 2 - 4;
								Polygon temp = new Polygon();
								temp
										.addPoint(startDrawX - fontHeight + 1,
												yPos);
								temp.addPoint(startDrawX - fontHeight + 1, yPos
										- fontHeight);
								temp
										.addPoint(startDrawX, yPos - fontHeight
												/ 2);
								g2.fillPolygon(temp);
							} else if (s.startsWith("<")) {

								s = s.substring(1, s.length());
								int fontWidth = Constants.getPixelWidth(g2, s);
								xPos = xPos + (fontHeight + 4) / 2;
								int startDrawX = xPos - fontWidth / 2 - 4;
								Polygon temp = new Polygon();
								temp.addPoint(startDrawX, yPos);
								temp.addPoint(startDrawX, yPos - fontHeight);
								temp.addPoint(startDrawX - fontHeight + 1, yPos
										- fontHeight / 2);
								g2.fillPolygon(temp);

							}

						}

					}
					// A.Mueller end...

					Constants.write(g2, s, xPos, yPos, true);
					yPos += Constants.getFontsize();
					yPos += Constants.getDistTextToText();
				}
			}
		}

		Vector<Point> criticalPoints = new Vector<Point>();
		for (int i = 1; i < startShapes.size(); i++) {
			Rectangle r = startShapes.elementAt(i);
			Point p1 = new Point((int) r.getX() - 2, (int) r.getY() - 2);
			Point p2 = new Point((int) r.getX() + (int) r.getWidth() + 2,
					(int) r.getY() + (int) r.getHeight() + 2);
			criticalPoints.add(p1);
			criticalPoints.add(p2);
		}
		for (int i = 1; i < endShapes.size(); i++) {
			Rectangle r = endShapes.elementAt(i);
			Point p1 = new Point((int) r.getX() - 2, (int) r.getY() - 2);
			Point p2 = new Point((int) r.getX() + (int) r.getWidth() + 2,
					(int) r.getY() + (int) r.getHeight() + 2);
			criticalPoints.add(p1);
			criticalPoints.add(p2);
		}
		if (this.getStrings() != null) {
			if (this.getStrings().size() > 0) {
				Point start = this.getCenterOfLine();
				int yPos = start.y;
				int xPos = start.x;
				for (int i = 0; i < getStrings().size(); i++) {
					String s = this.getStrings().elementAt(i);
					int width = Constants.getPixelWidth(g2, s);
					criticalPoints.add(new Point(xPos - width / 2 - 20, yPos
							- Constants.getFontsize() - 20));
					criticalPoints.add(new Point(xPos + width / 2 + 20,
							yPos + 20));
					yPos += Constants.getFontsize();
					yPos += Constants.getDistTextToText();
				}
			}
		}

		{
			int minx = 99999;
			int maxx = -99999;
			int miny = 99999;
			int maxy = -99999;
			for (int i = 0; i < getLinePoints().size(); i++) {
				Point p = getLinePoints().elementAt(i);
				minx = Math.min(minx, p.x);
				miny = Math.min(miny, p.y);
				maxx = Math.max(maxx, p.x);
				maxy = Math.max(maxy, p.y);

				// TEST
				minx = Math.min(minx, p.x - 20);
				miny = Math.min(miny, p.y - 20);
				maxx = Math.max(maxx, p.x + 20);
				maxy = Math.max(maxy, p.y + 20);
			}
			for (int i = 0; i < criticalPoints.size(); i++) {
				Point p = criticalPoints.elementAt(i);
				minx = Math.min(minx, p.x);
				miny = Math.min(miny, p.y);
				maxx = Math.max(maxx, p.x);
				maxy = Math.max(maxy, p.y);
			}

			int diffminx = minx;
			int diffminy = miny;
			int diffmaxx = maxx - getWidth();
			int diffmaxy = maxy - getHeight();

			if (minx != 0) {
				Controller.getInstance().executeCommandWithoutUndo(
						new Resize(this, 8, diffminx, 0));
			}
			if (maxx != 0) {
				Controller.getInstance().executeCommandWithoutUndo(
						new Resize(this, 2, diffmaxx, 0));
			}
			if (miny != 0) {
				Controller.getInstance().executeCommandWithoutUndo(
						new Resize(this, 1, 0, diffminy));
			}
			if (maxy != 0) {
				Controller.getInstance().executeCommandWithoutUndo(
						new Resize(this, 4, 0, diffmaxy));
			}
			if (minx != 0 | miny != 0) {
				for (int i = 0; i < getLinePoints().size(); i++) {
					Point p = getLinePoints().elementAt(i);
					p.x += -minx;
					p.y += -miny;
				}
			}
		}
	}

	private Point getStartPoint() {
		Point ret = this.getLinePoints().elementAt(0);
		return ret;
	}

	private Point getEndPoint() {
		Point ret = this.getLinePoints().elementAt(
				this.getLinePoints().size() - 1);
		return ret;
	}

	public Point getAbsoluteCoorStart() {
		Point ret = new Point();
		ret.x = this.getX() + this.getStartPoint().x;
		ret.y = this.getY() + this.getStartPoint().y;
		return ret;
	}

	public Point getAbsoluteCoorEnd() {
		Point ret = new Point();
		ret.x = this.getX() + this.getEndPoint().x;
		ret.y = this.getY() + this.getEndPoint().y;
		return ret;
	}

	public int getConnectedLinePoint(Entity e, int action) {
		int ret1 = -1;
		int ret2 = -1;

		Point p = this.getStartPoint();
		Point abs = new Point((int) (this.getX() + p.getX()), (int) (this
				.getY() + p.getY()));
		int where = e.doesCoordinateAppearToBeConnectedToMe(abs);
		if ((where > 0) && ((where & action) > 0))
			ret1 = 0;

		p = this.getEndPoint();
		abs = new Point((int) (this.getX() + p.getX()), (int) (this.getY() + p
				.getY()));
		where = e.doesCoordinateAppearToBeConnectedToMe(abs);
		if ((where > 0) && ((where & action) > 0))
			ret2 = this.getLinePoints().size() - 1;

		// A.Mueller start
		if (clientServer != null && clientServer.equals("start"))
			ret1 = -1;
		if (clientServer != null && clientServer.equals("end"))
			ret2 = -1;
		// A.Mueller end

		ret1++;
		ret2++;

		return ret1 + 1000 * ret2;
	}
	
	// G.Mueller start
	public String[] getCSDText(String str) {	// for the Composite Structure Diagram Text

		String tmp[] = new String[4];
		int to = 0;
		int from = 0;
		tmp[0] = " ";
		tmp[1] = " ";
		tmp[2] = " ";
		tmp[3] = " ";

		if (str.length() > 3) {

			// if (str.indexOf("<[") >=3) tmp[2] =
			// str.substring(3,str.indexOf("<["));
			// if (str.lastIndexOf("[") >=3 && str.lastIndexOf("[")-1 !=
			// str.lastIndexOf("<[")) tmp[3] = str.substring(str.indexOf("[",
			// str.length()));

			from = str.indexOf("<[") + 2;
			if (from >= 2)
				to = str.indexOf("]");
			if (from >= 2 && to >= 0 && from < to)
				tmp[0] = str.substring(from, to);
			
			from = str.indexOf("[", to) + 1;
			if (from >= 1)
				to = str.indexOf("]>", to);
			if (from >= 1 && to >= 0 && from < to)
				tmp[1] = str.substring(from, to);

		}

		return tmp;

	}
	
	//G.Mueller end

	public StickingPolygon getStickingBorder() { // LME
		return null;
	}
}
