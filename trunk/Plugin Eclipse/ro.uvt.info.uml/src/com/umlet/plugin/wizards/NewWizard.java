package com.umlet.plugin.wizards;

import org.eclipse.core.resources.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.*;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.*;




public class NewWizard
    extends Wizard
    implements INewWizard
{
    private IStructuredSelection mSelection = null;
    private WizardNewFileCreationPage firstPage = null; //[UB]:changed from default to private access

    public boolean performFinish(  )
    {  
		String fname=firstPage.getFileName();
		if (!fname.toLowerCase().endsWith(".uxf")) {
			firstPage.setFileName(fname+".uxf");
		}
		
		if (firstPage.getErrorMessage()!=null) return false;
		
        IFile file = firstPage.createNewFile( );
       
        return true;
    }

    public void init( IWorkbench workbench, IStructuredSelection selection )
    {
        this.mSelection = selection;
    }

    public void addPages(  )
    {
        firstPage = new WizardNewFileCreationPage("New Umlet Diagram",
                mSelection );
        firstPage.setTitle( "New Umlet Diagram" );
        firstPage.setDescription( "Enter file name." );
		
        addPage( firstPage );

    }
}
