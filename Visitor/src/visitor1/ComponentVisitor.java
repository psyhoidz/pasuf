package visitor1;

public abstract class ComponentVisitor {
	
	public abstract void visit(Widget w);
	public abstract void visit(WidgetAssembly wa);
    
}
