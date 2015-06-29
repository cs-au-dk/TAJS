package dk.brics.tajs.analysis.nativeobjects.concrete;

public class ConcreteNumber implements PrimitiveConcreteValue {

    private final double number;

    public ConcreteNumber(Double number) {
        this.number = number;
    }

    @Override
    public String toSourceCode() {
        String formatted = String.format("%.20f", number);
        return formatted;
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public double getNumber() {
        return number;
    }
}
