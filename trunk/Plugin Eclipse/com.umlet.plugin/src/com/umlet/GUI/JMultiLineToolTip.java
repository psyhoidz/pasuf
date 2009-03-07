package com.umlet.GUI;

import javax.swing.JComponent;
import javax.swing.JToolTip;

@SuppressWarnings("serial")
public class JMultiLineToolTip extends JToolTip{	
	String tipText;
	JComponent component;
	
	public JMultiLineToolTip() {
	    updateUI();
	}
	
	public void updateUI() {
	    setUI(MultiLineToolTipUI.createUI(this));
	}
	
	public void setColumns(int columns)
	{
		this.columns = columns;
		this.fixedwidth = 0;
	}
	
	public int getColumns()
	{
		return columns;
	}
	
	public void setFixedWidth(int width)
	{
		this.fixedwidth = width;
		this.columns = 0;
	}
	
	public int getFixedWidth()
	{
		return fixedwidth;
	}

	protected int columns = 0;
	protected int fixedwidth = 0;
}
