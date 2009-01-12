package vizitor2;

class Body implements CarElement{
    public void accept(CarElementVisitor visitor) {
        visitor.visit(this);
    }
}

