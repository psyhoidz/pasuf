package com.umlet.element.custom;

import java.awt.*;
import java.util.Vector;
import com.umlet.control.*;
import com.umlet.element.base.Entity;

public class State extends Entity
{
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(Constants.getFont());
		g2.setColor(_activeColor);
		Constants.getFRC(g2);

		Vector<String> tmp = Constants.decomposeStrings(this.getPanelAttributes(), "\n");
		int yPos = 0;
		// A.Mueller start
		if (tmp.contains("--") || tmp.contains("-."))
			yPos = 2 * Constants.getDistLineToText();
		else
			// A.Mueller end
			yPos = this.getHeight() / 2 - tmp.size() * (Constants.getFontsize() + Constants.getDistTextToText()) / 2;

		for (int i = 0; i < tmp.size(); i++)
		{
			String s = tmp.elementAt(i);
			// A.Mueller start
			if (s.equals("--"))
			{
				yPos += Constants.getDistTextToLine();
				g2.drawLine(0, yPos, getWidth(), yPos);
				yPos += Constants.getDistLineToText();
				//some other states will be drawn in this -> set transparent...
				this.setTransparentSelection(true);

			} else if (s.equals("-."))
			{
				yPos += Constants.getDistTextToLine();
				g2.setStroke(Constants.getStroke(1,1));
				g2.drawLine(0, yPos, getWidth(), yPos);
				g2.setStroke(Constants.getStroke(0,1));
				yPos += Constants.getDistLineToText();
				this.setTransparentSelection(true);
			} else
			{
				// A.Mueller end
				yPos += Constants.getFontsize();
				Constants.write(g2, s, (int) this.getWidth() / 2, yPos, true);
				yPos += Constants.getDistTextToText();
				// A.Mueller start
			}
			// A.Mueller end
		}

		g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 30, 30);
	}
}
