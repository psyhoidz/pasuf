/*
 * Created on 01.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.umlet.control.io;

/**
 * @author unknown
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface UmletPluginHandler {
	public boolean notifyIsClosing();
	public void notifyIsDirty(boolean isDirty);
	public boolean notifySave(boolean ask);
	public boolean notifySaveAsFormat(String format);
}
