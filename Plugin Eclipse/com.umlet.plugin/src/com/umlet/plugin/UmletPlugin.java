package com.umlet.plugin;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.umlet.GUI.eclipse.EclipseGUI;
import com.umlet.control.Umlet;

/**
 * The activator class controls the plug-in life cycle
 */
public class UmletPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.umlet.plugin";

	// The shared instance
	private static UmletPlugin plugin;

	private static EclipseGUI gui;
	
	public static EclipseGUI getGUI()
	{
		return gui;
	}
	
	/**
	 * The constructor
	 */
	public UmletPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		gui = new EclipseGUI(Umlet.getInstance());
		Umlet.getInstance().init(gui);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		Umlet.getInstance().getGUI().closeWindow();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UmletPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static void openPalette() {
		//NYI because would only be operable in eclipse 3.3
		//see eclipsepedia faq for more information.
		/*File fileToOpen = new File("externalfile.xml");
		
		if (fileToOpen.exists() && fileToOpen.isFile()) {
		    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
		    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		 
		    try {
		        IDE.openEditor...
		    } catch ( PartInitException e ) {
		    	
		    }
		}*/

	}
}
