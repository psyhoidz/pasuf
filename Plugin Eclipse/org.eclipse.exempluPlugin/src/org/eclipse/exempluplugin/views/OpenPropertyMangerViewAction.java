/**
 * 
 */
package org.eclipse.exempluplugin.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

/**
 * @author Tudor
 *
 */
public class OpenPropertyMangerViewAction implements
		IWorkbenchWindowActionDelegate {

	IWorkbenchWindow activeWindow = null;
	private int instanceNum = 0;
	private String viewId = "org.eclipse.exempluplugin.views.SampleView";
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		activeWindow = window;
	}

	@Override
	public void run(IAction action) {
		Shell shell = activeWindow.getShell();

		if(activeWindow != null) {	
			try {
				activeWindow.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
			} catch (PartInitException e) {
				MessageDialog.openError(shell, 
						"Error", 
						"Error opening view:" + e.getMessage());
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
