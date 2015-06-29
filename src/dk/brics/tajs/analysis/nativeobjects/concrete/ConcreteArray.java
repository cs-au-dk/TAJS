package dk.brics.tajs.analysis.nativeobjects.concrete;

import java.util.List;
import java.util.Map;

public class ConcreteArray implements ConcreteValue {

    private final List<ConcreteValue> array;

    private final Map<String, ConcreteValue> extraProperties;

    /**
     * Creates an array of concrete values, with some extra properties.
     * NB: the extra properties are *not* represented in the toSourceCode result!
     */
    public ConcreteArray(List<ConcreteValue> array, Map<String, ConcreteValue> extraProperties) {
        this.array = array;
        this.extraProperties = extraProperties;
        for (int i = 0; i < array.size(); i++) {
            extraProperties.remove(i + ""); // kill the numeric properties, to avoid double-representation
        }
    }

    public Map<String, ConcreteValue> getExtraProperties() {
        return extraProperties;
    }

    @Override
    public String toSourceCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (ConcreteValue argument : array) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(argument.toSourceCode());
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toSourceCode();
    }

    @Override
    public <T> T accept(ConcreteValueVisitor<T> v) {
        return v.visit(this);
    }

    public int getLength() {
        return array.size();
    }

    public ConcreteValue get(int i) {
        return array.get(i);
    }
}
