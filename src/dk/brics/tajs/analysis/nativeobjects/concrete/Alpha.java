package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Converts concrete values to abstract values.
 */
public class Alpha {

    public static Value createNewArrayValue(ConcreteArray array, State state, AbstractNode sourceNode) {
        final Map<String, Value> map = newMap();
        map.put("<CONCRETE>", Value.makeStr(array.toSourceCode()));
        ObjectLabel label = new ObjectLabel(sourceNode, ObjectLabel.Kind.ARRAY, new HeapContext(null, map));
        state.newObject(label);
        state.writeInternalPrototype(label, Value.makeObject(InitialStateBuilder.ARRAY_PROTOTYPE));
        Value length = Value.makeNum(array.getLength());
        Set<ObjectLabel> labels = singleton(label);
        state.writePropertyWithAttributes(labels, "length", length.setAttributes(true, true, false));
        array.getExtraProperties().forEach((String k, ConcreteValue v) -> state.writeProperty(labels, Value.makeTemporaryStr(k), toValue(v), true, false));
        for (int i = 0; i < array.getLength(); i++) {
            final Value index = Value.makeStr(String.valueOf(i));
            ConcreteValue concreteValue = array.get(i);
            final Value value = toValue(concreteValue);
            state.writeProperty(labels, index, value, true, false);
        }
        return Value.makeObject(label);
    }

    public static Value toValue(PrimitiveConcreteValue concreteValue) {
        return toValue((ConcreteValue) concreteValue);
    }

    private static Value toValue(ConcreteValue concreteValue) { // convenience signature to be used internally
        return concreteValue.accept(new ConcreteValueVisitor<Value>() {
            @Override
            public Value visit(ConcreteNumber v) {
                double number = v.getNumber();
                if (Double.isNaN(number)) {
                    return Value.makeNumNaN();
                }
                if (Double.isInfinite(number)) {
                    return Value.makeNumInf();
                }
                return Value.makeNum(number);
            }

            @Override
            public Value visit(ConcreteString v) {
                return Value.makeStr(v.getString());
            }

            @Override
            public Value visit(ConcreteArray v) {
                throw new IllegalArgumentException("Can only create arrays explicitly: use createNewArrayValue instead!");
            }

            @Override
            public Value visit(ConcreteUndefined v) {
                return Value.makeUndef();
            }

            @Override
            public Value visit(ConcreteRegularExpression v) {
                throw new IllegalArgumentException("Can not create new regular expressions");
            }

            @Override
            public Value visit(ConcreteNull v) {
                return Value.makeNull();
            }

            @Override
            public Value visit(ConcreteBoolean v) {
                return Value.makeBool(v.getBooleanValue());
            }
        });
    }
}
