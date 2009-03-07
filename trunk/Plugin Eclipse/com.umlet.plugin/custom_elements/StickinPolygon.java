import java.awt.*;
import java.util.*;

import com.umlet.constants.Constants;

@SuppressWarnings("serial")
public class <!CLASSNAME!> extends com.umlet.custom.CustomElement {

	public CustomElementImpl()
	{
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Constants.decomposeStrings(this.getState());
	
		/****CUSTOM_CODE START****/
//This is a tutorial for the Stickingpolygon

//calculates the right height for the text to be centered
int y=height/2 - textheight() * textlines.size()*2/3;

//draws the text in the usual way
for(String textline : textlines) {
	y = y + textheight();
	printCenter(textline,y);
}

//draws the outer border of the diamond
Polygon p = new Polygon();
p.addPoint(width/2,5);
p.addPoint(width-5,height/2);
p.addPoint(width/2,height-5);
p.addPoint(5,height/2);
drawPolygon(p);

//draws the stickingpolygon
//the stickinpolygon is only visible if the element is selected
//it defines the polygon on which relations will stick
//if no stickingpolygon is specified the default polygon is applied (rectangle)
addStickingPoint(width/2,0);
addStickingPoint(width,height/2);
addStickingPoint(width/2,height);
addStickingPoint(0,height/2);
addStickingPoint(width/2,0);
		/****CUSTOM_CODE END****/
	}
}