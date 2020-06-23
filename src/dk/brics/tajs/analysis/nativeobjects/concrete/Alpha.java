/*
 * Copyright 2009-2020 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.JSArray;
import dk.brics.tajs.analysis.nativeobjects.JSRegExp;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.Value;

import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Converts concrete values to abstract values.
 */
public class Alpha {

    private static Context.Qualifier concreteValueQualifier = new Context.Qualifier() {
        @Override
        public String toString() {
            return "<CONCRETE>";
        }
    };

    private static Value createNewArrayValue(ConcreteArray array, AbstractNode sourceNode, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        final Map<Context.Qualifier, Value> map = newMap();
        map.put(concreteValueQualifier, Value.makeStr(array.toSourceCode()));
        ObjectLabel label = JSArray.makeArray(sourceNode, Value.makeNum(array.getLength()), Context.makeQualifiers(map), c);
        Set<ObjectLabel> labels = singleton(label);
        array.getExtraProperties().forEach((PKey k, ConcreteValue v) -> pv.writeProperty(labels, k.toValue(), toValue(v, c), false, true));
        for (int i = 0; i < array.getLength(); i++) {
            final Value index = Value.makeStr(String.valueOf(i));
            ConcreteValue concreteValue = array.get(i);
            final Value value = toValue(concreteValue, c);
            pv.writeProperty(labels, index, value, false, true);
        }
        return Value.makeObject(label);
    }

    private static Value createNewRegExpValue(ConcreteRegularExpression regExp, AbstractNode sourceNode, Solver.SolverInterface c) {
        final Map<Context.Qualifier, Value> map = newMap();
        map.put(concreteValueQualifier, Value.makeStr(regExp.toSourceCode()));
        ObjectLabel label = JSRegExp.makeRegExp(sourceNode, regExp.getSource().getString(), regExp.getGlobal().getBooleanValue(), regExp.getIgnoreCase().getBooleanValue(), regExp.getMultiline().getBooleanValue(), regExp.getLastIndex().getNumber(), Context.makeQualifiers(map), c);
        return Value.makeObject(label);
    }

    /**
     * Converts a concrete value to an abstract value.
     *
     * @param concreteValue value to abstract
     * @param c             used for allocating new objects
     */
    public static Value toValue(ConcreteValue concreteValue, Solver.SolverInterface c) {
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
                return createNewArrayValue(v, c.getNode(), c);
            }

            @Override
            public Value visit(ConcreteUndefined v) {
                return Value.makeUndef();
            }

            @Override
            public Value visit(ConcreteRegularExpression v) {
                return createNewRegExpValue(v, c.getNode(), c);
            }

            @Override
            public Value visit(ConcreteNull v) {
                return Value.makeNull();
            }

            @Override
            public Value visit(ConcreteNullOrUndefined v) {
                return Value.makeNull().join(Value.makeUndef());
            }

            @Override
            public Value visit(ConcreteBoolean v) {
                return Value.makeBool(v.getBooleanValue());
            }
        });
    }
}
