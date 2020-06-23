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

import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.concrete.NativeResult.Kind;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisLimitationException.AnalysisModelLimitationException;
import dk.brics.tajs.util.Collectors;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Interface for relaying evaluation of calls to a concrete semantics.
 * <p>
 * NB: the methods of this class do not have side-effects on values that are not provided as arguments to the methods.
 * In general, the invocation of non-pure functions is not recommended, but some side-effects are supported (e.g. RegExp#lastIndex).
 */
public class TAJSConcreteSemantics {

    private static final NativeConcreteSemantics nativeConcreteSemantics = new CachingNativeConcreteSemantics(new NashornConcreteSemantics());

    /**
     * Implements a general call with implicit arguments. The default behavior is used if the concrete semantics was inapplicable to the call. The default behavior is used if the concrete semantics was inapplicable to the call.
     */
    public static Value convertTAJSCall(Value vThis, String functionName, int maxArguments, CallInfo call, Solver.SolverInterface c, Supplier<Value> defaultBehavior) {
        Set<Value> result = getGeneralCalls(c).convertTAJSCall(vThis, functionName, maxArguments, call, defaultBehavior);
        return Value.join(result);
    }

    /**
     * Implements a general call with explicit arguments. The default behavior is used if the concrete semantics was inapplicable to the call.
     */
    public static Value convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Solver.SolverInterface c, Supplier<Value> defaultBehavior) {
        Set<Value> result = getGeneralCalls(c).convertTAJSCallExplicit(vThis, functionName, arguments, defaultBehavior);
        return Value.join(result);
    }

    private static TAJSConcreteSemanticsForGeneralCalls getGeneralCalls(Solver.SolverInterface c) {
        return new TAJSConcreteSemanticsForGeneralCalls(c); // NB we could avoid this constructor call on every use by making this class a singleton
    }

    /**
     * Implements a general call with explicit arguments. Throws exception if the concrete semantics was inapplicable to the call.
     */
    public static Value convertTAJSCallExplicit(Value vThis, String functionName, List<Value> arguments, Solver.SolverInterface c) {
        Set<NativeResult<ConcreteValue>> result = getGeneralCalls(c).convertTAJSCallExplicit(vThis, functionName, arguments);
        if (result.stream().anyMatch(r -> r.kind != Kind.VALUE)) {
            throw makeNonValueFailure();
        }
        return Value.join(result.stream().map(r -> Alpha.toValue(r.getValue(), c)).collect(Collectors.toSet()));
    }

    private static AnalysisModelLimitationException makeNonValueFailure() {
        return new AnalysisModelLimitationException("Implementation only supports value-results here: supply a valid, non-crashing program!");
    }

    public static NativeConcreteSemantics getNative() {
        return nativeConcreteSemantics;
    }

    /**
     * Implements eval.Throws exception if the concrete semantics was inapplicable to the call.
     */
    public static Value eval(String code) {
        NativeResult<ConcreteValue> result = getNative().eval(code);
        if (result.kind != Kind.VALUE) {
            throw makeNonValueFailure();
        }
        return Alpha.toValue(result.getValue(), null);
    }

    /**
     * Implements Function.prototype.toString. Throws exception if the function is syntactically invalid (it should not be possible to make such an ObjectLabel).
     */
    public static String convertFunctionToString(ObjectLabel functionLabel) {
        if (functionLabel.isHostObject()) {
            String enumString = functionLabel.getHostObject().toString();
            String functionName = enumString.substring(enumString.lastIndexOf(".") + 1) /* abusing convention of host objects names pointing to their definition */;
            String source = String.format("function %s() { [native code] }", functionName);
            return source;
        }
        // In google chrome, the toString of a function is ignoring inline comments in certain places:
        // ```
        // $ (function /**/foo/**/(/**/bar/**/)/**/{/**/baz;/**/}).toString()
        // > "function foo(/**/bar/**/)/**/{/**/baz;/**/}"
        // ```
        // So we can not simply return the source of the function object
        // (but nashorn does return the entire source of the function declaration, so the call to concrete semantics is actually moot currently)
        String toStringCallCode = String.format("(%s).toString()", functionLabel.getFunction().getSource());
        NativeResult<ConcreteValue> result = getNative().eval(toStringCallCode);
        if (result.kind != Kind.VALUE) {
            throw new AnalysisModelLimitationException("Implementation only supports value-results here: supply a valid, non-crashing program!");
        }
        ConcreteString value = (ConcreteString) result.getValue();
        return value.getString();
    }
}
