package vizitor2;

class Engine implements CarElement{
    public void accept(CarElementVisitor visitor) {
        visitor.visit(this);
    }
}