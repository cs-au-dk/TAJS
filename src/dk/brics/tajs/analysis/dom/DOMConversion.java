/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.Collections;

import java.util.Set;

public class DOMConversion {

    /**
     * Converts the given value to a DOMNode value.
     */
    public static Value toNode(Value v, Solver.SolverInterface c) {
        return toNativeObject(DOMObjects.NODE_PROTOTYPE, v, true, c);
    }

    /**
     * Converts the given value to a DOMAttr value.
     */
    public static Value toAttr(Value v, Solver.SolverInterface c) {
        return toNativeObject(DOMObjects.ATTR_PROTOTYPE, v, true, c);
    }

    /**
     * Converts the given value to a HTMLElement value.
     */
    public static Value toHTMLElement(Value v, Solver.SolverInterface c) {
        return toNativeObject(DOMObjects.HTMLELEMENT_PROTOTYPE, v, true, c);
    }

    /**
     * Converts the given value to an EventTarget value.
     */
    public static Value toEventTarget(Value v, Solver.SolverInterface c) {
        return toNativeObject(DOMObjects.NODE_PROTOTYPE, v, true, c);
    }

    /**
     * Converts the given value to the given NativeObject (optionally following
     * the prototype chains).
     *
     * @param nativeObject target NativeObject
     * @param value        Value to convert
     * @param prototype    use the prototype chain?
     */
    public static Value toNativeObject(HostObject nativeObject, Value value, boolean prototype, Solver.SolverInterface solverInterface) {
        State state = solverInterface.getState();
        boolean bad = false;
        Set<ObjectLabel> matches = Collections.newSet();

        if (prototype) {
            // Make lookup using prototype chains
            Set<ObjectLabel> objectLabels = Collections.newSet(value.getObjectLabels());
            Set<ObjectLabel> visited = Collections.newSet();

            while (!objectLabels.isEmpty()) {
                Set<ObjectLabel> temp = Collections.newSet();
                for (ObjectLabel objectLabel : objectLabels) {
                    if (!visited.contains(objectLabel)) {
                        visited.add(objectLabel);
                        Value prototypeValue = state.readInternalPrototype(java.util.Collections.singleton(objectLabel));
                        prototypeValue = UnknownValueResolver.getRealValue(prototypeValue, state);

                        // FIXME: Needs code review. Looks fishy to compare objects with toString(). (GitHub #406)
                        String nativeObjectPrototype = nativeObject.toString();
                        String objectLabelPrototype = "";
                        if (objectLabel.getHostObject() != null) {
                            objectLabelPrototype = objectLabel.getHostObject().toString();
                        }

                        if (nativeObject == objectLabel.getHostObject() || nativeObjectPrototype.equals(objectLabelPrototype)) {
                            matches.add(objectLabel);
                        } else if (prototypeValue.getObjectLabels().isEmpty()) {
                            bad = true;
                        } else {
                            temp.addAll(prototypeValue.getObjectLabels());
                        }
                    }
                }
                objectLabels = temp;
            }
        } else {
            // Make lookup ignoring prototype chains

            // TODO: Verify this
            for (ObjectLabel objectLabel : value.getObjectLabels()) {
                if (objectLabel.getHostObject() == nativeObject
                        || (objectLabel.getHostObject() != null && objectLabel.getHostObject().toString().equals(nativeObject + ".prototype"))) {
                    matches.add(objectLabel);
                } else {
                    bad = true;
                }
            }
        }

//        Message.Status status;
//        if (good && bad) {
//            status = Message.Status.MAYBE;
//        } else if (!good && bad) {
//            status = Message.Status.CERTAIN;
//        } else if (good && !bad) {
//            status = Message.Status.NONE;
//        } else if (!good && !bad) { // equivalent to Value of Undef / a null argument
//            // Considered a certain type error.
//            status = Message.Status.CERTAIN;
//        } else {
//            throw new AnalysisException("toNativeObject: fell through cases - should not happen.");
//        }
        if (bad) {
            String message = "TypeError, argument is not of expected type: " + nativeObject;
            solverInterface.getMonitoring().addMessage(solverInterface.getNode(), Message.Severity.HIGH, message);
        }

        return Value.makeObject(matches);
    }
}
