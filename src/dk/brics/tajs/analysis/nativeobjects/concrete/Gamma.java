package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;

/**
 * Converts abstract values to concrete values, if possible.
 */
public class Gamma {

    public static boolean isConcreteString(Value v, Solver.SolverInterface c) {
        if (v.getObjectLabels().size() == 1 && v.getObjectLabels().iterator().next().getKind() == ObjectLabel.Kind.STRING) {
            v = Conversion.toString(v, c);
        }
        return v.isMaybeSingleStr() && !v.isMaybeOtherThanStr();
    }

    public static boolean isConcreteValue(Value value, Solver.SolverInterface c) {
        return isConcreteString(value, c) || isConcreteNumber(value, c) || isConcreteUndefined(value) || isConcreteBoolean(value) || isConcreteRegExp(value, c);
    }

    private static boolean isConcreteUndefined(Value value) {
        return value.isMaybeUndef() && !value.isMaybeOtherThanUndef();
    }

    public static boolean isConcreteNumber(Value value, Solver.SolverInterface c) {
        if (value.getObjectLabels().size() == 1 && value.getObjectLabels().iterator().next().getKind() == ObjectLabel.Kind.NUMBER) {
            value = Conversion.toNumber(value, c);
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
        if (isConcreteBoolean(value)) {
            return toConcreteBoolean(value);
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
        Value global = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "global"), state);
        Value ignoreCase = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "ignoreCase"), state);
        Value multiline = UnknownValueResolver.getRealValue(pv.readPropertyValue(value.getObjectLabels(), "multiline"), state);
        return new ConcreteRegularExpression(toConcreteString(source, c), toConcreteBoolean(global), toConcreteBoolean(ignoreCase), toConcreteBoolean(multiline));
    }

    private static ConcreteBoolean toConcreteBoolean(Value value) {
        checkConcrete(isConcreteBoolean(value));
        value = Conversion.toBoolean(value);
        return new ConcreteBoolean(value.isMaybeTrueButNotFalse());
    }

    private static boolean isConcreteBoolean(Value value) {
        if (value.getObjectLabels().size() == 1 && value.getObjectLabels().iterator().next().getKind() == ObjectLabel.Kind.BOOLEAN) {
            value = Conversion.toBoolean(value);
        }
        return (value.isMaybeTrueButNotFalse() || value.isMaybeFalseButNotTrue()) && !value.isMaybeOtherThanBool();
    }

    private static boolean isConcreteRegExp(Value value, Solver.SolverInterface c) {
        if (value.getObjectLabels().isEmpty())
            return false;
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
        return concreteValues && lastIndex.getNum().intValue() == 0; // syntactic limitation: can not construct regexep expression with lastIndex equal to anything but 0!
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
        value = Conversion.toNumber(value, c);
        return new ConcreteNumber(value.getNum());
    }

    private static void checkConcrete(boolean success) {
        if (!success) {
            throw new IllegalArgumentException("Not a concrete value!");
        }
    }

    public static ConcreteString toConcreteString(Value value, Solver.SolverInterface c) {
        checkConcrete(isConcreteString(value, c));
        value = Conversion.toString(value, c);
        return new ConcreteString(value.getStr());
    }
}
