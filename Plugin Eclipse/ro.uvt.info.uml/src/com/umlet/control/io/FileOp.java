package com.umlet.control.io;

import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import com.umlet.control.*;


public class FileOp {
    public static JFileChooser jfcOpenMlt ;
    public static JFileChooser jfcSaveAsMlt ;
    public static JFileChooser jfcSaveAsSvg ;
    public static JFileChooser jfcSaveAsPdf ;
    public static JFileChooser jfcSaveAsJpg ;
	public static JFileChooser jfcSaveAsEPS; //LME

	private String saveFileName=null;

	public static FileOp _instance;
	public static FileOp getInstance() {
		if (_instance==null) { _instance=new FileOp(); }
		return _instance;
	}
	private FileOp() { //LME 22.7. content moved to respective methods (JFileChooser problem)
	}

	public String getJpgFilename() {
    	if(jfcSaveAsJpg==null) { 
    		jfcSaveAsJpg = new JFileChooser();
	    	FileFilter filter = new FileFilter() {
	  	      public boolean accept(File f) {
	  	        return (f.getName().endsWith(".jpg") || f.getName().endsWith(".jpeg") || f.isDirectory());
	  	      }
	  	      public String getDescription() {
	  	        return "JPG format (*.jpg, *.jpeg)";
	  	      }
	  	    };
	  	    jfcSaveAsJpg.setFileFilter(filter);
  	    	jfcSaveAsJpg.setSelectedFile(new File(".jpg"));
    	}
	    int returnVal = jfcSaveAsJpg.showSaveDialog(Umlet.getInstance());
	    if(returnVal != JFileChooser.APPROVE_OPTION) return null;
	    return jfcSaveAsJpg.getSelectedFile().getAbsolutePath();
    }
    
    public String getSvgFilename() {
    	if(jfcSaveAsSvg==null) {
    		jfcSaveAsSvg = new JFileChooser();
	    	FileFilter filter = new FileFilter() {
	  	      public boolean accept(File f) {
	  	        return (f.getName().endsWith(".svg") || f.isDirectory());
	  	      }
	  	      public String getDescription() {
	  	        return "SVG format (*.svg)";
	  	      }
	  	    };
	  	    jfcSaveAsSvg.setFileFilter(filter);
	  	    jfcSaveAsSvg.setSelectedFile(new File(".svg"));
    	}
	    int returnVal = jfcSaveAsSvg.showSaveDialog(Umlet.getInstance());
	    if(returnVal != JFileChooser.APPROVE_OPTION) return null;
	    return jfcSaveAsSvg.getSelectedFile().getAbsolutePath();
    }
    public String getPdfFilename() {
    	if(jfcSaveAsPdf==null) { 
    		jfcSaveAsPdf = new JFileChooser();
	    	FileFilter filter = new FileFilter() {
	  	      public boolean accept(File f) {
	  	        return (f.getName().endsWith(".pdf") || f.isDirectory());
	  	      }
	  	      public String getDescription() {
	  	        return "PDF format (*.pdf)";
	  	      }
	  	    };
	  	    jfcSaveAsPdf.setFileFilter(filter);
	  	    jfcSaveAsPdf.setSelectedFile(new File(".pdf"));
    	}
	    int returnVal = jfcSaveAsPdf.showSaveDialog(Umlet.getInstance());
	    if(returnVal != JFileChooser.APPROVE_OPTION) return null;
	    return jfcSaveAsPdf.getSelectedFile().getAbsolutePath();
    }
	public String getEpsFilename() { //LME
		if(jfcSaveAsEPS==null) { 
			jfcSaveAsEPS = new JFileChooser(); //LME
			FileFilter filter = new FileFilter() {
			  public boolean accept(File f) {
				return (f.getName().endsWith(".eps") || f.isDirectory());
			  }
			  public String getDescription() {
				return "EPS format (*.eps)";
			  }
			};
			jfcSaveAsEPS.setFileFilter(filter);
			jfcSaveAsEPS.setSelectedFile(new File(".eps"));
		}
		int returnVal = jfcSaveAsEPS.showSaveDialog(Umlet.getInstance());
		if(returnVal != JFileChooser.APPROVE_OPTION) return null;
		return jfcSaveAsEPS.getSelectedFile().getAbsolutePath();
	}
	
    public String getMltSaveFilename(boolean ask_again) {
    	if(jfcSaveAsMlt==null) { 
    		jfcSaveAsMlt = new JFileChooser();
	    	FileFilter filter = new FileFilter() {
	        	public boolean accept(File f) {
	          		return (f.getName().endsWith(".uxf") || f.isDirectory());
	        	}
	        	public String getDescription() {
	          		return "UMLet diagram format (*.uxf)";
	        	}
	      	};
	    	jfcSaveAsMlt.setFileFilter(filter);
	    	jfcSaveAsMlt.setSelectedFile(new File(".uxf"));
    	}
    	if (ask_again) {
		    int returnVal = jfcSaveAsMlt.showSaveDialog(Umlet.getInstance());
		    if(returnVal != JFileChooser.APPROVE_OPTION) return null;
		    saveFileName=jfcSaveAsMlt.getSelectedFile().getAbsolutePath();
		    setSaveMenuItem(true);
    	}
	    return saveFileName;
     }

    public String getMltOpenFilename() {
    	if(jfcOpenMlt==null) {
    		jfcOpenMlt = new JFileChooser();
	    	FileFilter filter = new FileFilter() {
	        	public boolean accept(File f) {
	          		return (f.getName().endsWith(".uxf") || f.isDirectory());
	        	}
	        	public String getDescription() {
	          		return "UMLet diagram format (*.uxf)";
	        	}
	      	};
	    	jfcOpenMlt.setFileFilter(filter);
    	}
    	String fileName=null;
        int returnVal = jfcOpenMlt.showOpenDialog(Umlet.getInstance());
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	      fileName=jfcOpenMlt.getSelectedFile().getAbsolutePath();
	    }
	    if (fileName!=null) saveFileName=fileName;
	    return fileName;
    }


    public void setSaveMenuItem(boolean b) {
     	if (Umlet.getInstance()._saveMenuItem!=null) {
     		Umlet.getInstance()._saveMenuItem.setEnabled(b);
     	}
    }
}

