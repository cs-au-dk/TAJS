/*
 * Copyright 2009-2019 Aarhus University
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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.HeapContext;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newMap;

/**
 * The NodeList interface provides the abstraction of an ordered collection of
 * nodes, without defining or constraining how this collection is implemented.
 * NodeList objects in the DOM are live.
 * <p>
 * The items in the NodeList are accessible via an integral index, starting from
 * 0.
 *
 * @author magnusm
 */
public class DOMNodeList {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.NODELIST_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.NODELIST_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.NODELIST_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "NodeList", Value.makeObject(CONSTRUCTOR));

        // Prototype object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        createDOMProperty(INSTANCES, "length", Value.makeAnyNumUInt().setReadOnly(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        createDOMFunction(PROTOTYPE, DOMObjects.NODELIST_ITEM, "item", 1, c);
    }

    /**
     * Transfer Functions.
     */
    public static Value evaluate(DOMObjects nativeobject, @SuppressWarnings("unused") CallInfo call, @SuppressWarnings("unused") Solver.SolverInterface c) {
        switch (nativeobject) {
            case NODELIST_ITEM: {
                DOMFunctions.expectParameters(nativeobject, call, c, 1, 1);
                if (call.isUnknownNumberOfArgs()) {
                    Exceptions.throwTypeError(c);
                } else if (call.getNumberOfArgs() == 0) {
                    Exceptions.throwTypeError(c);
                    return Value.makeNone();
                }
                Value index = FunctionCalls.readParameter(call, c.getState(), 0);
                return c.getAnalysis().getPropVarOperations().readPropertyValue(c.getState().readThisObjects(), index);
            }
            default: {
                throw new AnalysisException(call.getJSSourceNode().getSourceLocation() + ": Unknown Native Object: " + nativeobject);
            }
        }
    }

    private static ObjectLabel makeAllocationSiteInstance(AbstractNode node, State s) {
        Map<String, Value> qualifier = newMap();
        qualifier.put("EXTRA_LABEL_KIND", Value.makeStr("DOMNodeList"));
        HeapContext ctx = HeapContext.make(null, qualifier);
        ObjectLabel label = ObjectLabel.make(node, ObjectLabel.Kind.OBJECT, ctx);
        s.newObject(label);
        s.writeInternalPrototype(label, Value.makeObject(PROTOTYPE));
        return label;
    }

    public static Value makeNaiveInstance() {
        return Value.makeObject(INSTANCES);
    }

    /**
     * Precise altenative to {@link #makeNaiveInstance()}: allocates a new DOMNodeList at the given allocation site. The content of the list is unordered.
     */
    public static ObjectLabel makeAllocationSiteInstanceWithUnorderedContent(AbstractNode node, Set<ObjectLabel> content, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ObjectLabel nodeList = makeAllocationSiteInstance(node, c.getState());
        if (!content.isEmpty()) {
            pv.writeProperty(Collections.singleton(nodeList), Value.makeAnyStrUInt(), Value.makeObject(content));
            pv.writeProperty(nodeList, "length", Value.makeAnyNumUInt().setReadOnly());
        } else {
            pv.writeProperty(nodeList, "length", Value.makeNum(0).setReadOnly()); // TODO install setter for changes to the list (GitHub #359)
        }
        return nodeList;
    }
}
