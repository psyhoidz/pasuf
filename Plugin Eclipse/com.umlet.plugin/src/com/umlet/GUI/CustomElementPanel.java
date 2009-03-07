package com.umlet.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import com.umlet.control.diagram.DiagramHandler;
import com.umlet.control.diagram.DrawPanel;
import com.umlet.custom.CustomElementHandler;

@SuppressWarnings("serial")
public class CustomElementPanel extends JPanel {
	
	private JSplitPane custompanelsplitleft;
	private JSplitPane custompanelsplitright;
	private JLabel savelabel;
	private CustomElementHandler customhandler;
	
	public CustomElementPanel(CustomElementHandler customhandler)
	{    
		this.customhandler = customhandler;
	    this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	    
	    JPanel custompanel2 = new JPanel();
	    custompanel2.setLayout(new BoxLayout(custompanel2,BoxLayout.Y_AXIS));
	    
	    JTextPane customcodepane = customhandler.getCodePane();
	    JScrollPane scr = new JScrollPane(customcodepane);
	    scr.setBorder(null);
	    scr.setAlignmentX(Component.LEFT_ALIGNMENT);
	    JLabel codelabel = new JLabel(" Code");
	    codelabel.setFont(new Font("SansSerif", Font.BOLD,11));
	    codelabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    custompanel2.add(codelabel);
	    custompanel2.add(scr);
	    
	    JPanel custompanel3 = new JPanel();
	    custompanel3.setLayout(new BoxLayout(custompanel3,BoxLayout.Y_AXIS));
	    DiagramHandler d = customhandler.getPreviewHandler();
	    DrawPanel custompreviewpanel = d.getDrawPanel();
	    custompreviewpanel.getScrollPanel().setAlignmentX(Component.LEFT_ALIGNMENT);
	    JLabel previewlabel = new JLabel(" Preview");
	    previewlabel.setFont(new Font("SansSerif", Font.BOLD,11));
	    previewlabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    JPanel labelpanel = new JPanel();
	    labelpanel.setLayout(new BoxLayout(labelpanel,BoxLayout.Y_AXIS));
	    labelpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
	    savelabel = new JLabel(" Add to diagram and close editor") {
	    	private boolean enabled=false;
	    	@Override
	    	public void setEnabled(boolean en) {
	    		if(!enabled && en) {
	    			this.enabled = en;
	    			this.addMouseListener(MenuListener.getInstance());
	    			this.addMouseMotionListener(MenuListener.getInstance());
	    		}
	    		else if(enabled && !en){
	    			this.enabled = en;
	    			this.removeMouseListener(MenuListener.getInstance());
	    			this.removeMouseMotionListener(MenuListener.getInstance());
	    		}
	    	}
	    };
	    savelabel.setFont(new Font("SansSerif", Font.BOLD,11));
	    savelabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
	    savelabel.setEnabled(true);

	    JLabel discardlabel = new JLabel("Discard and close editor");
	    discardlabel.setFont(new Font("SansSerif", Font.BOLD,11));
	    discardlabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
	    discardlabel.addMouseListener(MenuListener.getInstance());
	    discardlabel.addMouseMotionListener(MenuListener.getInstance());

	    custompanel3.add(previewlabel);
	    custompanel3.add(custompreviewpanel.getScrollPanel());
	    custompanel3.add(labelpanel);
	    labelpanel.add(Box.createHorizontalGlue());
	    labelpanel.add(savelabel);
	    labelpanel.add(discardlabel);
	    
	    custompanelsplitleft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,null,custompanel2);
	    custompanelsplitleft.setDividerSize(2);
	    custompanelsplitleft.setResizeWeight(0);
	    custompanelsplitleft.setBorder(null);
	    custompanelsplitright = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,custompanelsplitleft,custompanel3);
	    custompanelsplitright.setDividerSize(2);
	    custompanelsplitright.setResizeWeight(1);
	    custompanelsplitright.setBorder(null);
	    
	    this.add(custompanelsplitright);
	}
	
	public void setCustomElementSaveable(boolean enable) {
		this.customhandler.getPreviewHandler().getDrawPanel().setEnabled(enable);
		this.savelabel.setEnabled(enable);
		if(enable)
			this.savelabel.setForeground(Color.black);
		else
			this.savelabel.setForeground(Color.gray);
	}
	
	//specifies if the custom element is opened as new element or as edited element
	public void setCustomElementIsNew(boolean isnew) {
		if(isnew)
			this.savelabel.setText("Add to diagram and close editor");
		else
			this.savelabel.setText("Update in diagram and close editor");
	}
	
	public JSplitPane getLeftSplit() {
		return this.custompanelsplitleft;
	}
	
	public JSplitPane getRightSplit() {
		return this.custompanelsplitright;
	}
}
