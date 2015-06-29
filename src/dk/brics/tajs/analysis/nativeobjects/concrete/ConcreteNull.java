package dk.brics.tajs.analysis.nativeobjects.concrete;

public class ConcreteNull implements ConcreteValue {

    @Override
    public String toSourceCode() {
        return "null";
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }
}
