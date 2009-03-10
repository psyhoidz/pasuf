package com.umlet.control;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.filechooser.*;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.*;

import com.umlet.control.io.*;
import com.umlet.element.base.*;
import com.umlet.element.base.Class;
import com.umlet.element.base.Package;

import com.umlet.plugin.UmletPlugin; //LME


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

// 3.2.: 8h, neu starten
// 4.2.: 6h, Klassen
// 5.2.:     Interfaces

public class Umlet extends JComponent {

  protected static String CUSTOM_ELEMENTS_PATH="custom_elements/"; //LME
	
  JFileChooser _chooser = new JFileChooser();
  public static UmletPluginHandler pluginHandler=null;
  
  private static boolean _isPlugggedIn=false;
  
  public static boolean isPlugggedIn() {return _isPlugggedIn;}
  public static void setIsPluggedIn(boolean _plugged) {_isPlugggedIn=_plugged;};

  private static boolean _isEmbedded=false;
  
  public static boolean isEmbedded() {return _isEmbedded;}
  public static void setIsEmbedded(boolean _isEmbed) {_isEmbedded=_isEmbed;};  
  
  public JFrame window;
  
  public void createFrame() {
    window= new JFrame();
    window.setContentPane(this);
  }
  
  public class DrawPanel extends JPanel{
         private JScrollPane _scr;
        private int _incx=0;
        private int _incy=0;
       public Dimension getPreferredSize() {
                 int retx=0;
                 int rety=0;
                 for (int i=0; i<getComponents().length; i++) {
                    Component c=getComponent(i);
                    int x=c.getX()+c.getWidth();
                    int y=c.getY()+c.getHeight();
                    if (x>retx) retx=x;
                    if (y>rety) rety=y;
                 }
                 if (_incx>0) {
                     int viewsx = _scr.getViewport().getWidth();
                     if (viewsx+_incx>retx) retx=viewsx+_incx;
                 }
                 if (_incy>0) {
                     int viewsy = _scr.getViewport().getHeight();
                     if (viewsy+_incy>rety) rety=viewsy+_incy;
                 }
                 return new Dimension(retx, rety);
       }
       public void setScrollPanel(JScrollPane scr) {
             _scr=scr;
          scr.setViewportView(this);
       }
       public JScrollPane getScrollPanel() {
            return _scr;
       }
       public void incViewPosition(int incx, int incy) {
          _incx+=incx;
          _incy+=incy;
          Point viewp=_scr.getViewport().getViewPosition();
          _scr.getViewport().setViewSize(getPreferredSize());
          _scr.getViewport().setViewPosition(new Point(viewp.x+incx, viewp.y+incy));
         }
  }

  public String getHomePath() { //LME: retrieve the home directory
  	String homePath="";
  	if(isPlugggedIn()) homePath=UmletPlugin.getHomePath();
  	else {
      //System.out.println("loading palette from file...");
  		homePath="./";
  	}
  	return homePath;
  }
  
  
  //[UB]: rewrote this function to use dom instead of
  //      jdom.
  public String createStringToBeSaved() {
    Component[] components=this.getPanel().getComponents();


    DocumentBuilder db= null;
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      db = dbf.newDocumentBuilder();
    } catch (Exception e) {
      System.err.println("Error saving XML.");      
    }
    Document doc= db.newDocument();
    
    Element root= doc.createElement("umlet_diagram");
    doc.appendChild(root);
    
    for (int i=0; i<components.length; i++) {
      
      Entity e=(Entity)components[i];
      
      
      java.lang.Class c = e.getClass();
      String sElType = c.getName();
      int[] coor = e.getCoordinates();
      String sElPanelAttributes = e.getPanelAttributes();
      String sElAdditionalAttributes = e.getAdditionalAttributes();
      
      Element el=doc.createElement("element");
      root.appendChild(el);
      
      Element elType= doc.createElement("type");
      elType.appendChild(doc.createTextNode(sElType));
      el.appendChild(elType);

      Element elCoor= doc.createElement("coordinates");
      el.appendChild(elCoor);
      
      Element elX= doc.createElement("x");
      elX.appendChild(doc.createTextNode(""+coor[0]));
      elCoor.appendChild(elX);

      Element elY= doc.createElement("y");
      elY.appendChild(doc.createTextNode(""+coor[1]));
      elCoor.appendChild(elY);
      
      Element elW= doc.createElement("w");
      elW.appendChild(doc.createTextNode(""+coor[2]));
      elCoor.appendChild(elW);
      
      Element elH= doc.createElement("h");
      elH.appendChild(doc.createTextNode(""+coor[3]));
      elCoor.appendChild(elH);
      
      Element elPA= doc.createElement("panel_attributes");
      elPA.appendChild(doc.createTextNode(sElPanelAttributes));
      el.appendChild(elPA);
      
      Element elAA= doc.createElement("additional_attributes");
      elAA.appendChild(doc.createTextNode(sElAdditionalAttributes));
      el.appendChild(elAA);
    }
    
    
    //output the stuff...
    StringWriter stringWriter= null;
    try {
      DOMSource source = new DOMSource(doc);
      stringWriter= new StringWriter();
      StreamResult result= new StreamResult(stringWriter);
    
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();

      transformer.transform(source, result);
    } catch (Exception e){
      System.err.println("Error saving XML.");
      e.printStackTrace();
    }
    

    return stringWriter.toString();

  }

  public static boolean IS_CLIPPING=false;

  private int MAIN_UNIT=10;
  public int getMainUnit() { return MAIN_UNIT; }

  private boolean _isChanged=false;
  
  public boolean isChanged() {
       return _isChanged;
  }
  public void setChanged(boolean ch) {
    if (ch!=isChanged()) {
        _isChanged=ch;
        if (getFileName() ==null) return;
        if (isChanged()) {
            appendToTitle("* "+getFileName());
        } else {
            appendToTitle(getFileName());
        }
    }
  }

  private String _fileName=null;
  private String getFileName() { return _fileName; }
  public JMenuItem _saveMenuItem;
  public void setFileName(String s) {
    _fileName=s;
    if (_saveMenuItem!=null) {
      if (_fileName!=null)
        _saveMenuItem.setEnabled(true);
      else
        _saveMenuItem.setEnabled(false);
    }
  }

  private static String getStringFromFile(String fileName) {
    try {
      StringBuffer sb=new StringBuffer();
      BufferedReader br=new BufferedReader(new FileReader(fileName));
      String line;
      while(((line=br.readLine())!="")&&line!=null) sb = new StringBuffer(sb + line + "\n");
      String ret=new String(sb);
      if (ret.length()>0)
        if (ret.charAt(ret.length()-1)=='\n')
          ret=ret.substring(0, ret.length()-1);
      return ret;
    } catch (Exception e) {
      return null;
    }
  }
  private static String getStringFromStream(String streamName) {
    try {
      StringBuffer sb=new StringBuffer();
      BufferedReader br=new BufferedReader(new InputStreamReader(getInstance().getClass().getResourceAsStream(streamName)));
      String line;
      while(((line=br.readLine())!="")&&line!=null) sb = new StringBuffer(sb + line + "\n");
      String ret=new String(sb);
      if (ret.length()>0)
        if (ret.charAt(ret.length()-1)=='\n')
          ret=ret.substring(0, ret.length()-1);
      return ret;
    } catch (Exception e) {
      return null;
    }
  }

  public boolean askSaveIfDirty(){
      if (Umlet.getInstance().isChanged()) {
          int ch=JOptionPane.showOptionDialog(Umlet.getInstance(),"Save changes?","UMLet",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,null, null, null);
          if (ch==JOptionPane.YES_OPTION) {
              Umlet.getInstance().doSave();
              return true;
          } else if (ch==JOptionPane.CANCEL_OPTION) {
              return false;
          }
      }
      return true;
  }

  public void doNew() {
      if (askSaveIfDirty()) {
        Vector tmp=Selector.getInstance().getAllEntitiesOnPanel();
        Controller.getInstance().executeCommand(new RemoveElement(tmp));
        Umlet.getInstance().setFileName("New Diagram");
        Umlet.getInstance().getJspMain().revalidate();
        Umlet.getInstance().setChanged(false);
        Controller._instance=null; // clean UNDO-Commands
        FileOp._instance=null; // reset File settings
      }
  }
  public void doOpen(String fileNameIn) {
    if (askSaveIfDirty()) {
        FileOp._instance=null; // reset File settings
        String fileName;
        if(fileNameIn==null) fileName=FileOp.getInstance().getMltOpenFilename();
        else fileName=fileNameIn;
        if (fileName==null) return;
        setFileName(fileName);
        openFileToPanel(fileName, getInstance().getPanel());
        Controller._instance=null; // clean UNDO-Commands
    }
  }
  
  public static void openStreamToPanel(InputStream instr, JPanel p) {
    Vector tmp=Selector.getInstance().getAllEntitiesOnPanel();
    Controller.getInstance().executeCommand(new RemoveElement(tmp));
    
        //[UB]: Use the Factory instead of directly instantiating
        //      SAXParser because its constructor became protected
        try {
            SAXParser parser= SAXParserFactory.newInstance().newSAXParser();
            parser.parse(instr, new XMLContentHandler(p));
    } catch (Exception e) {
          System.err.println("Error parsing the inputstream.");
          System.err.println(e.getMessage());
        /*StackTraceElement[] trace=e.getStackTrace();
        String out="";
        for (int i=0; i<trace.length; i++) {
            out+=trace[i].toString()+"\n";
        }
        Umlet.getInstance().getPropertyPanel().setText("EX="+out);*/
        
    } finally {
        FileOp.getInstance().setSaveMenuItem(true);
        Umlet.getInstance().setChanged(false);
        Umlet.getInstance().repaint();
    }
  }
  private void openFileToPanel(String filename, JPanel p) {
    try {
        openStreamToPanel(new FileInputStream(filename), p);
        if( p != Umlet.getInstance().getPalettePanel())
            setChanged(false);
    } catch (Exception e) {
        StackTraceElement[] trace=e.getStackTrace();
        String out="";
        for (int i=0; i<trace.length; i++) {
            out+=trace[i].toString()+"\n";
        }
        Umlet.getInstance().getPropertyPanel().setText("EX="+out);        
    }
  }
 

  private void setEntitiesOnPanel(String s, JPanel p) {
    Vector v=Constants.decomposeStrings(s,Constants.DELIMITER_ENTITIES);
    for (int i=0; i<v.size(); i++) {
      String tmp=(String)v.elementAt(i);
      String s1=tmp.substring(0,4);
      String s2=tmp.substring(4,tmp.length());
      if (s1.equals("CLAS")) {
        p.add(new Class(s2));
      } else if (s1.equals("INTE")) {
        p.add(new Interface(s2));
      } else if (s1.equals("PACK")) {
        p.add(new Package(s2));
      } else if (s1.equals("USEC")) {
        p.add(new UseCase(s2));
      } else if (s1.equals("NOTE")) {
        p.add(new Note(s2));
      } else if (s1.equals("ACTO")) {
        p.add(new Actor(s2));
      } else if (s1.equals("RELA")) {
        Relation r=new Relation(s2);
        p.add(r);
      }
    }
    p.repaint();
  }



  public static void appendToTitle(String s) {
    if (isEmbedded()) return;
    if (s==null) {
      getInstance().window.setTitle("UMLet");
      return;
    }
    getInstance().window.setTitle("UMLet - "+s);
  }


  public void doSaveAs() {
    String fileName=FileOp.getInstance().getMltSaveFilename(true);
    if (fileName==null) return;
    save(fileName);
  }
  public void doSave() {
    if (!isPlugggedIn()) {
        String fileName=FileOp.getInstance().getMltSaveFilename(false);
        if (fileName==null) doSaveAs();
        else save(fileName);
    } else {
        pluginHandler.notifySave(false);
    }
  }
  public void doSaveAsSvg(String fileNameIn) {
    if (!isPlugggedIn()) {
    	String fileName;
        if(fileNameIn==null)fileName=FileOp.getInstance().getSvgFilename();
        else fileName = fileNameIn;
        if (fileName==null) return;
        GenSvg.createAndOutputSvgToFile(fileName);
    } else {
        pluginHandler.notifySaveAsFormat("svg");
    }
  }
  public void doSaveAsEps(String fileNameIn) {
    if (!isPlugggedIn()) {
    	String fileName;
        if(fileNameIn==null)fileName=FileOp.getInstance().getEpsFilename();
        else fileName = fileNameIn;
        if (fileName==null) return;
        GenEps.getInstance().createAndOutputEPSToFile(fileName);
    } else {
        pluginHandler.notifySaveAsFormat("eps");
    }
  }  
  public void doSaveAsPdf(String fileNameIn) {
      if (!isPlugggedIn()) {
      	String fileName;
        if(fileNameIn==null)fileName=FileOp.getInstance().getPdfFilename();
        else fileName = fileNameIn;
        if (fileName==null) return;
        GenPdf.getInstance().createAndOutputPdfToFile(fileName);
    } else {
        pluginHandler.notifySaveAsFormat("pdf");
    }
  }
  public void doSaveAsJPG(String fileNameIn) {
    if (!isPlugggedIn()) {      
        String fileName;
        if(fileNameIn==null)fileName=FileOp.getInstance().getJpgFilename();
        else fileName = fileNameIn;
        if (fileName==null) return;
        GenPdf.getInstance().createAndOutputJpgToFile(fileName);
    } else {
        pluginHandler.notifySaveAsFormat("jpg");
    }
  }
  private String save(String fileName) {
    String tmp=this.createStringToBeSaved();
    try {
      PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
      out.print(tmp);
      out.close();
      setFileName(fileName);
      appendToTitle(fileName);
      Umlet.getInstance().setChanged(false);
    } catch (java.io.IOException e) {
      return null;
    }
    return fileName;
  }
  public void doLoadClass(String absolutePath, String className) { //LME2
	//String[] fileNameStrArr=FileOp.getInstance().getCustomElementFilename();
  	String [] fileNameStrArr={absolutePath,className};
	if( fileNameStrArr==null) return;
	//System.out.println("doLoadClass: FILE TO LOAD:"+fileNameStrArr[0]);
	Entity e = CustomElementLoader.getInstance().doLoadClass(fileNameStrArr, false, false); //no forced clean build
	//getInstance().getPalettePanel().add(e);
	//System.out.println("doLoadClass->e:"+e);
	//Umlet.getInstance().getPalettePanel().add(e);
	if(e!=null) {
		Umlet.getInstance().getPanel().add(e);
		e.setLocation(50,50);
		//e.setSize(100,100); moved to Entity()
	}
  }

 
  private int _oldWidth=-1;
  private int _oldHeight=-1;
  public int getOldWidth() { return _oldWidth; }
  public void setOldWidth(int d) { _oldWidth=d;}
  public int getOldHeight() { return _oldHeight; }
  public void setOldHeight(int d) { _oldHeight=d; }  
  
  //A.Mueller start
  private JTextField _searchField;
  public JTextField getSearchField(){
      if (_searchField == null){
          _searchField = new JTextField();
          _searchField.setBackground(new Color(148,172,251));
          _searchField.setActionCommand("Search");
          _searchField.addActionListener(UniversalListener.getInstance());
      }
      return _searchField;
  }
  //A.Mueller end
  
  private JTextArea _sourceCodePanel; //LME
  public JTextArea getSourceCodePanel() {
	if (_sourceCodePanel==null) {
	  _sourceCodePanel=new JTextArea();
	  _sourceCodePanel.setLineWrap(false); //dont wrap lines
	}
	return _sourceCodePanel;
  }
  private JScrollPane _scrollPanel2; //LME
  public JScrollPane getScrollPanel2() { //scrollable panel for the source code
	if (_scrollPanel2==null) {
	  _scrollPanel2=new JScrollPane(getSourceCodePanel());
	}
	return _scrollPanel2;
  }
  public void setSourceCodePanelToEntity(Entity e) { //LME
	  _editedEntity=e;
	  
	  if (e!=null && !e.getJavaSource().equals("")) {
		_sourceCodePanel.setText(e.getJavaSource());
		_sourceCodePanel.setEnabled(true);
		setCompileCodeMenuButtonEnabled(true);
	  } else {
		_sourceCodePanel.setText("Click on a custom UML element to see and edit its source code here.");
		_sourceCodePanel.setEnabled(false);
		setCompileCodeMenuButtonEnabled(false);
	  }
	  Umlet.getInstance().getJspMain().requestFocus();
  }

  private JSplitPane _jspRightBottom; //LME
  public JSplitPane getJspRightBottom() { //register space for the source code panel
  	if (_jspRightBottom==null) {
  		_jspRightBottom=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  		_jspRightBottom.setDividerSize(10);
  	}
  	return _jspRightBottom;
  } 

  private JSplitPane _jspRightBottom2; //LME
  public JSplitPane getJspRightBottom2() { //register space for the save/compile-Button within the source code panel 
  	if (_jspRightBottom2==null) {
  		_jspRightBottom2=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  		_jspRightBottom2.setDividerSize(1);
  	}
  	return _jspRightBottom2;
  }
  
  private JButton compileCodeMenuB = new JButton("Compile and - if o.k. - overwrite current file"); //LME
  public void setCompileCodeMenuButtonEnabled(boolean flag) { //LME enable/disable the button in the source code panel
  	compileCodeMenuB.setEnabled(flag);
  }
  
  private JTextPane _propertyPanel;
  public JTextPane getPropertyPanel() {
    if (_propertyPanel==null) {
      _propertyPanel=new JTextPane();
    }
    return _propertyPanel;
  }
  private SelectorFrame _selFrame;
   public SelectorFrame getSelectorFrame() {
     if (_selFrame==null) {
        _selFrame=new SelectorFrame();
     }
     return _selFrame;
   }
  private JScrollPane _scrollPanel;
  public JScrollPane getScrollPanel() {
    if (_scrollPanel==null) {
      _scrollPanel=new JScrollPane(getPropertyPanel());
    }
    return _scrollPanel;
  }

  private static JTextPane _infoText;
  public static void outputInfoText(String s) {
    _infoText.setText(s);
  }
  private Entity _editedEntity;
  public Entity getEditedEntity() { return _editedEntity; }
  public String getPropertyString() { return _propertyPanel.getText(); }

  private DrawPanel _panel;
  public DrawPanel getPanel() {
    if (_panel==null) {
      _panel=new DrawPanel();
    }
    return _panel;
  }
  //[LI&UB]: return the currently selected palette
  public DrawPanel getPalettePanel() {
      String name= (String) _paletteList.getSelectedItem();
      return _paletteHashtable.get(name);    
  }

  //[LI&UB]: workaround for creating a new instance from a static method
  public DrawPanel createDrawPanel() {
      return new DrawPanel();
  }
  
  private static Umlet _instance;
  public static Umlet getInstance() {
    if (_instance==null) {
      _instance=new Umlet();
    }
    return _instance;
  }
  
  private static boolean _isInstanceCreated=false;
  
  public static void setInstanceCreated(boolean isCreated) {
    _isInstanceCreated=isCreated;
  }
    
  public static boolean isInstanceCreated() {
      return _isInstanceCreated;
  }

  public void setPropertyPanelToEntity(Entity e) {
    _editedEntity=e;
    if (e!=null) {
      _propertyPanel.setText(e.getPanelAttributes());
    } else {
        //[LI]: updated text
      _propertyPanel.setText( "Welcome to UMLet!\n\n"+
                              "Double-click on UML elements to add them to the diagram.\n"+
                              "Edit element properties by modifying the text in this panel.\n"+
                              "Edit the file 'palette.uxf' to store your own element palette.\n"+
                              "Press Del or Backspace to remove elements from the diagram.\n"+
                              "Hold down Ctrl key to select multiple elements.\n"+
                              "Press c to copy the UML diagram to the system clipboard."
                              );
    }
    Umlet.getInstance().getJspMain().requestFocus();
  }

  public Vector getAllEntities() {
    Vector<Component> v=new Vector<Component>();
    for (int i=0; i<_panel.getComponentCount(); i++) {
      v.add(_panel.getComponent(i));
    }
    return v;
  }
  public Vector getAllPaletteEntities() { //LME
	Vector<Component> v=new Vector<Component>();
	for (int i=0; i<getPalettePanel().getComponentCount(); i++) {
		v.add(getPalettePanel().getComponent(i));
	}
	return v;
}
  public int getCenterX() {
    return _panel.getWidth()/2;
  }
  public int getCenterY() {
    return _panel.getHeight()/2;
  }

  private JSplitPane _jspMain;
  public JSplitPane getJspMain() {
    if (_jspMain==null) {
      _jspMain=new JSplitPane();
      _jspMain.setDividerSize(10);
    }
    return _jspMain;
  }
  private JSplitPane _jspRight;
  public JSplitPane getJspRight() {
    if (_jspRight==null) {
      _jspRight=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
      _jspRight.setDividerSize(3);
    }
    return _jspRight;
  }
  
  private JScrollPane _jscrpMain;
 
  public JScrollPane getScrollPaneMain() {
    if (_jscrpMain==null) {
        _jscrpMain=new JScrollPane();
        getInstance().getPanel().setScrollPanel(_jscrpMain);
     }
     return _jscrpMain;
  }
//[LI&UB]: not needed any longer
  /*
  private JScrollPane _jscrpPalette;
  
  public JScrollPane getScrollPanePalette() {
    if (_jscrpPalette==null) {
        _jscrpPalette=new JScrollPane();
        getInstance().getPalettePanel().setScrollPanel(_jscrpPalette);
     }
     return _jscrpPalette;
  }
  */
  
  public static void main(String args[]) {
  	if(args.length!=0) {
  		String action = null;
  		String format = null;
  		String filename = null;
  		for(int i=0;i<args.length;i++) {
  			if(args[i].startsWith("-action=")) action=args[i].substring(8);
  			if(args[i].startsWith("-format=")) format=args[i].substring(8);
  			if(args[i].startsWith("-filename=")) filename=args[i].substring(10);
  		}
  		if(action!=null && format!=null && filename!=null) {
  			if(action.equals("convert")) {
  				init(false); //boolean value indicates not to display the window
  	  		    Umlet.getInstance().doNew();
  	  		    Umlet.getInstance().doConvert(filename, format);
  	  		    System.exit(0);
  			} else printUsage();
  		} else {
  			printUsage();
  		}
  	} else { //no arguments specified 
  		init(true);
  		Umlet.getInstance().doNew();
  	}
  }
  
  public void doConvert(String fileName, String format) {
  	Umlet.getInstance().doOpen(fileName);
  	String fileNameMain = fileName.substring(0,fileName.indexOf(".uxf"));
  	if(format!=null && format.equals("jpg")) Umlet.getInstance().doSaveAsJPG(fileNameMain+".jpg");
  	else if(format!=null && format.equals("pdf")) Umlet.getInstance().doSaveAsPdf(fileNameMain+".pdf");
  	else if(format!=null && format.equals("svg")) Umlet.getInstance().doSaveAsSvg(fileNameMain+".svg");
  	else if(format!=null && format.equals("eps")) Umlet.getInstance().doSaveAsEps(fileNameMain+".eps");
  }
  
  private static void printUsage() {
  	System.out.println("USAGE: -action=convert -format=(jpg|pdf|svg|eps) -filename=inputfile.uxf");
  }
  
  public static void init(UmletPluginHandler pluginHandler, boolean isEmbedded) {
    Umlet.pluginHandler=pluginHandler;
    setIsPluggedIn(true);
    setIsEmbedded(isEmbedded);
    Controller._instance=null; // clean UNDO-Commands
    FileOp._instance=null; // reset File settings    
    init(true);
  }
//[LI&UB]: scan for palette files
  public static File[] scanForPalettes()
  {
      //scan palettes directory...
      FileSystemView fileSystemView= FileSystemView.getFileSystemView();
      File[] paletteFiles= fileSystemView.getFiles(new File("palettes\\"), false);

      return paletteFiles;
  }
 
//[LI&UB]: cards is our container for our drawpanels
  private JPanel _cards;
  public JPanel getCards()
  {
      if (_cards == null)
          _cards= new JPanel(new CardLayout());
      
      return _cards;
  }
  
//[LI&UB]: for efficient look-up of the currently active drawpanel
  private Hashtable<String, DrawPanel> _paletteHashtable;
  public Hashtable<String, DrawPanel> getPaletteHashtable()
  {
      if (_paletteHashtable== null)
          _paletteHashtable= new Hashtable<String, DrawPanel>();
      
      return _paletteHashtable;
  }
  
  //[LI&UB]: This is the combobox which lists
  //the different palettes
  private JComboBox _paletteList;
  public JComboBox getPaletteList()
  {
      if (_paletteList == null)
          _paletteList= new JComboBox();
      
      return _paletteList;
  }
  
  //LME: combobox with custom elements
  private JComboBox _elementsList;
  public JComboBox getElementsList() {
  	if(_elementsList == null) _elementsList=new JComboBox();
  	return _elementsList;
  }
  
  /**
   * init
   * @param isVisible
   */
  public static void init(boolean isVisible) {      
    if (isInstanceCreated()) return;
    setInstanceCreated(true);
    
    if (!isEmbedded()) {
        // create frame window for standalone viewing
        getInstance().createFrame();
        getInstance().window.addWindowListener((WindowListener)UniversalListener.getInstance());
        getInstance().window.setBounds(10,10,980,780);
        getInstance().window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getInstance().window.validate();
        getInstance().window.repaint();
    }
    
    getInstance().setLayout(new BorderLayout());
    
    // Add split panel
    getInstance().add(getInstance().getJspMain(), BorderLayout.CENTER);

    // Add main panel
    //getInstance().getPanel().setBounds(0,0,300,300);
    getInstance().getPanel().setLayout(null);
    getInstance().getPanel().setBackground(Color.white);
    getInstance().getPanel().addMouseListener(UniversalListener.getInstance());
    getInstance().getPanel().addMouseMotionListener(UniversalListener.getInstance());
    //A.Mueller start
    JPanel p = new JPanel();
    getInstance().getSearchField().addFocusListener(UniversalListener.getInstance());
    p.setLayout(new BorderLayout());
    p.add(getInstance().getScrollPaneMain(),BorderLayout.CENTER);
    p.add(getInstance().getSearchField(), BorderLayout.SOUTH);
    getInstance().getJspMain().add(p,JSplitPane.LEFT);
    getInstance().getSearchField().setVisible(false);
    /*
     <OLDCODE>
     getInstance().getJspMain().add(getInstance().getScrollPaneMain(), JSplitPane.LEFT);
     </OLDCODE>
     */
    //A.Mueller end
    getInstance().getPanel().addKeyListener(UniversalListener.getInstance()); //LME5
    getInstance().getJspMain().setOneTouchExpandable(true);
    
    // Add palette panel
    //[UB & LI]: Also create combobox for listing different palettes
    //           and a jpanel with cardlayout
    JPanel paletteContainer= new JPanel();
    paletteContainer.setLayout(new BorderLayout());
    paletteContainer.add(getInstance().getCards(),BorderLayout.CENTER);
    
    Umlet.getInstance().getPaletteList().setEditable(false);
    Umlet.getInstance().getPaletteList().addItemListener(UniversalListener.getInstance());
    
    Umlet.getInstance().getElementsList().setEditable(false);
    Umlet.getInstance().getElementsList().addItemListener(UniversalListener.getInstance());
    //JPanel comboBoxPane= new JPanel();
    //comboBoxPane.add(Umlet.getInstance().getPaletteList());
    //paletteContainer.add(comboBoxPane, BorderLayout.PAGE_START);
    
    getInstance().getJspRight().add(paletteContainer, JSplitPane.TOP);

    // Add property panel
    getInstance().getPropertyPanel().addKeyListener(UniversalListener.getInstance());
    getInstance().getJspRightBottom().add(getInstance().getScrollPanel(), JSplitPane.TOP);

    //getInstance().getJspMain().add(getInstance().getJspRight(), JSplitPane.RIGHT);

	//LME: add source code panel
	getInstance().getSourceCodePanel().addKeyListener(UniversalListener.getInstance());
	getInstance().getJspRightBottom2().add(getInstance().getScrollPanel2(), JSplitPane.BOTTOM);
	getInstance().getJspRightBottom().setOneTouchExpandable(true);
	Container codeMenu = new Container();
	codeMenu.setLayout(new FlowLayout(FlowLayout.CENTER,10,1));
	//LME: Button
	getInstance().compileCodeMenuB.setActionCommand("COMPILE_BUTTON");
	getInstance().compileCodeMenuB.addActionListener(UniversalListener.getInstance());
	codeMenu.add(getInstance().compileCodeMenuB);
	getInstance().getJspRightBottom2().add(codeMenu, JSplitPane.TOP);
	Umlet.getInstance().setCompileCodeMenuButtonEnabled(false);
	getInstance().getJspRightBottom().add(getInstance().getJspRightBottom2(),JSplitPane.BOTTOM); //add code panel with buttons

	getInstance().getJspRight().add(getInstance().getJspRightBottom(), JSplitPane.RIGHT); //Register SplitPane
	getInstance().getJspMain().add(getInstance().getJspRight(), JSplitPane.RIGHT); //Register SpitPane

    KeyStroke bUndo = KeyStroke.getKeyStroke('z');
    AbstractAction aUndo = new AbstractAction() {
      public void actionPerformed( ActionEvent e ) {
          Selector.getInstance().deselectAll();
        Controller.getInstance().undo();
        System.out.println("undo");
      }
    };
    KeyStroke bRedo = KeyStroke.getKeyStroke('y');
    AbstractAction aRedo = new AbstractAction() {
      public void actionPerformed( ActionEvent e ) {
        Controller.getInstance().redo();
      }
    };
    
//  A.Mueller start
	// adding Ctrl+Z and Ctrl+Y (undo and redo) as well -> CHANGED: accellerators now
	//KeyStroke bUndo2 = KeyStroke.getKeyStroke("ctrl Z");
	//KeyStroke bRedo2 = KeyStroke.getKeyStroke("ctrl Y");
	
    //adding the slash to activate searching...
    KeyStroke bSearch = KeyStroke.getKeyStroke("ctrl F");
    AbstractAction aSearch = new AbstractAction() {
        public void actionPerformed( ActionEvent e) {
            Umlet.getInstance().getSearchField().setText("");
            Umlet.getInstance().getSearchField().setVisible(true);
            JPanel p = (JPanel)Umlet.getInstance().getSearchField().getParent();
            p.doLayout();
            Umlet.getInstance().getSearchField().requestFocus();
        }
    };
	// A.Mueller end
    
    KeyStroke bClip = KeyStroke.getKeyStroke('c');
    AbstractAction aClip = new AbstractAction() {
      public void actionPerformed( ActionEvent e ) {
        Clip.getInstance().copy();
      }
    };
    KeyStroke bBack = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,0,true); //Use BACKSPACE to remove Element
    AbstractAction aBack = new AbstractAction() {
      public void actionPerformed( ActionEvent e ) {
          Vector v=Selector.getInstance().getSelectedEntitiesOnPanel();
        if (v.size()>0) {
          Controller.getInstance().executeCommand(new RemoveElement(v));
        }
      }
    };
    //A.Mueller start
    /* nothing, because they are accelerators now...
    <OLDCODE>
    KeyStroke bDele = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0,true); //Use DELETE to remove Element
    AbstractAction aDele = new AbstractAction() {
      public void actionPerformed( ActionEvent e ) {
          Vector v=Selector.getInstance().getSelectedEntitiesOnPanel();
        if (v.size()>0) {
          Controller.getInstance().executeCommand(new RemoveElement(v));
        }
      }
    };
    KeyStroke bSave = KeyStroke.getKeyStroke('S', Event.CTRL_MASK , false); //Use CTRL-S to save file
     AbstractAction aSave = new AbstractAction() {
       public void actionPerformed( ActionEvent e ) {
         Umlet.getInstance().doSave();
       }
     };    
     </OLDCODE>
     */
     //A.Mueller end
     //LME5: Use CTRL-O to select overlapped Entities (which otherwise are hard to select)
    
     //A.Mueller start -> changed O to zero because of ctrl open
     KeyStroke bOtherElement = KeyStroke.getKeyStroke('0', Event.CTRL_MASK, false);
     //A.Mueller end
     AbstractAction aOtherElement = new AbstractAction() {
     	public void actionPerformed(ActionEvent e) {
     		Entity selEntity = Umlet.getInstance().getEditedEntity();
     		if(selEntity!=null) {
     			Vector<Entity> allEntities = Umlet.getInstance().getAllEntities();
     			for(int i=0;i<allEntities.size();i++) {
     				Entity en = allEntities.elementAt(i);
     				if((selEntity.getX()<=en.getX())&&(en.getX()<=selEntity.getX()+selEntity.getWidth()) &&
     						(selEntity.getY()<=en.getY())&&(en.getY()<=selEntity.getY()+selEntity.getHeight())) {
     					Umlet.getInstance().getPanel().remove(en); //## rearrange (Component)Entity 
     					Umlet.getInstance().getPanel().add(en,0);  //## in the Container
     					Selector.getInstance().deselect(selEntity); //deselect selected Entity
     					Selector.getInstance().singleSelect(en,false); //select overlapped Entities
     				}
     			}
     		} //#endif(selEntity...
     	}
     };

    InputMap inputMap   = Umlet.getInstance().getJspMain().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    ActionMap actionMap = Umlet.getInstance().getJspMain().getActionMap();
    inputMap.put( bUndo, "undo" );
    actionMap.put( "undo", aUndo );
    inputMap.put( bRedo, "redo" );
    actionMap.put( "redo", aRedo );
//  A.Mueller start
    inputMap.put(bSearch, "search");
    actionMap.put("search", aSearch);
	// A.Mueller end
    inputMap.put( bBack, "back" );
    actionMap.put( "back", aBack );
//  A.MUeller start
    //removed following four lines -> is an accellerator now
    //inputMap.put( bDele, "dele" );
    //actionMap.put( "dele", aDele );
    //inputMap.put( bSave, "save" );
    //actionMap.put( "save", aSave );
    //A.Mueller end
    inputMap.put( bClip, "clip" );
    actionMap.put( "clip", aClip );
    inputMap.put( bOtherElement, "nextelement" );
    actionMap.put( "nextelement", aOtherElement );



    // Add menu
    JMenuBar m=new JMenuBar();
    JMenu iFile=new JMenu("File");
    JMenu iEdit=new JMenu("Edit");
    //JMenu iFont=new JMenu("Font-Size"); //LME5
    m.add(iFile); m.add(iEdit); //m.add(iFont);

    // Tudor
    JMenu iClass = new JMenu("Class"); // TODO review the menu name
    m.add(iClass);
    //
    
    JLabel l1 = new JLabel(" Insert custom element: ");
    JLabel l2 = new JLabel(" Palettes: ");
    l1.setEnabled(false); //LME: give a different look to labels  
    l2.setEnabled(false);
    m.add(l1);
    m.add(Umlet.getInstance().getElementsList()); //LME: list of custom elements
    m.add(l2);
    m.add(Umlet.getInstance().getPaletteList()); //LME: moved palette-combobox to menu bar
        
    
    //A.Mueller start
    JMenuItem iNew=new JMenuItem("New");
    iNew.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
    JMenuItem iOpen=new JMenuItem("Open...");
    iOpen.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
    JMenuItem iSave=new JMenuItem("Save");
    iSave.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
    getInstance()._saveMenuItem=iSave;
    getInstance().setFileName(null);
    JMenuItem iSaveAs=new JMenuItem("Save as...");
    JMenuItem iSaveAsGif=new JMenuItem("Save as JPG...");
    JMenuItem iSaveAsSvg=new JMenuItem("Save as SVG...");
    JMenuItem iSaveAsPdf=new JMenuItem("Save as PDF...");
    JMenuItem iSaveAsEps=new JMenuItem("Save as EPS...");

    JMenuItem iDelete=new JMenuItem("Delete");
    iDelete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
    JMenuItem iUndo=new JMenuItem("Undo");
    iUndo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
    JMenuItem iRedo=new JMenuItem("Redo");
    iRedo.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
    JMenuItem iRealign=new JMenuItem("Realign to grid");
    iRealign.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
    
    // Tudor - create submenu items for 
    JMenuItem iSetClassOptions = new JMenuItem("Set Class Options"); 
    JMenuItem iAddAtribute = new JMenuItem("Add Atribute to Class");
    JMenuItem iAddOperation = new JMenuItem("Add Operation to Class");
    //
    
    /*
     <OLDCODE>
     JMenuItem iNew=new JMenuItem("New");
    JMenuItem iOpen=new JMenuItem("Open");
    JMenuItem iSave=new JMenuItem("Save");
    getInstance()._saveMenuItem=iSave;
    getInstance().setFileName(null);
    JMenuItem iSaveAs=new JMenuItem("Save as..");
    JMenuItem iSaveAsGif=new JMenuItem("Save as JPG..");
    JMenuItem iSaveAsSvg=new JMenuItem("Save as SVG..");
    JMenuItem iSaveAsPdf=new JMenuItem("Save as PDF..");
    JMenuItem iSaveAsEps=new JMenuItem("Save as EPS..");

    JMenuItem iDelete=new JMenuItem("Delete");
    JMenuItem iUndo=new JMenuItem("Undo");
    JMenuItem iRedo=new JMenuItem("Redo");
    JMenuItem iRealign=new JMenuItem("Realign to grid");
     </OLDCODE
     */
    //A.Mueller end
    
	// A.Mueller Start
    JMenuItem iSearch = new JMenuItem("Find");
    iSearch.setAccelerator(KeyStroke.getKeyStroke('/'));
	JMenuItem iGroup = new JMenuItem("Group");
	iGroup.setAccelerator(KeyStroke.getKeyStroke("ctrl G"));
	JMenuItem iUngroup = new JMenuItem("Ungroup");
	iUngroup.setAccelerator(KeyStroke.getKeyStroke("ctrl U"));
	JCheckBoxMenuItem iTransparency = new JCheckBoxMenuItem("Toggle Transparency");
    iTransparency.setSelected(false);
	iTransparency.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
	// A.Mueller End
    
    //JMenuItem iFontSize6=new JMenuItem("6");
    //JMenuItem iFontSize8=new JMenuItem("8");
    //JMenuItem iFontSize10=new JMenuItem("10");
    //JMenuItem iFontSize12=new JMenuItem("12");
    
    
    if (!isPlugggedIn()) iFile.add(iNew);
    if (!isPlugggedIn()) iFile.add(iOpen);
    iFile.add(iSave);
    if (!isPlugggedIn()) iFile.add(iSaveAs);
    iFile.add(iSaveAsGif);
    iFile.add(iSaveAsSvg);
    iFile.add(iSaveAsPdf);
    iFile.add(iSaveAsEps);
    iEdit.add(iUndo);
    iEdit.add(iRedo);
    iEdit.add(iDelete);
    iEdit.add(iRealign); //LME
    // A.Mueller start
	iEdit.add(iGroup);
	iEdit.add(iUngroup);
	iEdit.add(iSearch);
	iEdit.add(iTransparency);
	// A.Mueller end

    //iFont.add(iFontSize6);
    //iFont.add(iFontSize8);
    //iFont.add(iFontSize10);
    //iFont.add(iFontSize12);

	// Tudor - add submenus to the menu
	iClass.add(iSetClassOptions);
	iClass.add(iAddAtribute);
	iClass.add(iAddOperation);
		// add some action listeners
	iSetClassOptions.addActionListener(UniversalListener.getInstance());
	iAddAtribute.addActionListener(UniversalListener.getInstance());
	iAddOperation.addActionListener(UniversalListener.getInstance());
	//

    iNew.addActionListener(UniversalListener.getInstance());
    iOpen.addActionListener(UniversalListener.getInstance());
    iSave.addActionListener(UniversalListener.getInstance());
    iSaveAs.addActionListener(UniversalListener.getInstance());
    iSaveAsGif.addActionListener(UniversalListener.getInstance());
    iSaveAsSvg.addActionListener(UniversalListener.getInstance());
    iSaveAsEps.addActionListener(UniversalListener.getInstance());
    iSaveAsPdf.addActionListener(UniversalListener.getInstance());

    iUndo.addActionListener(UniversalListener.getInstance());
    iRedo.addActionListener(UniversalListener.getInstance());
    iDelete.addActionListener(UniversalListener.getInstance());
    iRealign.addActionListener(UniversalListener.getInstance());
    // A.Mueller Start
	iGroup.addActionListener(UniversalListener.getInstance());
	iSearch.addActionListener(UniversalListener.getInstance());
	iUngroup.addActionListener(UniversalListener.getInstance());
	iTransparency.addActionListener(UniversalListener.getInstance());
	iEdit.addMenuListener(UniversalListener.getInstance());
	// A.Mueller End
    //iFontSize6.addActionListener(UniversalListener.getInstance());
    //iFontSize8.addActionListener(UniversalListener.getInstance());
    //iFontSize10.addActionListener(UniversalListener.getInstance());
    //iFontSize12.addActionListener(UniversalListener.getInstance());
    
    m.setLayout(new FlowLayout(0,0,0));
    getInstance().add(m, BorderLayout.NORTH);

    getInstance().getJspMain().addComponentListener(UniversalListener.getInstance());
    getInstance().getJspRight().addComponentListener(UniversalListener.getInstance());
    //getInstance().addKeyListener(UniversalListener.getInstance());
    //getInstance().setLrSeparatorLocation(520);
    //getInstance().setTbSeparatorLocation(550);
    
    //getInstance().getJspMain().setDividerLocation(520);
    //getInstance().getJspRight().setDividerLocation(450);
    
    if (!isEmbedded()) getInstance().window.setVisible(isVisible); //set invisible in batchrunning mode
    //System.out.println("xxx"+getInstance().toString()+"--"+getInstance().getBounds());
    //getInstance().setOldBounds(getInstance().getBounds());
    getInstance().setPropertyPanelToEntity(null);
    getInstance().setSourceCodePanelToEntity(null); //LME

    /*String palette=getInstance().getStringFromFile("."+File.separator+"palette.mlt");
    if (palette!=null) {
      getInstance().setEntitiesOnPanel(palette, getInstance().getPalettePanel());
    } else {
      palette = getInstance().getStringFromStream("palette.mlt");
      if (palette!=null) {
        getInstance().setEntitiesOnPanel(palette, getInstance().getPalettePanel());
      }
    }*/
    //System.out.println("path="+((new File("palette/palette.uxf")).getAbsolutePath()));
   
    
    String homePath=Umlet.getInstance().getHomePath();
      
    
    //LME: search for all existing palette files (= all .uxf files in the palettes/ directory)
    String[] paletteFileList = new File(homePath+"palettes/").list(new PaletteFilenameFilter());
    //if (paletteFileList != null) for(int i=0;i<paletteFileList.length;i++) System.out.println("paletteFileList["+i+"]:"+paletteFileList[i]);
    
    //[UB & LI]: Load several palette-files
    if (paletteFileList!=null && paletteFileList.length > 0)
    {
        //System.out.println("Loading palette(s) from palettes/ directory...");

        for(int i=0;i<paletteFileList.length;i++) { //LME: put the default palette on top 
        	if(paletteFileList[i].equals("default_palette.uxf")&&(i!=0)) {
        		String dummy=paletteFileList[0];
        		paletteFileList[0]=paletteFileList[i];
        		paletteFileList[i]=dummy;
        	}
        }
        
        for (int i=0; i< paletteFileList.length; i++)
        {
            DrawPanel d= Umlet.getInstance().createDrawPanel();
            d.setBackground(Color.white);
            d.setLayout(null);
            d.addMouseListener(UniversalListener.getInstance());
            d.addMouseMotionListener(UniversalListener.getInstance());            
            
            JScrollPane scrollPnl= new JScrollPane();
            d.setScrollPanel(scrollPnl);
            
            Umlet.getInstance().openFileToPanel(homePath+"palettes/"+paletteFileList[i], d );
            Umlet.getInstance().getCards().add(scrollPnl, paletteFileList[i]);
            
            Umlet.getInstance().getPaletteList().addItem(paletteFileList[i]);
            Umlet.getInstance().getPaletteHashtable().put(paletteFileList[i], d);
        }    
    }

    //load custom elements from "elements" directory
    String[] elementFileList = new File(homePath+CUSTOM_ELEMENTS_PATH).list(new CustomElementFilenameFilter());
    Umlet.getInstance().getElementsList().addItem("");
	Umlet.getInstance().getElementsList().addItem("NEW ELEMENT");
    if (elementFileList!=null && elementFileList.length > 0) {
    	for(int i=0;i<elementFileList.length;i++) {
    		Umlet.getInstance().getElementsList().addItem(elementFileList[i]);
    	}
    }
    
	//GenPdf.getInstance().outputPdf();
	//GenPdf.getInstance().outputJpeg();
  }
  /**init********************************************************************/
  
  
  protected JDialog _compilerMessageDialog;
  public void showCompilerMessage(String message) { //LME3
	_compilerMessageDialog = new JDialog(window,"Compiler message",true);
	Container contentPane = _compilerMessageDialog.getContentPane();
	contentPane.setLayout(new BorderLayout());
		
	JTextArea textField = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(textField); //make textArea scrollable
	textField.setText(message);
	textField.setEditable(false);
		
	JButton okButton = new JButton("Ok");
	okButton.setActionCommand("COMPILER_MESSAGE_DIALOG_OK"); //see UniversalListener
	okButton.addActionListener(UniversalListener.getInstance());
		
	contentPane.add(scrollPane);
	contentPane.add(okButton, BorderLayout.SOUTH);
	_compilerMessageDialog.setSize(500,300);
	Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension dialogDim = _compilerMessageDialog.getSize();
	_compilerMessageDialog.setLocation(screenDim.width/2-dialogDim.width/2,screenDim.height/2-dialogDim.height/2); //center dialog
	_compilerMessageDialog.setVisible(true);
  }
  
  protected JDialog _newCustomElementDialog;
  public JTextField showNewCustomElementDialog() {
  	_newCustomElementDialog = new JDialog(window,"Create new custom element",true);
	Container contentPane = _newCustomElementDialog.getContentPane();
	//contentPane.setLayout(new BorderLayout());
	JTextField elementName = new JTextField("NewElement");
	elementName.setColumns(25);
	
	JButton okButton = new JButton("Ok");
	okButton.setActionCommand("NEW_CUSTOM_ELEMENT_OK"); //see UniveralListener
	okButton.addActionListener(UniversalListener.getInstance());
	JButton cancelButton = new JButton("Cancel");
	cancelButton.setActionCommand("NEW_CUSTOM_ELEMENT_CANCEL");
	cancelButton.addActionListener(UniversalListener.getInstance());
	
	contentPane.add(elementName);
	Container buttonContainer = new Container();
	buttonContainer.setLayout(new FlowLayout());
	buttonContainer.add(okButton);
	buttonContainer.add(cancelButton);
	
	contentPane.add(buttonContainer,BorderLayout.SOUTH);
	
	_newCustomElementDialog.pack();
	Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension dialogDim = _newCustomElementDialog.getSize();
	_newCustomElementDialog.setLocation(screenDim.width/2-dialogDim.width/2,screenDim.height/2-dialogDim.height/2); //center dialog
	_newCustomElementDialog.setVisible(true);
	return elementName;
  }
}