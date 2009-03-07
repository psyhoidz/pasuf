package com.umlet.GUI.standalone;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;

import com.umlet.GUI.MenuListener;
import com.umlet.GUI.OptionPanel;
import com.umlet.control.BrowserLauncher;
import com.umlet.control.Umlet;
import com.umlet.control.command.Copy;
import com.umlet.control.command.Cut;
import com.umlet.control.command.Paste;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.control.diagram.Selector;

import com.umlet.help.AboutWindow;

public class StandaloneMenuListener extends MenuListener {

	public StandaloneMenuListener() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		
		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
		DrawPanel currentdiagram = Umlet.getInstance().getGUI().getCurrentDiagram(); //current active diagram
		Selector selector = null;
		if (e.getSource() instanceof JMenuItem) 
		{		
			JMenuItem b = (JMenuItem) e.getSource();
			
			if(handler != null) // only execute if at least one diagram is available
			{
				selector = handler.getDrawPanel().getSelector();					
				if (b.getText() == "Undo" && selector != null) {
					selector.deselectAll();
					handler.getController().undo();
				} else if (b.getText() == "Redo") {
					handler.getController().redo();
				} else if (b.getText().equals("Copy")) {
					(new Copy()).execute(handler);
				} else if (b.getText().equals("Paste")) {
					handler.getController().executeCommand(new Paste());
				} else if (b.getText().equals("Cut")) {
					handler.getController().executeCommand(new Cut());
				} else if (b.getText().equals("Select all")) {
					selector.selectAll();
				}
			}
			
			if(currentdiagram != null) //nur f�r diagramme - nicht f�r paletten!
			{
				selector = currentdiagram.getSelector();
				if (b.getText() == "Save") {
					currentdiagram.getHandler().doSave();
				} else if (b.getText() == "Save as...") {
					currentdiagram.getHandler().doSaveAs("uxf");
				} else if (b.getText() == "Save as JPG...") {
					selector.deselectAll();
					currentdiagram.getHandler().doSaveAs("jpg");
				} else if (b.getText() == "Save as SVG...") {
					selector.deselectAll();
					currentdiagram.getHandler().doSaveAs("svg");
				} else if (b.getText() == "Save as EPS...") { // LME
					selector.deselectAll();
					currentdiagram.getHandler().doSaveAs("eps");
				} else if (b.getText() == "Save as PDF...") {
					selector.deselectAll();
					currentdiagram.getHandler().doSaveAs("pdf");
				}
			}
			
			if (b.getText() == "Open...") {
				Umlet.getInstance().doOpen();
			} else if (b.getText() == "New") {
				Umlet.getInstance().doNew(); 
			} else if (b.getText() == "Online Help") {
				BrowserLauncher
						.openURL("http://umlet.com.hosting.domaindirect.com/faq.htm");
			} else if (b.getText() == "About") {
				AboutWindow.getInstance().setVisible();
			} else if (b.getText() == "Exit") {
				Umlet.getInstance().getGUI().closeWindow();
			} else if(b.getText().equals("Edit Current Palette")) {
				Umlet.getInstance().doOpen(Umlet.getInstance().getPalette().getFileHandler().getFullPathName());
			} else if(b.getText().equals("New...")) {
				if(Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity())
				{
					Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
					Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
					Umlet.getInstance().getGUI().getCurrentCustomHandler().newEntity();
				}
			}
			else if(b.getText().equals("Options")) {
				OptionPanel.getInstance().showOptionPanel();
			}
		}
	}

	
}
