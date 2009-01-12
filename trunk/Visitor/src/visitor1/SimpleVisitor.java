package visitor1;

public class SimpleVisitor extends ComponentVisitor {
	
	public SimpleVisitor() {
		
	}
	
	public void visit (Widget w) {
    	System.out.println("Visiting a Widget");
	}
    
	public void visit (WidgetAssembly wa) {
    	System.out.println("Visiting a WidgetAssembly");
	}

}
