package visitor1;

public class VisitorTest {

	public static void main (String[] args) {
		// Create some widgets.
		Widget w1 = new Widget("Widget1", 10.00);
		Widget w2 = new Widget("Widget2", 20.00);
		Widget w3 = new Widget("Widget3", 30.00);
		
		// Add then to a widget assembly.
		WidgetAssembly wa = new WidgetAssembly("Chassis");
		wa.addComponent(w1);
		wa.addComponent(w2);
		wa.addComponent(w3);
		
		// Visit some nodes with a SimpleVisitor.
		SimpleVisitor sv = new SimpleVisitor();
		w1.accept(sv);
		w2.accept(sv);
		w3.accept(sv);
		wa.accept(sv);
		
		// Visit some nodes with a PriceVisitor.
		PriceVisitor pv = new PriceVisitor(25.00);
		w1.accept(pv);
		w2.accept(pv);
		w3.accept(pv);
		wa.accept(pv);
	}

}
