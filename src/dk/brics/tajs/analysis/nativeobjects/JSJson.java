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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

/**
 * Native JSON functions.
 */
public class JSJson {

    public static Value evaluate(ECMAScriptObjects nativeobject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        switch (nativeobject) {
            case JSON_PARSE: {
                return makeAnyJSONObject(c);
            }
            case JSON_STRINGIFY: {
                return Value.makeAnyStr().joinUndef(); // note: the result is 'undefined' if stringifying a function
            }
            default:
                return null;
        }
    }

    /**
     * Returns a Value representing all possible JSON objects.
     */
    public static Value makeAnyJSONObject(Solver.SolverInterface c) {
        State s = c.getState();
        ObjectLabel objLabel = ObjectLabel.make(c.getNode(), ObjectLabel.Kind.OBJECT);
        s.newObject(objLabel);
        s.writeInternalPrototype(objLabel, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        s.summarize(Collections.singleton(objLabel));

        ObjectLabel summaryObjLabel = objLabel.makeSummary();

        ObjectLabel arrayLabel = JSArray.makeArray(c.getNode(), c);
        s.summarize(Collections.singleton(arrayLabel));
        ObjectLabel summaryArrayLabel = arrayLabel.makeSummary();

        Value v = Value.makeObject(summaryObjLabel).joinObject(summaryArrayLabel).joinAnyStr().joinAnyNum().joinAnyBool();

        c.getAnalysis().getPropVarOperations().writeProperty(Collections.singleton(summaryObjLabel), Value.makeAnyStr(), v);
        c.getAnalysis().getPropVarOperations().writeProperty(Collections.singleton(summaryArrayLabel), Value.makeAnyStrUInt(), v);
        return v;
    }
}
