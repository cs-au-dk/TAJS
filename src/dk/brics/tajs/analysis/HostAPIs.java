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

package dk.brics.tajs.analysis;

import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptFunctions;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.analysis.nativeobjects.TAJSFunction;
import dk.brics.tajs.analysis.nativeobjects.TAJSFunctionEvaluator;
import dk.brics.tajs.analysis.signatures.NativeFunctionSignatureChecker;
import dk.brics.tajs.lattice.HostAPI;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;

/**
 * Descriptors and dispatching for the supported host APIs.
 */
public enum HostAPIs implements HostAPI {

    ECMASCRIPT_NATIVE("ECMAScript native functions", "native"),
    DOCUMENT_OBJECT_MODEL("The Document Object Model", "dom"),
    TAJS("TAJS_-functions", "tajs"),
    PARTIAL_HOST_MODEL("Partially modeled host APIs", "partial"),
    SPEC("TypeScript modeled", "spec");

    private String name;

    private String short_name;

    HostAPIs(String name, String short_name) {
        this.name = name;
        this.short_name = short_name;
    }

    public static Value evaluate(HostObject hostobject, CallInfo call, Solver.SolverInterface c) {
        if (NativeFunctionSignatureChecker.get().shouldStopPropagation(hostobject, call, c)) {
            c.getState().setToBottom();
            return Value.makeNone();
        }
        switch ((HostAPIs) hostobject.getAPI()) {
            case ECMASCRIPT_NATIVE:
                return ECMAScriptFunctions.evaluate((ECMAScriptObjects) hostobject, call, c);
            case DOCUMENT_OBJECT_MODEL:
                return DOMFunctions.evaluate((DOMObjects) hostobject, call, c);
            case TAJS:
                return TAJSFunctionEvaluator.evaluate((TAJSFunction) hostobject, call, c);
            case PARTIAL_HOST_MODEL:
                throw new AnalysisLimitationException.AnalysisModelLimitationException(call.getSourceNode().getSourceLocation() + ": No model for " + hostobject);
            case SPEC:
                  return c.getAnalysis().getTypeTester().evaluateCallToSymbolicFunction(hostobject, call, c);
            default:
                throw new AnalysisException("Unexpected host API");
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getShortName() {
        return short_name;
    }
}
