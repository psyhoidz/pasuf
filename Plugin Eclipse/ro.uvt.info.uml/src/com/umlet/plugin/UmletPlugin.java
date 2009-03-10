package com.umlet.plugin;

import org.osgi.framework.BundleContext;
import org.eclipse.ui.plugin.*;
import org.eclipse.core.resources.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Path;

/**
 * The main plugin class to be used in the desktop.
 */
public class UmletPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static UmletPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
         * [UB]: Changed to default constructor
         *       for eclipse-3.0 compatibility
	 */
	public UmletPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle= ResourceBundle.getBundle("com.umlet.plugin.UmletPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}
  
        /**
         * This method is called upon plug-in activation
         * [UB]: added this standard method
         */
        public void start(BundleContext context) throws Exception {
                super.start(context);
        }

        /**
         * This method is called when the plug-in is stopped
         * [UB]: added this standard method
         */
        public void stop(BundleContext context) throws Exception {
                super.stop(context);
        }

  
	/**
	 * Returns the shared instance.
	 */
	public static UmletPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle= UmletPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public static String getHomePath() {
		String thePath=null;
		try { //LME: load palette from plugin's homedirectory
            //System.out.println("loading palette from url...");
            URL homeURL = Platform.find(UmletPlugin.getDefault().getBundle(), new Path("/"));
            
            //LME: truncate the URL string to an absolute directory path
            thePath =Platform.asLocalURL(homeURL).toString().substring(new String("file:/").length());
            if (System.getProperty("file.separator").equals("/")) //[UB]: if UNIX
            	  thePath= "/" + thePath;
        }
        catch (IOException ioe) {
            System.err.println("Umlet->init():"+ioe);
        }
		return thePath;
	}
}
