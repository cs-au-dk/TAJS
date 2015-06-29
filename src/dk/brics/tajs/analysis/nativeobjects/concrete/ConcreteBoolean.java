package dk.brics.tajs.analysis.nativeobjects.concrete;

public class ConcreteBoolean implements PrimitiveConcreteValue {

    private final boolean booleanValue;

    public ConcreteBoolean(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    @Override
    public String toSourceCode() {
        return String.valueOf(booleanValue);
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }
}
