/*******************************************************************************
 * 2004-03-03 15:00 runs
 *******************************************************************************/
package com.umlet.plugin.editors;

import java.awt.*;
import java.io.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*; //org.eclipse.ui.part.FileEditorInput;
import org.eclipse.swt.SWT;


import com.umlet.control.*;
import com.umlet.control.io.*;

public class UmletEditor extends EditorPart implements UmletPluginHandler {
	private boolean isDirty = false;
	private org.eclipse.swt.widgets.Composite parent;
	
	public Panel embedded_umlet_panel;
	
	public static String UMLET_SAVE="UMLET_SAVE";
	public static String UMLET_CLOSE="UMLET_CLOSE";
	private class UmletEventThread extends Thread{
		
		private IEditorPart editor;
		private String action;
		private boolean inFlag;
		public boolean returnCode; 
		
		
		public UmletEventThread(IEditorPart editor, String action, boolean inFlag) {
			this.editor=editor;
			this.action=action;
			this.inFlag=inFlag;
		}
		public void run() {
			System.out.println("UmletEventThread.run");
			if (action.equalsIgnoreCase(UMLET_SAVE)) {
				returnCode = editor.getSite().getPage().saveEditor(editor, inFlag);
			} else if (action.equalsIgnoreCase(UMLET_CLOSE)) {
				returnCode = editor.getSite().getPage().closeEditor(editor, inFlag);
			}
		}
	}
	
	// *** Implementation of interface UmletPluginHandler ***
	public boolean notifyIsClosing() {
		System.out.println("UmletPlugin->notifyIsClosing...");
		if (!Umlet.isEmbedded()) Umlet.getInstance().window.toBack();
		Display disp = getSite().getShell().getDisplay();
		UmletEventThread umletEvent = new UmletEventThread(this, UMLET_CLOSE, true);
		disp.syncExec(umletEvent);
		System.out.println("UmletPlugin->after exec..."+ umletEvent.returnCode);
		if (!umletEvent.returnCode && !Umlet.isEmbedded()) Umlet.getInstance().window.toFront();
		return umletEvent.returnCode;
	}
	public void notifyIsDirty(boolean isDirty) {
		this.isDirty=isDirty;
	}
	public boolean notifySave(boolean ask) {
		System.out.println("UmletPlugin->notifySave...");
		Display disp = getSite().getShell().getDisplay();
		UmletEventThread umletEvent = new UmletEventThread(this, UMLET_SAVE, ask);
		disp.syncExec(umletEvent);
		return umletEvent.returnCode;
		//getSite().
	}
	public boolean notifySaveAsFormat(String format) {
		try {
			setFocus();
			if (!hasUmletHandle()) return false;
			ByteArrayOutputStream outdata = new ByteArrayOutputStream();
			if (format.equalsIgnoreCase("PDF")) {
				GenPdf.getInstance().createPdfToStream(outdata);
			} else if (format.equalsIgnoreCase("JPG")) {
				GenPdf.getInstance().createJpgToStream(outdata);
			} else if (format.equalsIgnoreCase("SVG")) {
				GenSvg.createSvgToStream(outdata);
			} else if (format.equalsIgnoreCase("EPS")) {
				GenEps.getInstance().createEpsToStream(outdata);
			}				
			IFile selFile = (IFile) ((FileEditorInput) getEditorInput()).getStorage();
			IContainer targetFolder = selFile.getParent();
					
			IPath newFilePath = targetFolder.getFullPath().append("/"+selFile.getName().substring(0, selFile.getName().length()-4)+"."+format.toLowerCase());
                        //[UB]: changed WorkspacePlugin to ResourcesPlugin for eclipse3.0 compatibility
			IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newFilePath);
			if (!newFile.exists()) {
				newFile.create(new ByteArrayInputStream(outdata.toByteArray()), false, null);
			} else {
				newFile.setContents(new ByteArrayInputStream(outdata.toByteArray()), false, true, null);
			}
		} catch (CoreException e) {
			throw new RuntimeException(e.getMessage());
		}
		return true;
	}	
	// ********************************************************

	public void dispose() {
		System.out.println("UmletPlugin->dispose...");
		if (hasUmletHandle()) {
			if (!Umlet.isEmbedded()) Umlet.getInstance().window.setVisible(false);
			Umlet.pluginHandler=null;
		}
	}		
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		System.out.println("UmletPlugin->doSave...");
		if (!hasUmletHandle()) return;
		try {
			IFile filestorage = (IFile) ((FileEditorInput) getEditorInput()).getStorage();
			InputStream newContent = new ByteArrayInputStream(Umlet.getInstance().createStringToBeSaved().getBytes("UTF-8"));
			filestorage.setContents(newContent, false, true, monitor);
			Umlet.getInstance().setChanged(false);
		} catch (Exception e)  {
			throw new RuntimeException(e.getMessage()); 
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		System.out.println("UmletPlugin->doSaveAs...");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker marker) {
		System.out.println("UmletPlugin->gotoMarker...");

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	//public String title;
	
	public void init(IEditorSite site, IEditorInput input)
		throws PartInitException {
		System.out.println("UmletPlugin->init..." + this.toString());
                 
		//[UB]: changed setTitle(input.getName()); to setPartName
		//for eclipse-3.0 compatibility
                setPartName(input.getName());
		setInput(input);
		setSite(site);

	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() {
		if (hasUmletHandle()) isDirty=Umlet.getInstance().isChanged();
		System.out.println("UmletPlugin->isDirty..."+isDirty);
		return isDirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		System.out.println("UmletPlugin->isSaveAsAllowed...");
		return isDirty();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(org.eclipse.swt.widgets.Composite parent) {
		System.out.println("UmletPlugin->createPartControl...");

		// trying to embed the Umlet Swing-Panel
		try {

            /* [UB]: embedding works a bit differently in eclipe-3.0 
             *       It should work on linux too.
             */
            org.eclipse.swt.widgets.Composite goodSWTComposite= 
                new org.eclipse.swt.widgets.Composite(parent, SWT.EMBEDDED); //we need the embedded attribute set
            java.awt.Frame frame= org.eclipse.swt.awt.SWT_AWT.new_Frame(goodSWTComposite);
            embedded_umlet_panel = new java.awt.Panel(new java.awt.BorderLayout());
            frame.add(embedded_umlet_panel);
            //embedded_umlet_panel.validate();
          
			//parent.layout();
			//parent.redraw();
			this.parent= goodSWTComposite;
        } catch (IllegalArgumentException e) {
             System.out.println(e.getMessage());
		} catch (Throwable e) {
			System.err.println("UmletPlugin->Could not embed windows. " + e.getMessage());
			// something went wrong - show Umlet as standalone window
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		System.out.println("UmletPlugin->setFocus...");
		// open File Data with Umlet if not empty:
		if (!hasUmletHandle()) {
			// trying to grap the Umlet Singleton Instance:
			if (Umlet.pluginHandler!=null) {
				if (!Umlet.pluginHandler.notifySave(true)) {
					//throw new RuntimeException("Cant not open Umlet editor for "+getEditorInput().getName()+".");
					getSite().getPage().activate((UmletEditor) Umlet.pluginHandler);
					return;
				}
				if (isUmletEmbedded()) {
					((UmletEditor) Umlet.pluginHandler).embedded_umlet_panel.removeAll();
				}
			}
			try {
				FileEditorInput fileIn = (FileEditorInput) getEditorInput();
				InputStream data = fileIn.getFile().getContents();
				if (isUmletEmbedded()) {
					embedded_umlet_panel.add(Umlet.getInstance()); //LME: Umlet.getInstance() first call
					//if (isUmletEmbedded()) Umlet.getInstance().getJspMain().setDividerLocation(Umlet.getInstance().getJspMain().getDividerLocation());	
					//embedded_umlet_panel.repaint();					
				}
				Umlet.init(this, isUmletEmbedded());
				Umlet.openStreamToPanel(data, Umlet.getInstance().getPanel());
				Umlet.getInstance().setFileName(fileIn.getName());
				Umlet.appendToTitle(fileIn.getName());
				if (isUmletEmbedded()) {
					//if (isUmletEmbedded()) Umlet.getInstance().getJspMain().setDividerLocation(Umlet.getInstance().getJspMain().getDividerLocation());	
					embedded_umlet_panel.validate();
					//embedded_umlet_panel.repaint();					
				}				
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}	

		if (!Umlet.isEmbedded()) {
			Umlet.getInstance().window.setVisible(true);
			Umlet.getInstance().window.toFront();
		} 
	}
	
	private boolean hasUmletHandle() {
		return Umlet.pluginHandler==this;
	}
	
	private boolean isUmletEmbedded() {
		return embedded_umlet_panel!=null;
	}	
}
