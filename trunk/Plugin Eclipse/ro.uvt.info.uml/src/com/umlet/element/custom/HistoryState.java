package com.umlet.element.custom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;

import com.umlet.control.Constants;
import com.umlet.element.base.*;

public class HistoryState extends Entity
{

	
	public void paint(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		g2.fillOval(0,0,this.getWidth(),this.getHeight());
//       Measure the font and the message
        Rectangle2D bounds = Constants.getFont().getStringBounds("H", Constants.getFRC(g2));
        LineMetrics metrics = Constants.getFont().getLineMetrics("H", Constants.getFRC(g2));
        float width = (float) bounds.getWidth();     // The width of our text
        float lineheight = metrics.getHeight();      // Total line height
        float ascent = metrics.getAscent();          // Top of text to baseline

//       Now display the message centered horizontally and vertically in this
        float x0 = (float) ( (this.getWidth() - width)/2);
        float y0 = (float) ( (this.getHeight() - lineheight)/2 + ascent);
        g2.setColor(Color.WHITE);
        g2.drawString("H", x0, y0);
		g2.setColor(_activeColor);
		
	}
	public int getPossibleResizeDirections() {return 0;} //deny size changes
	
}
