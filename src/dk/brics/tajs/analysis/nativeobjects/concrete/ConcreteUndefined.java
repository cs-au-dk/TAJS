package dk.brics.tajs.analysis.nativeobjects.concrete;

public class ConcreteUndefined implements PrimitiveConcreteValue {

    @Override
    public String toSourceCode() {
        return "undefined";
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }
}
