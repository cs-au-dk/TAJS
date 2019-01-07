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

package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

/**
 * Converts abstract values to concrete values, if possible.
 */
public class SingleGamma {

    public static boolean isConcreteString(Value value, Solver.SolverInterface c) {
        if (value.isNone()) {
            return false;
        }
        if (!value.getObjectLabels().isEmpty() && value.getObjectLabels().stream().allMatch(l -> l.getKind() == ObjectLabel.Kind.STRING)) {
            value = value.restrictToNotObject().join(c.getState().readInternalValue(value.getObjectLabels()));
        }
        return value.isMaybeSingleStr() && !value.isMaybeOtherThanStr();
    }

    public static boolean isConcreteValue(Value value, Solver.SolverInterface c) {
        return isConcreteString(value, c) || isConcreteNumber(value, c) || isConcreteUndefined(value) || isConcreteBoolean(value, c) || isConcreteRegExp(value, c);
    }

    private static boolean isConcreteUndefined(Value value) {
        if (value.isNone()) {
            return false;
        }
        return value.isMaybeUndef() && !value.isMaybeOtherThanUndef();
    }

    public static boolean isConcreteNumber(Value value, Solver.SolverInterface c) {
        if (value.isNone()) {
            return false;
        }
        if (!value.getObjectLabels().isEmpty() && value.getObjectLabels().stream().allMatch(l -> l.getKind() == ObjectLabel.Kind.NUMBER)) {
            value = value.restrictToNotObject().join(c.getState().readInternalValue(value.getObjectLabels()));
        }
        if (value.isMaybeInf()) {
            return false; // +/- Infinity
        }
        if (value.isNaN()) {
            return true;
        }
        return value.isMaybeSingleNum() && !value.isMaybeOtherThanNum();
    }

    public static ConcreteValue toConcreteValue(Value value, Solver.SolverInterface c) {
        checkConcrete(isConcreteValue(value, c));
        if (isConcreteBoolean(value, c)) {
            return toConcreteBoolean(value, c);
        }
        if (isConcreteNumber(value, c)) {
            return toConcreteNumber(value, c);
        }
        if (isConcreteString(value, c)) {
            return toConcreteString(value, c);
        }
        if (isConcreteUndefined(value)) {
            return new ConcreteUndefined();
        }
        if (isConcreteRegExp(value, c)) {
            return toConcreteReqExp(value, c);
        }
        throw new RuntimeException("Unexpected concrete value: " + value);
    }

    private static ConcreteValue toConcreteReqExp(Value value, Solver.SolverInterface c) {
        checkConcrete(isConcreteRegExp(value, c));
        State state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        Value source = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "source"), state);
        Value lastIndex = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "lastIndex"), state);
        if (lastIndex.isMaybeOtherThanNum()) {
            lastIndex = Value.makeNum(0);
        }
        Value global = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "global"), state);
        Value ignoreCase = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "ignoreCase"), state);
        Value multiline = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "multiline"), state);
        return new ConcreteRegularExpression(toConcreteString(source, c), toConcreteBoolean(global, c), toConcreteBoolean(ignoreCase, c), toConcreteBoolean(multiline, c), toConcreteNumber(lastIndex, c));
    }

    private static ConcreteBoolean toConcreteBoolean(Value value, Solver.SolverInterface c) {
        checkConcrete(isConcreteBoolean(value, c));
        if (value.isMaybeObject()) {
            value = c.getState().readInternalValue(value.getObjectLabels());
        }
        return new ConcreteBoolean(value.isMaybeTrueButNotFalse());
    }

    private static boolean isConcreteBoolean(Value value, Solver.SolverInterface c) {
        if (value.isNone()) {
            return false;
        }
        if (!value.getObjectLabels().isEmpty() && value.getObjectLabels().stream().allMatch(l -> l.getKind() == ObjectLabel.Kind.BOOLEAN)) {
            value = value.restrictToNotObject().join(c.getState().readInternalValue(value.getObjectLabels()));
        }
        return (value.isMaybeTrueButNotFalse() || value.isMaybeFalseButNotTrue()) && !value.isMaybeOtherThanBool();
    }

    private static boolean isConcreteRegExp(Value value, Solver.SolverInterface c) {
        if (value.isNone()) {
            return false;
        }
        if (value.getObjectLabels().isEmpty())
            return false;

        if (value.isMaybePrimitiveOrSymbol()) return false;
        for (ObjectLabel label : value.getObjectLabels()) {
            boolean isRegExp = label.getKind() == ObjectLabel.Kind.REGEXP;
            if (!isRegExp) {
                return false;
            }
        }

        State state = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        Value source = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "source"), state);
        Value lastIndex = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "lastIndex"), state);
        Value global = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "global"), state);
        Value ignoreCase = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "ignoreCase"), state);
        Value multiline = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "multiline"), state);

        boolean concreteValues = isConcreteValues(c, source, lastIndex, global, ignoreCase, multiline);
        return concreteValues;
    }

    public static boolean isConcreteValues(Solver.SolverInterface c, Value... values) {
        for (Value value : values) {
            if (!isConcreteValue(value, c)) {
                return false;
            }
        }
        return true;
    }

    public static ConcreteNumber toConcreteNumber(Value value, Solver.SolverInterface c) {
        checkConcrete(isConcreteNumber(value, c));
        if (value.isMaybeObject()) {
            value = c.getState().readInternalValue(value.getObjectLabels());
        }
        return new ConcreteNumber(value.getNum());
    }

    private static void checkConcrete(boolean success) {
        if (!success) {
            throw new AnalysisException("Not a concrete value!");
        }
    }

    public static ConcreteString toConcreteString(Value value, Solver.SolverInterface c) {
        checkConcrete(isConcreteString(value, c));
        if (value.isMaybeObject()) {
            value = c.getState().readInternalValue(value.getObjectLabels());
        }
        return new ConcreteString(value.getStr());
    }
}
