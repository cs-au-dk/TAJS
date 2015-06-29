package dk.brics.tajs.analysis.nativeobjects.concrete;

public class ConcreteString implements PrimitiveConcreteValue {

    private final String string;

    public ConcreteString(String string) {
        this.string = string;
    }

    @Override
    public String toSourceCode() {
        // escape quotes and newlines in concrete string, wrap in quotes
        return String.format("\"%s\"", string.replace("\"", "\\\"").replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n"));
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public String getString() {
        return string;
    }
}
