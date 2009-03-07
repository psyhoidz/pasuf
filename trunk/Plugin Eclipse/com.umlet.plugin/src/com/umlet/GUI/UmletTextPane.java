package com.umlet.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.umlet.constants.Constants;
import com.umlet.listeners.TextpaneFocusListener;

@SuppressWarnings("serial")
public class UmletTextPane extends JTextPane {
	
	private ArrayList<String> specialcommands;
	private JPanel panel;
	
	public UmletTextPane(JPanel panel) {
		specialcommands = new ArrayList<String>();
		specialcommands.add("bg=");
		specialcommands.add("fg=");
		specialcommands.add(Constants.autoresize);
		this.panel = panel;
		this.addFocusListener(new TextpaneFocusListener());
		
		StyledDocument doc = this.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		doc.addStyle("default", def);
		Style err = doc.addStyle("special", def);
		StyleConstants.setForeground(err, Color.gray);
	}
	
    public void setSize(Dimension d){
    	if(this.getHeight() != d.height)
    		super.setSize(new Dimension(1000,d.height));
    }
	
    public void checkCurrentLineForSpecialChars() 
    {
    	int caret = this.getCaretPosition();
    	String[] lines = this.getText().split(Constants.newline,-1);
    	int linenum=0, pos=0;
		for(; linenum < lines.length && pos < caret ; linenum++) {	
			pos += lines[linenum].length()+1;
		}
		if(linenum > 0) {
			linenum--;
			pos -= lines[linenum].length()+1;
		}
		
		boolean special = false;
		String line = lines[linenum];
		for(String comp : this.specialcommands) {
			if(line.startsWith(comp)) {
				special = true;
				break;
			}
		}
		if(special)
			this.getStyledDocument().setCharacterAttributes(pos,line.length(),this.getStyledDocument().getStyle("special"), true);
		else
			this.getStyledDocument().setCharacterAttributes(pos,line.length(),this.getStyledDocument().getStyle("default"), true);
    }
    
	public void checkPanelForSpecialChars()
	{
		String[] lines = this.getText().split(Constants.newline,-1);
		for(int linenum=0, pos=0; linenum < lines.length ; linenum++) {
			String line = lines[linenum];
			boolean special = false;
			for(String comp : this.specialcommands) {
				if(line.startsWith(comp)) {
					special = true;
					break;
				}
			}
			if(special)
				this.getStyledDocument().setCharacterAttributes(pos,line.length(),this.getStyledDocument().getStyle("special"), true);
			else
				this.getStyledDocument().setCharacterAttributes(pos,line.length(),this.getStyledDocument().getStyle("default"), true);
			
			pos += line.length()+1;
		}
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
}
