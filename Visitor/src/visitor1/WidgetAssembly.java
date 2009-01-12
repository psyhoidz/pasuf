package visitor1;

import java.util.Enumeration;
import java.util.Vector;

public class WidgetAssembly extends Component {

	protected Vector<Component> components;

	public WidgetAssembly (String name) {
		super(name);
		components = new Vector<Component>();
	}
	
	public void addComponent (Component c) {
		components.addElement(c);
	}
	
	public void removeComponent (Component c) {
	components.removeElement(c);
	}
	
	public double getPrice() {
		double totalPrice = 0.0;
		Enumeration<Component> e = components.elements();
		
		while (e.hasMoreElements()) {
			totalPrice += ((Component) e.nextElement()).getPrice();
		}
		
		return totalPrice;
	}
	
	public void accept (ComponentVisitor v) {
		v.visit(this); 
	}
	
}