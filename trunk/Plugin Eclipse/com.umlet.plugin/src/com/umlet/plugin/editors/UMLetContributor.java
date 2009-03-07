package com.umlet.plugin.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.umlet.GUI.OptionPanel;
import com.umlet.GUI.eclipse.EclipseGUI;
import com.umlet.GUI.eclipse.UpdateActionBars;
import com.umlet.control.BrowserLauncher;
import com.umlet.control.TemplateSorter;
import com.umlet.control.Umlet;
import com.umlet.control.command.Copy;
import com.umlet.control.command.Cut;
import com.umlet.control.command.Paste;
import com.umlet.control.command.RemoveElement;
import com.umlet.control.diagram.DiagramHandler;
import com.umlet.custom.CustomElement;
import com.umlet.element.base.Entity;
import com.umlet.help.AboutWindow;
import com.umlet.plugin.UmletPlugin;

public class UMLetContributor extends EditorActionBarContributor {

	private Action help;
	private Action about;
	private Action expjpg;
	private Action expsvg;
	private Action exppdf;
	private Action expeps;
	private Action customnew;
	private List<String> newfromtemplate;
	private Hashtable<String, Action> templateactions;
	private Action customedit;
	private Action options;
	
    private IAction undoAction;
    private IAction redoAction;
    private IAction copyAction;
    private IAction cutAction;
    private IAction pasteAction;
    private IAction deleteAction;
    private IAction selectallAction;
    private IAction searchAction;
   
    
    
    private boolean custom_panel_shown;
    private boolean custom_element_selected;
    
	public UMLetContributor() {
		this.newfromtemplate = new ArrayList<String>();
		this.templateactions = new Hashtable<String,Action>();
		createMenuActions();
		this.custom_panel_shown = false;
		this.custom_element_selected = false;
	}

	@Override
	public void init(IActionBars actionBars) {
		super.init(actionBars);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
	    
		// actions of the eclipse menu
	    undoAction = new Action() {
	    	public void run() {
	    		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
	    		if(handler != null) {
	    			handler.getDrawPanel().getSelector().deselectAll();
					handler.getController().undo();
	    		}
	    	}
	    };
	    undoAction.setImageDescriptor(
	    		sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
	    
	    redoAction = new Action() {
	    	public void run() {
	    		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
	    		if(handler != null) 
					handler.getController().redo();
	    	}
	    };
	    redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
	    
	    copyAction = new Action() {
	    	public void run() {
	    		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
	    		if(handler != null) 
					(new Copy()).execute(handler);
	    	}
	    };
	    copyAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
	    
	    cutAction = new Action() {
	    	public void run() {
	    		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
	    		if(handler != null) 
	    			handler.getController().executeCommand(new Cut());
	    	}
	    };
	    cutAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
	    cutAction.setEnabled(false);
	    
	    pasteAction = new Action() {
	    	public void run() {
	    		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
	    		if(handler != null) 
	    			handler.getController().executeCommand(new Paste());
	    	}
	    };
	    pasteAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
	    pasteAction.setEnabled(false);
	    
	    deleteAction = new Action() {
	    	public void run() {
	    		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
	    		if(handler != null) { 
	    			Vector<Entity> v = handler.getDrawPanel().getSelector().getSelectedEntities();
					if (v.size() > 0) {
						handler.getController().executeCommand(
								new RemoveElement(v));
					}	
	    		}
	    	}
	    };
	    deleteAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	    deleteAction.setEnabled(false);
	    
	    selectallAction = new Action() {
	    	public void run() {
	    		DiagramHandler handler = Umlet.getInstance().getDiagramHandler();
	    		if(handler != null) 
	    			handler.getDrawPanel().getSelector().selectAll();
	    	}
	    };
	    
	    searchAction = new Action() {
	    	public void run() {
	    		Umlet.getInstance().getGUI().enableSearch(true);
	    	}
	    };
	    
	    actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
	    actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
	    actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
	    actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);
	    actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);
	    actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
	    actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectallAction);
	    actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), searchAction);
	    
		if(UmletPlugin.getGUI() instanceof EclipseGUI)
			((EclipseGUI)UmletPlugin.getGUI()).setContributor(this);
	}
	
	private void createMenuActions() {
		help = new Action("Online Help") {
			public void run() {
				BrowserLauncher.openURL("http://umlet.com.hosting.domaindirect.com/faq.htm");
			}
		};
		about = new Action("About UMLet") {
			public void run() {
				AboutWindow.getInstance().setVisible();
			}
		};
		this.expeps = new Action("Export to EPS") {
			public void run() {
				if(UMLetEditor.getCurrent() != null)
					UMLetEditor.getCurrent().exportToFormat("EPS");
			}
		};
		this.expjpg = new Action("Export to JPG") {
			public void run() {
				if(UMLetEditor.getCurrent() != null)
					UMLetEditor.getCurrent().exportToFormat("JPG");
			}
		};
		this.exppdf = new Action("Export to PDF") {
			public void run() {
				if(UMLetEditor.getCurrent() != null)
					UMLetEditor.getCurrent().exportToFormat("PDF");
			}
		};
		this.expsvg = new Action("Export to SVG") {
			public void run() {
				if(UMLetEditor.getCurrent() != null)
					UMLetEditor.getCurrent().exportToFormat("SVG");
			}
		};
		this.customnew = new Action("New...") {
			public void run() {
				if(Umlet.getInstance().getGUI().getCurrentCustomHandler() != null)
				{
					if(Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity())
					{
						Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().newEntity();
					}
				}
			}
		};
		
	    this.newfromtemplate = Umlet.getInstance().getTemplateNames();
	    for(String template : this.newfromtemplate) {
	    	Action tmp = new Action(template) {
	    		public void run()
	    		{
	    			if(Umlet.getInstance().getGUI().getCurrentCustomHandler() != null)
	    			{
		    			if(Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity())
		    			{
		    				Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
		    				Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(true);
		    				Umlet.getInstance().getGUI().getCurrentCustomHandler().newEntity(this.getText());
		    			}
	    			}
	    		}
	    	};
	    	this.templateactions.put(template, tmp);
	    }
	    
	    this.customedit = new Action("Edit Selected...") {
			public void run() { //copied from menulistener
				Entity entity = Umlet.getInstance().getEditedEntity();
				if(entity instanceof CustomElement && entity != null && Umlet.getInstance().getGUI().getCurrentCustomHandler() != null)
				{
					if(Umlet.getInstance().getGUI().getCurrentCustomHandler().closeEntity())
					{
						Umlet.getInstance().getGUI().setCustomPanelEnabled(true);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().getPanel().setCustomElementIsNew(false);
						Umlet.getInstance().getGUI().getCurrentCustomHandler().editEntity((CustomElement)entity);
					}
				}
			}
		};
		this.customedit.setEnabled(false);
		
		this.options = new Action("Options") {
			public void run() {
				OptionPanel.getInstance().showOptionPanel();
			}
		};
	}
	
	@Override
	public void contributeToMenu(IMenuManager manager) {
		if(UmletPlugin.getGUI() instanceof EclipseGUI)
			((EclipseGUI)UmletPlugin.getGUI()).setContributor(this);
		
		IMenuManager menu = new MenuManager("UMLet");
		IMenuManager export = new MenuManager("Export");
		IMenuManager custom = new MenuManager("Custom Elements");
		IMenuManager templates = new MenuManager("New from template");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		export.add(this.expeps);
		export.add(this.expjpg);
		export.add(this.exppdf);
		export.add(this.expsvg);
		
		Collections.sort(this.newfromtemplate, new TemplateSorter());
		for(String name : this.newfromtemplate)
			templates.add(this.templateactions.get(name));
		
		custom.add(this.customnew);
		custom.add(templates);
		custom.add(this.customedit);
		
		menu.add(export);
		menu.add(custom);
		menu.add(this.options);
		menu.add(this.about);
		menu.add(this.help);
	}
	
	public void enablePaste() {
		this.pasteAction.setEnabled(true);
	}
	
	public void setCustomElementSelected(boolean selected) {
		this.custom_element_selected = selected;
		this.customedit.setEnabled(selected && !this.custom_panel_shown);
	}
	
	public void setElementsSelected(int count) {
		if(count > 0)
		{
			this.cutAction.setEnabled(true);
			this.deleteAction.setEnabled(true);
		}
		else
		{
			this.deleteAction.setEnabled(false);
			this.cutAction.setEnabled(false);
		}
	}
	
	public void setCustomPanelEnabled(boolean enable) {
		this.custom_panel_shown = enable;
		this.customedit.setEnabled(!enable && this.custom_element_selected);
		this.customnew.setEnabled(!enable);
		for(Action a : this.templateactions.values())
			a.setEnabled(!enable);
		this.searchAction.setEnabled(!enable);
	}
	
	public void textpanelfocused(boolean focused) {
		if(focused)
		{
		    this.getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), null);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), null);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), null);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), null);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), null);	
		}
		else
		{
			this.getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
			this.getActionBars().setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectallAction);
		    this.getActionBars().setGlobalActionHandler(ActionFactory.FIND.getId(), searchAction);
		}
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new UpdateActionBars(this.getActionBars()));
	}
}
