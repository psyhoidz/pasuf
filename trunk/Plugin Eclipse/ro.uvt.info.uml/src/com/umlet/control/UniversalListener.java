package com.umlet.control;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.umlet.element.base.*;
import com.umlet.element.base.detail.*;
import com.umlet.control.io.CustomElementLoader; //LME
import java.io.*;

/*import Move;*/

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


public class UniversalListener extends ComponentAdapter
	implements WindowListener, MouseListener, MouseMotionListener, ActionListener, KeyListener, ItemListener, MenuListener, FocusListener {
    
	private Cursor lrCursor=new Cursor(Cursor.E_RESIZE_CURSOR);
	private Cursor tbCursor=new Cursor(Cursor.N_RESIZE_CURSOR);
	private Cursor nwCursor=new Cursor(Cursor.NW_RESIZE_CURSOR);
	private Cursor neCursor=new Cursor(Cursor.NE_RESIZE_CURSOR);
	private Cursor handCursor=new Cursor(Cursor.HAND_CURSOR);
	private Cursor defCursor=new Cursor(Cursor.DEFAULT_CURSOR);
	private Cursor crossCursor=new Cursor(Cursor.CROSSHAIR_CURSOR);

	private boolean IS_DRAGGING=false;
	private boolean IS_DRAGGING_LINEPOINT=false;
	private boolean IS_RESIZING=false;
	private int LINEPOINT=-1;
	private int RESIZE_DIRECTION=0;
	private boolean IS_FIRST_DRAGGING_OVER=false;
	private boolean IS_FIRST_RESIZE_OVER=false;
	private Vector<Command> ALL_MOVE_COMMANDS=null;
	private Vector<MoveLinePoint> ALL_RESIZE_COMMANDS=null;
	private boolean DESELECT_MULTISEL=false;
	private boolean IS_SELECTORFRAME_ACTIVE=false;
  
	private boolean _newCustomElementDialog_CANCELLED=false;

	private static UniversalListener _instance;
	public static UniversalListener getInstance() {
		if (_instance==null) _instance=new UniversalListener();
		return _instance;
	}

	int _xOffset, _yOffset;

  //[UB & LI]: added combobox listener that switches between the palettes
  public void itemStateChanged(ItemEvent evt) {
  	if(evt.getSource().equals(Umlet.getInstance().getPaletteList())) { //LME: palette list event
  		if (evt.getStateChange() == ItemEvent.SELECTED) {
  			JPanel cards = Umlet.getInstance().getCards();
  			CardLayout cl = (CardLayout)(cards.getLayout());
  			cl.show(cards, (String)evt.getItem());
  		}
  	} else if(evt.getSource().equals(Umlet.getInstance().getElementsList())) { //LME: custom elements list event
  		if (evt.getStateChange() == ItemEvent.SELECTED) {
  			String eName=(String)evt.getItem();
  			String homePath=Umlet.getInstance().getHomePath(); //LME
  			if(eName!=null && !eName.equals("")) {
  				if(eName.equals("NEW ELEMENT")) { //create new element
  					String fileName=null;
  					try {
	  					String elementsPath=homePath+Umlet.CUSTOM_ELEMENTS_PATH;
	  					
	  					File templateFile = new File(elementsPath+"template_element.txt");
	  					if(!templateFile.exists()) { //file missing?
	  						Umlet.getInstance().showCompilerMessage("File not found: "+elementsPath+templateFile.getName());
	  						return;
	  					}
	  					JTextField tf = Umlet.getInstance().showNewCustomElementDialog();
	  					if(!_newCustomElementDialog_CANCELLED && tf!=null) fileName=tf.getText();
	  					if(fileName!=null && !fileName.equals("")) {
  				  			fileName=fileName.substring(0,1).toUpperCase()+fileName.substring(1); //make first char uppercase
	  						if(fileName.indexOf(".java")<0) fileName+=".java";
	  						OutputStream oStream = new FileOutputStream(elementsPath+fileName);
		  					OutputStreamWriter osw = new OutputStreamWriter(oStream);
		  					
		  					FileReader fr = new FileReader(templateFile);
		  					BufferedReader br = new BufferedReader(fr);
		  					String line=null;
		  					while ((line = br.readLine())!=null) {
		  						String classNameToken = "<!CLASSNAME!>";
		  						if(line.indexOf(classNameToken)>=0) {
		  							osw.write(line.substring(0,line.indexOf(classNameToken))+fileName.substring(0,fileName.indexOf(".java"))+line.substring(line.indexOf(classNameToken)+classNameToken.length(),line.length()));
		  						} else osw.write(line);
		  						osw.write("\n");
		  					}
		  					br.close();
		  					osw.close();
	  					}
	  				}
	  				catch (IOException ioe) {
	  					System.err.println("UMLet->UniversalListener->itemStateChanged:"+ioe);
	  				}
	  				if(fileName!=null) { //file selection dialog may be canceled
	  					Umlet.getInstance().getElementsList().insertItemAt(fileName,2);
	  					Umlet.getInstance().getElementsList().setSelectedIndex(2); //select and place new element
	  				}
  					
  				} else { //place element
  					Umlet.getInstance().doLoadClass(homePath+Umlet.CUSTOM_ELEMENTS_PATH+eName,eName.substring(0,eName.indexOf(".java")));
  				}
  			}
  		}
  	}
  }  
  
  public void actionPerformed(ActionEvent e) {
  	if(e.getSource() instanceof JMenuItem) { //LME3 distinguish between menu and button events
	    JMenuItem b=(JMenuItem)e.getSource();
	    if (b.getText()=="Delete") {
	        Vector<Entity> v=Selector.getInstance().getSelectedEntitiesOnPanel();
	        if (v.size()>0) {
	          Controller.getInstance().executeCommand(new RemoveElement(v));
	        }
	    } else if (b.getText()=="Undo") {
	      Selector.getInstance().deselectAll();
	      Controller.getInstance().undo();
	    } else if (b.getText()=="Redo") {
	      Controller.getInstance().redo();
//		    A.Mueller start
	    } else if (b.getText().equals("Find")) {
            Umlet.getInstance().getSearchField().setText("");
            Umlet.getInstance().getSearchField().setVisible(true);
            JPanel p = (JPanel)Umlet.getInstance().getSearchField().getParent();
            p.doLayout();
            Umlet.getInstance().getSearchField().requestFocus();
	    } else if (b.getText().equals("Group")) {
	    	Controller.getInstance().executeCommand(new AddGroup());
	    } else if (b.getText().equals("Ungroup")) {
	    	Vector<Entity> entities = Selector.getInstance().getSelectedEntities();
			for (int i=0; i<entities.size(); i++)
			{
				if (entities.get(i) instanceof Group) Controller.getInstance().executeCommand(new Ungroup((Group)entities.get(i)));
			}
	    } else if (b.getText().equals("Toggle Transparency")) {
	    	Vector<Entity> entities = Selector.getInstance().getSelectedEntities();
            boolean toSet=true;
            for (int i=0; i<entities.size();i++){
                if (entities.get(i).getTransparentSelection()){
                    toSet=false;
                    break;
                }
            }
			for (int i=0; i<entities.size(); i++) {
				entities.get(i).setTransparentSelection(toSet);
			}
			//A.Mueller end
	    } else if (b.getText()=="Open...") {
	      Umlet.getInstance().doOpen(null); //no filename predefined: use file selector
	    } else if (b.getText()=="Save") {
	      Umlet.getInstance().doSave();
	    } else if (b.getText()=="Save as...") {
	      Umlet.getInstance().doSaveAs();
	    } else if (b.getText()=="New") {
	      Umlet.getInstance().doNew();
	    } else if (b.getText()=="Save as JPG...") {
	      Selector.getInstance().deselectAll();
	      Umlet.getInstance().doSaveAsJPG(null);
	    } else if (b.getText()=="Save as SVG...") {
	      Selector.getInstance().deselectAll();
	      Umlet.getInstance().doSaveAsSvg(null);
	    } else if (b.getText()=="Save as EPS...") { //LME
	      Selector.getInstance().deselectAll();
	      Umlet.getInstance().doSaveAsEps(null);
	    } else if (b.getText()=="Save as PDF...") {
	      Selector.getInstance().deselectAll();
	      Umlet.getInstance().doSaveAsPdf(null);
	    } else if(b.getText()=="Realign to grid") { //LME
	    	Vector v=Umlet.getInstance().getAllEntities();
	    	for(int i=0;i<v.size();i++) {
	    		System.out.println("Element["+i+"]: x:"+((Entity)v.elementAt(i)).getX()+"_y:"+((Entity)v.elementAt(i)).getY());
	    		Entity entity = (Entity)v.elementAt(i);
	    		int x=entity.getX();
				int y=entity.getY();
	    		entity.setLocation(x-x%Umlet.getInstance().getMainUnit(),y-y%Umlet.getInstance().getMainUnit());
	    	}
	    } else if((b.getText()=="6")||(b.getText()=="8")||(b.getText()=="10")||(b.getText()=="12")) {
	    	int fontSize = Integer.parseInt(b.getText());
	    	Vector selElements = Selector.getInstance().getSelectedEntities();
	    	if((selElements!=null)&&(selElements.size()>0)) {
	    		for(int i=0;i<selElements.size();i++) {
	    			Entity entity = (Entity)selElements.elementAt(i);
	    			//entity.setFontSize(fontSize);
	    			//## setFontSize() -> Constants.getFontsize()
//##########################################################
//##########################################################
//##########################################################
	    		}
	    	}
	    }
	    // Tudor - add actions for Class menu
	      else if(b.getText()=="Set Class Options") {
	    	  // TODO write the code for this
		    	
	    	  JOptionPane.showMessageDialog(null, 
	    			  "Function is not yet implemented!", 
	    			  "Error", 
	    			  JOptionPane.WARNING_MESSAGE);
	    } else if(b.getText()=="Add Atribute to Class") {
	    	// TODO write the code for this
	    	
	    	JOptionPane.showMessageDialog(Umlet.getInstance(), 
	    			  "Function is not yet implemented!", 
	    			  "Error", 
	    			  JOptionPane.WARNING_MESSAGE);
	    } else if(b.getText()=="Add Operation to Class") {
	    	// TODO write the code for this
	    	
	    	JOptionPane.showMessageDialog(Umlet.getInstance(), 
	    			  "Function is not yet implemented!", 
	    			  "Error", 
	    			  JOptionPane.WARNING_MESSAGE);
	    }
	    //
  	} else if(e.getActionCommand().equals("COMPILER_MESSAGE_DIALOG_OK")) { //LME3
  		Umlet.getInstance()._compilerMessageDialog.setVisible(false);
    //A.Mueller start
    } else if (e.getActionCommand().equals("Search")) {
        Controller.getInstance().executeCommand(new Search(Umlet.getInstance().getSearchField().getText()));
        Umlet.getInstance().getSearchField().setVisible(true);
        Umlet.getInstance().getSearchField().requestFocus();
    //A.Mueller end
  	} else if(e.getActionCommand().equals("COMPILE_BUTTON")) { //LME4
		String sourceCode=Umlet.getInstance().getSourceCodePanel().getText(); //LME3 get source from source-code-panel
		Entity entity=Umlet.getInstance().getEditedEntity();
		
		if(e!=null) {
			String oldCode = entity.getJavaSource();
			String newCode = Umlet.getInstance().getSourceCodePanel().getText();
			entity.saveJavaSource(newCode); //save tha java source code
			String[] fileNameStrArr = { //create names array for CustomElementLoader
				entity.getSourceFilePath(),
				entity.getClass().getName()
			};
			Entity newEntity = CustomElementLoader.getInstance().doLoadClass(fileNameStrArr, true, false); //compile and load the modified class
			if(newEntity != null) { //any errors while compiling/loading class?
				//update drawing panel
				Vector allEntities = Umlet.getInstance().getAllEntities();
				for(int i=0;i<allEntities.size();i++) {
					if(allEntities.elementAt(i).getClass().getName().equals(entity.getClass().getName())) {
						Entity oldInstance = (Entity)allEntities.elementAt(i);
						Umlet.getInstance().getPanel().remove(oldInstance); //delete old entity instance
						newEntity.setState(oldInstance.getPanelAttributes()); //copy panel-attributes
						newEntity.setAdditionalAttributes(oldInstance.getAdditionalAttributes());
						newEntity.setBounds(oldInstance.getX(),oldInstance.getY(),oldInstance.getWidth(),oldInstance.getHeight());
						//newEntity.reshape(oldInstance.getX(),oldInstance.getY(),oldInstance.getWidth(),oldInstance.getHeight());
						Umlet.getInstance().getPanel().add(newEntity.CloneFromMe()); //add new entity instance
					}
				}
				Umlet.getInstance().getPanel().repaint();
				
				//update palette panel
				allEntities = Umlet.getInstance().getAllPaletteEntities();
				for(int i=0;i<allEntities.size();i++) {
					if(allEntities.elementAt(i).getClass().getName().equals(entity.getClass().getName())) {
						Entity oldInstance = (Entity)allEntities.elementAt(i);
						Umlet.getInstance().getPalettePanel().remove(oldInstance);
						newEntity.setState(oldInstance.getPanelAttributes());
						newEntity.setAdditionalAttributes(oldInstance.getAdditionalAttributes());
						//newEntity.reshape(oldInstance.getX(),oldInstance.getY(),oldInstance.getWidth(),oldInstance.getHeight());
						newEntity.setBounds(oldInstance.getX(),oldInstance.getY(),oldInstance.getWidth(),oldInstance.getHeight());
						Umlet.getInstance().getPalettePanel().add(newEntity.CloneFromMe());
					}
				}
				Umlet.getInstance().getPalettePanel().repaint();
				Umlet.getInstance().setCompileCodeMenuButtonEnabled(false); //disable code panel button
			} else { //so an error has occured while compiling
				entity.saveJavaSource(oldCode); //restore old code state in java file				
			}
		}
  	} else if(e.getActionCommand().equals("NEW_CUSTOM_ELEMENT_OK")) { //LME
  		Umlet.getInstance()._newCustomElementDialog.setVisible(false);
  		_newCustomElementDialog_CANCELLED=false;
  	} else if(e.getActionCommand().equals("NEW_CUSTOM_ELEMENT_CANCEL")) { //LME
  		Umlet.getInstance()._newCustomElementDialog.setVisible(false);
  		_newCustomElementDialog_CANCELLED=true;
  	}
  }

  public void mouseClicked (MouseEvent me) {}
  public void mouseEntered (MouseEvent me) {}
  public void mousePressed (MouseEvent me) {
    
    Component c=me.getComponent();
 
    if (me.getClickCount()==2) {
      if (!(c instanceof Entity)) return;
      //Umlet.getInstance().setChanged(true);
      
      //if (me.getComponent().getParent()==Frame.getInstance().getPanel()) return; // Ignore double click on panel
      Entity tmp=(Entity)me.getComponent();
      Entity e=tmp.CloneFromMe();
      Command cmd;
      int MAIN_UNIT=Umlet.getInstance().getMainUnit();
      if (tmp.getParent()==Umlet.getInstance().getPalettePanel()) {
        Point viewp = Umlet.getInstance().getScrollPaneMain().getViewport().getViewPosition();
        cmd = new AddEntity(e, viewp.x-(viewp.x % MAIN_UNIT)+MAIN_UNIT*2, viewp.y - (viewp.y % MAIN_UNIT)+MAIN_UNIT*2);
      } else {
        cmd = new AddEntity(e, tmp.getX()+MAIN_UNIT*2, tmp.getY()+MAIN_UNIT*2);
      }
      Controller.getInstance().executeCommand(cmd);
      Selector.getInstance().singleSelect(e, true);
      Container cont=e.getParent(); cont.remove(e); cont.add(e,0);
      return;
    }

    // Otherwise..
    int mod=me.getModifiers();
    if (c==Umlet.getInstance().getPanel() || c==Umlet.getInstance().getPalettePanel()) {
      _xOffset=me.getX();
      _yOffset=me.getY();
      if ((mod & MouseEvent.CTRL_MASK)!=0) {
        SelectorFrame selframe = Umlet.getInstance().getSelectorFrame();
        selframe.setLocation(_xOffset, _yOffset);
        selframe.setSize(1,1);
        ((JComponent) c).add(selframe, 0);
        IS_SELECTORFRAME_ACTIVE=true;
      }
    } else {
      _xOffset=me.getX()+c.getX();
      _yOffset=me.getY()+c.getY();
    }

    if (c instanceof Relation) {
      Relation rel=(Relation)c;
      int where=rel.getLinePoint(new Point(me.getX(), me.getY()));
      if (where>=0) {
        IS_DRAGGING_LINEPOINT=true;
        LINEPOINT=where;
      }
    }
    if (c instanceof Entity) {
      Entity e=(Entity)c;
      int ra=e.getResizeArea(me.getX(),me.getY());
      ra=ra&e.getPossibleResizeDirections(); //LME

      if (ra!=0) {
        IS_RESIZING=true;
        RESIZE_DIRECTION=ra;
        return;
      } else {
        IS_DRAGGING=true;
        if ((mod & MouseEvent.CTRL_MASK)!=0) {
          if (e.isSelected())
            DESELECT_MULTISEL=true;
          else
            Selector.getInstance().singleSelect(e, false);
        } else {
          if (!Selector.getInstance().getSelectedEntities().contains(e))
              Selector.getInstance().singleSelect(e, true);
          if (Selector.getInstance().hasSameSelectionPattern(e, me.getX(), me.getY())) {
            Container cont=e.getParent(); cont.remove(e); cont.add(e,-1);
          } else {
            Container cont=e.getParent(); cont.remove(e); cont.add(e,0);
          }
          Selector.getInstance().setSingleSelectorInformation(e, me.getX(), me.getY());
        }
      }
    } else {
    // Otherwise
      if (!IS_SELECTORFRAME_ACTIVE) Selector.getInstance().deselectAll();
      Umlet.getInstance().setSourceCodePanelToEntity(null); //LME: clear code panel
    //Frame.getInstance().getJspMain().requestFocus();
    }
  }

  private void adjustLeftAndUpperCoorsNonNegative(Umlet.DrawPanel panel) {
    int minx=0; int miny=0;
    for (int i=0; i<panel.getComponents().length; i++) {
       Component c=panel.getComponent(i);
       int x=c.getX();
       int y=c.getY();
       if (x<minx) minx=x;
       if (y<miny) miny=y;
    }
    if (minx<0 || miny<0) {
        for (int i=0; i<panel.getComponents().length; i++) {
           JComponent c=(JComponent) panel.getComponent(i);
           c.setLocation(c.getX()-minx, c.getY()-miny);
        }
        panel.incViewPosition(-minx, -miny);
            
    }
    /*if (minx>0 && panel._incx>0) {
        int dec=minx;
        if (dec>panel._incx) dec=panel._incx;
        panel.incViewPosition(-dec, 0);
    }*/

  }
  
  public void mouseReleased (MouseEvent me) {
    if (IS_DRAGGING_LINEPOINT & LINEPOINT>=0) {
      Relation rel=(Relation)me.getComponent();
      if (rel.isOnLine(LINEPOINT)) {
        Controller.getInstance().executeCommand(new RemoveLinePoint(rel, LINEPOINT));
      }
    }
    Component c=me.getComponent();
    if (c instanceof Entity) {
        Entity e=(Entity)c;    
        int mod=me.getModifiers();
        if ((mod & MouseEvent.CTRL_MASK)!=0) {
              if (e.isSelected() && DESELECT_MULTISEL) Selector.getInstance().deselect(e);
        }
        e.drawStickingPolygon(e.getGraphics(), e.getStickingBorder()); //LME: draw sticking border
    }    
    if (IS_SELECTORFRAME_ACTIVE) {
        SelectorFrame selframe=Umlet.getInstance().getSelectorFrame();
        Container panel = selframe.getParent();
        panel.remove(selframe);
        Selector.getInstance().deselectAll();
        Selector.getInstance().multiSelect(panel, selframe.getLocation(), selframe.getSize());
        panel.repaint();
    }
    
    adjustLeftAndUpperCoorsNonNegative(Umlet.getInstance().getPanel());
    adjustLeftAndUpperCoorsNonNegative(Umlet.getInstance().getPalettePanel());
    Umlet.getInstance().getJspMain().revalidate();
    
    DESELECT_MULTISEL=false;
    IS_DRAGGING=false;
    IS_DRAGGING_LINEPOINT=false;
    IS_RESIZING=false;
    IS_FIRST_DRAGGING_OVER=false;
    IS_FIRST_RESIZE_OVER=false;
    ALL_MOVE_COMMANDS=null;
    ALL_RESIZE_COMMANDS=null;
    IS_SELECTORFRAME_ACTIVE=false;
  }

  public void mouseExited (MouseEvent e) {}

  public void mouseDragged(MouseEvent me) {
    // Get new mouse coordinates
    Component c=me.getComponent();
    if (c.getParent()==Umlet.getInstance().getPanel()) Umlet.getInstance().setChanged(true);
    
    if (IS_SELECTORFRAME_ACTIVE) {
        SelectorFrame selframe=Umlet.getInstance().getSelectorFrame();
        int newwidth=1; int newheight=1;
        if (me.getX()-selframe.getX()>1) newwidth=me.getX()-selframe.getX();
        if (me.getY()-selframe.getY()>1) newheight=me.getY()-selframe.getY();
        selframe.setSize(newwidth, newheight);
        return;
    }
    
    int xNewOffset;
    int yNewOffset;
    if (c==Umlet.getInstance().getPanel() || c==Umlet.getInstance().getPalettePanel()) {
      xNewOffset=me.getX();
      yNewOffset=me.getY();
    } else {
      xNewOffset=me.getX()+c.getX();
      yNewOffset=me.getY()+c.getY();
    }

    int MAIN_UNIT=Umlet.getInstance().getMainUnit();

    int new_x_eff=MAIN_UNIT*((xNewOffset-MAIN_UNIT/2)/MAIN_UNIT);
    int new_y_eff=MAIN_UNIT*((yNewOffset-MAIN_UNIT/2)/MAIN_UNIT);
    int old_x_eff=MAIN_UNIT*((_xOffset-MAIN_UNIT/2)/MAIN_UNIT);
    int old_y_eff=MAIN_UNIT*((_yOffset-MAIN_UNIT/2)/MAIN_UNIT);

    //delta
    int delta_x=0;
    int delta_y=0;
    if (IS_RESIZING) {
      if (RESIZE_DIRECTION==3 || RESIZE_DIRECTION==2 || RESIZE_DIRECTION==6) {
          delta_x=(c.getX()+c.getWidth())%MAIN_UNIT;
      }
      if (RESIZE_DIRECTION==12 || RESIZE_DIRECTION==4 || RESIZE_DIRECTION==6) {
          delta_y=(c.getY()+c.getHeight())%MAIN_UNIT;
      }
      if(c instanceof Entity) {
      	Entity e = (Entity)c;
      	e.drawStickingPolygon(e.getGraphics(), e.getStickingBorder());
      }
    } else if (IS_DRAGGING_LINEPOINT) {
      Relation r=(Relation)c;
      Vector<Point> tmp=r.getLinePoints();
      Point p=tmp.elementAt(LINEPOINT);
      delta_x=(r.getX()+p.x)%MAIN_UNIT;
      delta_y=(r.getY()+p.y)%MAIN_UNIT;
    }

    
    int diffx=new_x_eff-old_x_eff-delta_x;
    int diffy=new_y_eff-old_y_eff-delta_y;
    int grid_diffx = diffx%MAIN_UNIT;
    int grid_diffy = diffy%MAIN_UNIT;
    
    //if(diffx%10!=0 || diffy%10!=0)
    //    System.out.println("diffX:"+diffx+" delta_x:"+delta_x+" new_x_eff:"+new_x_eff+" old_x_eff:"+old_x_eff+" xNewOffset:"+xNewOffset);
    
    
    _xOffset=xNewOffset;
    _yOffset=yNewOffset;



    if (c instanceof Relation) {
      Relation r=(Relation)c;
      if (IS_DRAGGING_LINEPOINT & LINEPOINT>=0) {
        Controller.getInstance().executeCommand(new MoveLinePoint(r,LINEPOINT,diffx,diffy));
        return;
      }

      int where=r.getLinePoint(new Point(me.getX(), me.getY()));
      if (where>=0) {
        IS_DRAGGING_LINEPOINT=true;
        LINEPOINT=where;
        Controller.getInstance().executeCommand(new MoveLinePoint(r,where,diffx,diffy));
        return;
      } else {
        Point p=new Point(me.getX(), me.getY());
        int ins=r.getWhereToInsert(p);
        if (ins>0) {
          IS_DRAGGING_LINEPOINT=true;
          LINEPOINT=ins;
          Controller.getInstance().executeCommand(new InsertLinePoint(r, ins, me.getX(),me.getY()));
          return;
        }
      }
    }

    if (IS_DRAGGING_LINEPOINT) return;

    if (c instanceof Entity) {
      Entity e=(Entity)c;
      if (IS_DRAGGING==true) {
        DESELECT_MULTISEL=false;
          if (IS_FIRST_DRAGGING_OVER==false) {
            Vector<Entity> entitiesToBeMoved=new Vector<Entity>(Selector.getInstance().getSelectedEntities());
            //Vector allRelations=Selector.getInstance().getAllRelationsOnPanel();
    
            Vector<Move> moveCommands=new Vector<Move>();
            for (int i=0; i<entitiesToBeMoved.size(); i++) {
              Entity tmpEntity=entitiesToBeMoved.elementAt(i);
              moveCommands.add(new Move(tmpEntity, diffx, diffy));
            }
            
            Vector<MoveLinePoint> linepointCommands=new Vector<MoveLinePoint>();
           
            for (int i=0; i<entitiesToBeMoved.size(); i++) {
              Entity tmpEntity=entitiesToBeMoved.elementAt(i);
              if (tmpEntity instanceof Relation || tmpEntity.getParent()==Umlet.getInstance().getPalettePanel()) continue;
              Vector<RelationLinePoint> affectedRelationPoints=tmpEntity.getAffectedRelationLinePoints(15);
              for (int j=0; j<affectedRelationPoints.size(); j++) {
                  RelationLinePoint tmpRlp=affectedRelationPoints.elementAt(j);
                  if (entitiesToBeMoved.contains(tmpRlp.getRelation())) continue;
                linepointCommands.add(new MoveLinePoint(tmpRlp.getRelation(), tmpRlp.getLinePointId(), diffx, diffy));
              }
            }
            Vector<Command> allCommands=new Vector<Command>();
            allCommands.addAll(moveCommands);
            allCommands.addAll(linepointCommands);
            
            ALL_MOVE_COMMANDS=allCommands;
            IS_FIRST_DRAGGING_OVER=true;
          } else {
              Vector<Command> tmpVector=new Vector<Command>();
              for (int i=0; i<ALL_MOVE_COMMANDS.size(); i++) {
                  Command tmpCommand=ALL_MOVE_COMMANDS.elementAt(i);
                  if (tmpCommand instanceof Move) {
                      Move m=(Move)tmpCommand;
                      tmpVector.add(new Move(m.getEntity(), diffx, diffy));
                  } else if (tmpCommand instanceof MoveLinePoint) {
                      MoveLinePoint m=(MoveLinePoint)tmpCommand;
                      tmpVector.add(new MoveLinePoint(m.getRelation(), m.getLinePointId(), diffx, diffy));
                  }
              }
              ALL_MOVE_COMMANDS=tmpVector;
          }
        
        Controller.getInstance().executeCommand(new Macro(ALL_MOVE_COMMANDS));          

      } else if (IS_RESIZING==true) {
        if (RESIZE_DIRECTION==9 || RESIZE_DIRECTION==8 || RESIZE_DIRECTION==12)
          if ((e.getWidth()-diffx)<Constants.getFontsize()) return;
          else if (RESIZE_DIRECTION==8) diffy=0;
        if (RESIZE_DIRECTION==3 || RESIZE_DIRECTION==2 || RESIZE_DIRECTION==6)
          if ((e.getWidth()+diffx)<Constants.getFontsize()) return;
          else if (RESIZE_DIRECTION==2) diffy=0;

        if (RESIZE_DIRECTION==9 || RESIZE_DIRECTION==1 || RESIZE_DIRECTION==3)
          if ((e.getHeight()-diffy)<Constants.getFontsize()) return;
          else if (RESIZE_DIRECTION==1) diffx=0;
        if (RESIZE_DIRECTION==12 || RESIZE_DIRECTION==4 || RESIZE_DIRECTION==6)
          if ((e.getHeight()+diffy)<Constants.getFontsize()) return;
          else if (RESIZE_DIRECTION==4) diffx=0;

        Vector<Command> resizeCommands=new Vector<Command>();
        resizeCommands.add(new Resize(e,RESIZE_DIRECTION,diffx,diffy));
        if (IS_FIRST_RESIZE_OVER==false) {
          ALL_RESIZE_COMMANDS=new Vector<MoveLinePoint>();

          Vector<RelationLinePoint> affectedRelationLinePoints=e.getAffectedRelationLinePoints(RESIZE_DIRECTION);
          for (int i=0; i<affectedRelationLinePoints.size(); i++) {
              RelationLinePoint rlp=affectedRelationLinePoints.elementAt(i);
              ALL_RESIZE_COMMANDS.add(new MoveLinePoint(rlp.getRelation(), rlp.getLinePointId(), diffx, diffy));
          }
          IS_FIRST_RESIZE_OVER=true;
        } else {
          Vector<MoveLinePoint> tmpVector=new Vector<MoveLinePoint>();
          for (int i=0; i<ALL_RESIZE_COMMANDS.size(); i++) {
            MoveLinePoint m= ALL_RESIZE_COMMANDS.elementAt(i);
              tmpVector.add(new MoveLinePoint(m.getRelation(), m.getLinePointId(), diffx, diffy));
            }
            ALL_RESIZE_COMMANDS=tmpVector;
        }
        resizeCommands.addAll(ALL_RESIZE_COMMANDS);
        Controller.getInstance().executeCommand(new Macro(resizeCommands));
      }
    } else if (c instanceof JPanel) {
      Vector<Entity> v=Selector.getInstance().getAllEntitiesOnPanel((JPanel) c);
      Vector<Move> moveCommands=new Vector<Move>();
      for (int i=0; i<v.size(); i++) {
          Entity e=v.elementAt(i);
        moveCommands.add(new Move(e, diffx, diffy));
      }
      Controller.getInstance().executeCommand(new Macro(moveCommands));
    }
  }
  public void mouseMoved(MouseEvent me) {
    //Frame.getInstance().setTitle(""+me.getX()+", "+me.getY());

    Component c=me.getComponent();
    if (c instanceof Relation) {
      Relation rel=(Relation)c;
      int where=rel.getLinePoint(new Point(me.getX(), me.getY()));
      if (where>=0) {
        Umlet.getInstance().setCursor(handCursor);
      } else Umlet.getInstance().setCursor(crossCursor);
      return;
    }
    if (c instanceof Entity) {
      Entity e=(Entity)c;
      int ra=e.getResizeArea(me.getX(), me.getY());
      ra=ra&e.getPossibleResizeDirections(); //LME
      if (0!=ra) {
        if (ra==1 | ra==4)
          Umlet.getInstance().setCursor(tbCursor);
        if (ra==2 | ra==8)
          Umlet.getInstance().setCursor(lrCursor);
        if (ra==3 | ra==12)
          Umlet.getInstance().setCursor(neCursor);
        if (ra==6 | ra==9)
          Umlet.getInstance().setCursor(nwCursor);
      } else Umlet.getInstance().setCursor(handCursor);
    } else Umlet.getInstance().setCursor(defCursor);
  }

  private UniversalListener() {}

  public void keyPressed(KeyEvent e) {}
  
  public void keyReleased(KeyEvent ke) {
    Component c=ke.getComponent();
    Entity entity=Umlet.getInstance().getEditedEntity(); //LME
    if (c==Umlet.getInstance().getPropertyPanel()) {
      String s=Umlet.getInstance().getPropertyString();
      if (entity!=null)
        Controller.getInstance().executeCommand(new ChangeState(entity,entity.getPanelAttributes(),s));
      ke.consume();
    } else if(c==Umlet.getInstance().getSourceCodePanel()) { //LME4
		Umlet.getInstance().setCompileCodeMenuButtonEnabled(true); //enable code panel button

		//display "in progress" messages
		Vector pEs = Umlet.getInstance().getAllEntities(); 
		pEs.addAll(Umlet.getInstance().getAllPaletteEntities()); //collect all entities from drawing pane and the palette
		for(int i=0;i<pEs.size();i++) {
			if(pEs.elementAt(i) instanceof Entity) {
				Entity tmp=(Entity)pEs.elementAt(i);
				if(tmp.getClass().toString().equals(entity.getClass().toString())) { //???any other possibilities to check wether the objects are the same class/type 
					tmp.setInProgress(tmp.getGraphics(),true);
				}
			}
		}
		ke.consume();
    } 
  }

  public void keyTyped(KeyEvent ke) {}

  public void componentResized (ComponentEvent ce) {
    /*if (ce.getComponent()==Umlet.getInstance()) {
      System.out.println("componentResized... "+ Umlet.getInstance().getOldBounds()+":::"+Umlet.getInstance().getBounds());
      if (Umlet.getInstance().getBounds().equals(new Rectangle(0,0,0,0))) return;
      if (Umlet.getInstance().getOldBounds()==null) {
        System.out.println("componentResized: setting initial values...");
        Umlet.getInstance().getJspMain().setDividerLocation((double) 0.7);
        Umlet.getInstance().getJspRight().setDividerLocation((double) 0.6);
        Umlet.getInstance().setOldBounds(Umlet.getInstance().getBounds());
      } else {
          System.out.println("componentResized: adjusting values...");
          Rectangle rOld=Umlet.getInstance().getOldBounds();
          Rectangle rNew=Umlet.getInstance().getBounds();
          int diffX=(int)(rNew.getWidth()-rOld.getWidth());
          int diffY=(int)(rNew.getHeight()-rOld.getHeight());
          Umlet.getInstance().getJspMain().setDividerLocation(Umlet.getInstance().getJspMain().getDividerLocation()+diffX);
          Umlet.getInstance().getJspRight().setDividerLocation(Umlet.getInstance().getJspRight().getDividerLocation()+diffY);
          Umlet.getInstance().setOldBounds(rNew);
      }
    }*/
    if (ce.getComponent()==Umlet.getInstance().getJspMain() || ce.getComponent()==Umlet.getInstance().getJspRight()) {
     //System.out.println("componentResized... "+ (ce.getComponent()==Umlet.getInstance().getJspMain()?"JSPMain":"JSPRight") +" -->"+ ce.getComponent().getBounds());
    
     //LME: palette-List position
     //System.out.println("location:"+Umlet.getInstance().getPaletteList().getLocation());
     //Umlet.getInstance().getPaletteList().setLocation(Umlet.getInstance().getWidth()-Umlet.getInstance().getPaletteList().getWidth()-20,0);
     
     //Umlet.getInstance().paintAll(Umlet.getInstance().getGraphics());
     if (Umlet.getInstance().getBounds().equals(new Rectangle(0,0,0,0))) return;
     if (ce.getComponent()==Umlet.getInstance().getJspMain()) {
         if (Umlet.getInstance().getOldWidth()==-1) {
            //System.out.println("componentResized: setting initial values...");
            Umlet.getInstance().getJspMain().setDividerLocation((double) 0.7);
            Umlet.getInstance().setOldWidth(Umlet.getInstance().getBounds().width);
         } else {
             //System.out.println("componentResized: adjusting values...");
             int oldw=Umlet.getInstance().getOldWidth();
             Rectangle rNew=Umlet.getInstance().getBounds();
             int diffX=(int)(rNew.getWidth()-oldw);
             Umlet.getInstance().getJspMain().setDividerLocation(Umlet.getInstance().getJspMain().getDividerLocation()+diffX);
             Umlet.getInstance().setOldWidth(rNew.width);
         }
      } else {
        if (Umlet.getInstance().getOldHeight()==-1) {
           //System.out.println("componentResized: setting initial values...");
           Umlet.getInstance().getJspRight().setDividerLocation((double) 0.7);
           Umlet.getInstance().setOldHeight(Umlet.getInstance().getBounds().height);
        } else {
            //System.out.println("componentResized: adjusting values...");
            int oldh=Umlet.getInstance().getOldHeight();
            Rectangle rNew=Umlet.getInstance().getBounds();
            int diffY=(int)(rNew.getHeight()-oldh);
            Umlet.getInstance().getJspRight().setDividerLocation(Umlet.getInstance().getJspRight().getDividerLocation()+diffY);
            Umlet.getInstance().setOldHeight(rNew.height);
        }          
      }
    }
  }


  public void windowActivated(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowClosing(WindowEvent e) {
    if (!Umlet.isPlugggedIn()) {
        if (Umlet.getInstance().askSaveIfDirty()) {
            Umlet.getInstance().window.dispose();
            System.exit(0);
        }
    } else {
        Umlet.pluginHandler.notifyIsDirty(Umlet.getInstance().isChanged());
        if (Umlet.pluginHandler.notifyIsClosing()) {
            if (!Umlet.isEmbedded()) Umlet.getInstance().window.setVisible(false);
            Umlet.pluginHandler=null;
        }
    }
  }
  public void windowDeactivated(WindowEvent e){}
  public void windowDeiconified(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

  //A.Mueller start...
  //implement the MenuListener
  public void menuCanceled(MenuEvent e) {
//	 Auto-generated method stub
  }

  public void menuDeselected(MenuEvent e) {
    // Auto-generated method stub
  }

  public void menuSelected(MenuEvent e) {
    //first get the toggle Transparency menuItem...
    JMenu edit = (JMenu)e.getSource();
    JCheckBoxMenuItem transparentSelection= new JCheckBoxMenuItem();
    for (int i=0; i< edit.getItemCount();i++){
        if (edit.getItem(i) instanceof JCheckBoxMenuItem){
            transparentSelection=(JCheckBoxMenuItem)edit.getItem(i);
            transparentSelection.setSelected(false);
            break;
        }
    }
    //test if it should be selected...
    Vector<Entity> entities = Selector.getInstance().getSelectedEntities();
    for (int i=0; i<entities.size();i++){
        if (entities.get(i).getTransparentSelection()){
           transparentSelection.setSelected(true);
        }
    }
  }

  public void focusGained(FocusEvent evnt) {
  }

  public void focusLost(FocusEvent evnt) {
    if (evnt.getSource() instanceof JTextField) {
        Umlet.getInstance().getSearchField().setVisible(false);
        Umlet.getInstance().getSearchField().setBackground(new Color(148,172,251));
        (Umlet.getInstance().getSearchField().getParent()).doLayout();
        Umlet.getInstance().getPanel().requestFocus();
    }
  }
  //A.Mueller end
}