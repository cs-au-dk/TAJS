package dk.brics.tajs.analysis.nativeobjects.concrete;

public class ConcreteString implements PrimitiveConcreteValue {

    private final String string;

    public ConcreteString(String string) {
        this.string = string;
    }

    private String escapeDoubleQuotesAndNewLines(String string) {
        return string.replace("\"", "\\\"").replaceAll("(\\r|\\n|\\r\\n|\u2028|\u2029)+", "\\\\n");
    }

    private String escapeBackslashes(String string) {
        return string.replace("\\", "\\\\");
    }

    @Override
    public String toSourceCode() {
        return String.format("\"%s\"", escapeDoubleQuotesAndNewLines(escapeBackslashes(string)));
    }

    public String toRegExpSourceCodeComponent() {
        return escapeDoubleQuotesAndNewLines(string);
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public String getString() {
        return string;
    }
}
