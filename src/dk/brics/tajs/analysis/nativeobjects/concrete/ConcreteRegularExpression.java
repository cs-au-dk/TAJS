package dk.brics.tajs.analysis.nativeobjects.concrete;

public class ConcreteRegularExpression implements ConcreteValue {

    private final ConcreteString source;

    private final ConcreteBoolean global;

    private final ConcreteBoolean ignoreCase;

    private final ConcreteBoolean multiline;

    public ConcreteRegularExpression(ConcreteString source, ConcreteBoolean global, ConcreteBoolean ignoreCase, ConcreteBoolean multiline) {
        this.source = source;
        this.global = global;
        this.ignoreCase = ignoreCase;
        this.multiline = multiline;
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public ConcreteBoolean getGlobal() {
        return global;
    }

    public ConcreteBoolean getIgnoreCase() {
        return ignoreCase;
    }

    public ConcreteBoolean getMultiline() {
        return multiline;
    }

    public ConcreteString getSource() {
        return source;
    }

    @Override
    public String toSourceCode() {
        return String.format("/%s/%s%s%s", source.toRegExpSourceCodeComponent(), global.getBooleanValue() ? "g" : "", ignoreCase.getBooleanValue() ? "i" : "", multiline.getBooleanValue() ? "m" : "");
    }
}
