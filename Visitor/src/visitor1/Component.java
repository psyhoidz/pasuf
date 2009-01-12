package visitor1;

public abstract class Component {
	
    protected String name;
    
    public Component(String name) {
    	this.name = name;
    }
    
    public String getName() { 
    	return name; 
    }
    
    public void setName(String name) { 
    	this.name = name; 
    }
    
    public abstract double getPrice();
    
    public abstract void accept(ComponentVisitor v);

}