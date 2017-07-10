/*
 * Copyright 2009-2017 Aarhus University
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

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls.CallInfo;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.apache.log4j.Logger;

import static dk.brics.tajs.util.Collections.singleton;

/**
 * Encapsulation of transfer functions for ECMAScript native functions.
 */
public class ECMAScriptFunctions {

    private static Logger log = Logger.getLogger(ECMAScriptFunctions.class);

    /**
     * Evaluates the given native ECMAScript function.
     */
    public static Value evaluate(ECMAScriptObjects nativeobject, CallInfo call, Solver.SolverInterface c) {
        if (log.isDebugEnabled())
            log.debug("native function: " + nativeobject);
        Value res;
        switch (nativeobject) {

            case OBJECT:
            case OBJECT_TOSTRING:
            case OBJECT_TOLOCALESTRING:
            case OBJECT_VALUEOF:
            case OBJECT_HASOWNPROPERTY:
            case OBJECT_ISPROTOTYPEOF:
            case OBJECT_PROPERTYISENUMERABLE:
            case OBJECT_DEFINE_PROPERTY:
            case OBJECT_DEFINE_PROPERTIES:
            case OBJECT_CREATE:
            case OBJECT_FREEZE:
            case OBJECT_KEYS:
            case OBJECT_GETOWNPROPERTYNAMES:
            case OBJECT_DEFINEGETTER:
            case OBJECT_DEFINESETTER:
            case OBJECT_GETOWNPROPERTYDESCRIPTOR:
            case OBJECT_GETPROTOTYPEOF:
            case OBJECT_SETPROTOTYPEOF:
                res = JSObject.evaluate(nativeobject, call, c);
                break;

            case FUNCTION:
            case FUNCTION_TOSTRING:
            case FUNCTION_APPLY:
            case FUNCTION_CALL:
            case FUNCTION_PROTOTYPE:
                res = JSFunction.evaluate(nativeobject, call, c);
                break;

            case JSON_PARSE:
            case JSON_STRINGIFY:
                res = JSJson.evaluate(nativeobject, call, c);
                break;

            case ARRAY:
            case ARRAY_JOIN:
            case ARRAY_TOSTRING:
            case ARRAY_TOLOCALESTRING:
            case ARRAY_CONCAT:
            case ARRAY_PUSH:
            case ARRAY_POP:
            case ARRAY_REVERSE:
            case ARRAY_SHIFT:
            case ARRAY_SLICE:
            case ARRAY_SORT:
            case ARRAY_SPLICE:
            case ARRAY_UNSHIFT:
            case ARRAY_INDEXOF:
            case ARRAY_ISARRAY:
                res = JSArray.evaluate(nativeobject, call, c);
                break;

            case NUMBER:
            case NUMBER_TOEXPONENTIAL:
            case NUMBER_TOFIXED:
            case NUMBER_TOPRECISION:
            case NUMBER_TOLOCALESTRING:
            case NUMBER_TOSTRING:
            case NUMBER_VALUEOF:
            case NUMBER_ISFINITE:
                res = JSNumber.evaluate(nativeobject, call, c);
                break;

            case EVAL_ERROR:
            case RANGE_ERROR:
            case REFERENCE_ERROR:
            case SYNTAX_ERROR:
            case TYPE_ERROR:
            case URI_ERROR:
            case ERROR:
            case ERROR_TOSTRING:
                res = JSError.evaluate(nativeobject, call, c);
                break;

            case REGEXP:
            case REGEXP_COMPILE:
            case REGEXP_EXEC:
            case REGEXP_TEST:
            case REGEXP_TOSTRING:
                res = JSRegExp.evaluate(nativeobject, call, c);
                break;

            case DATE:
            case DATE_GETDATE:
            case DATE_GETDAY:
            case DATE_GETFULLYEAR:
            case DATE_GETHOURS:
            case DATE_GETMILLISECONDS:
            case DATE_GETMINUTES:
            case DATE_GETMONTH:
            case DATE_GETSECONDS:
            case DATE_GETTIME:
            case DATE_GETTIMEZONEOFFSET:
            case DATE_GETUTCDATE:
            case DATE_GETUTCDAY:
            case DATE_GETUTCFULLYEAR:
            case DATE_GETUTCHOURS:
            case DATE_GETUTCMILLISECONDS:
            case DATE_GETUTCMINUTES:
            case DATE_GETUTCMONTH:
            case DATE_GETUTCSECONDS:
            case DATE_PARSE:
            case DATE_SETDATE:
            case DATE_SETFULLYEAR:
            case DATE_SETHOURS:
            case DATE_SETMILLISECONDS:
            case DATE_SETMINUTES:
            case DATE_SETMONTH:
            case DATE_SETSECONDS:
            case DATE_SETTIME:
            case DATE_SETUTCDATE:
            case DATE_SETUTCFULLYEAR:
            case DATE_SETUTCHOURS:
            case DATE_SETUTCMILLISECONDS:
            case DATE_SETUTCMINUTES:
            case DATE_SETUTCMONTH:
            case DATE_SETUTCSECONDS:
            case DATE_TODATESTRING:
            case DATE_TOLOCALEDATESTRING:
            case DATE_TOLOCALESTRING:
            case DATE_TOLOCALETIMESTRING:
            case DATE_TOSTRING:
            case DATE_TOTIMESTRING:
            case DATE_TOUTCSTRING:
            case DATE_UTC:
            case DATE_VALUEOF:
            case DATE_GETYEAR:
            case DATE_SETYEAR:
            case DATE_TOGMTSTRING:
            case DATE_NOW:
                res = JSDate.evaluate(nativeobject, call, c);
                break;

            case STRING:
            case STRING_VALUEOF:
            case STRING_TOSTRING:
            case STRING_TOUPPERCASE:
            case STRING_TOLOCALEUPPERCASE:
            case STRING_TOLOWERCASE:
            case STRING_TOLOCALELOWERCASE:
            case STRING_SUBSTRING:
            case STRING_SUBSTR:
            case STRING_SPLIT:
            case STRING_SLICE:
            case STRING_SEARCH:
            case STRING_MATCH:
            case STRING_LOCALECOMPARE:
            case STRING_LASTINDEXOF:
            case STRING_INDEXOF:
            case STRING_CONCAT:
            case STRING_FROMCHARCODE:
            case STRING_CHARCODEAT:
            case STRING_CHARAT:
            case STRING_TRIM:
            case STRING_TRIMLEFT:
            case STRING_TRIMRIGHT:
            case STRING_ENDSWITH:
            case STRING_STARTSWITH:
                res = JSString.evaluate(nativeobject, call, c);
                break;

            case BOOLEAN:
            case BOOLEAN_TOSTRING:
            case BOOLEAN_VALUEOF:
                res = JSBoolean.evaluate(nativeobject, call, c);
                break;

            case MATH_ABS:
            case MATH_SIN:
            case MATH_COS:
            case MATH_TAN:
            case MATH_ATAN:
            case MATH_SQRT:
            case MATH_ROUND:
            case MATH_CEIL:
            case MATH_ASIN:
            case MATH_ACOS:
            case MATH_EXP:
            case MATH_FLOOR:
            case MATH_LOG:
            case MATH_ATAN2:
            case MATH_POW:
            case MATH_MAX:
            case MATH_MIN:
            case MATH_RANDOM:
                res = JSMath.evaluate(nativeobject, call, c);
                break;

            case EVAL:
            case PARSEINT:
            case PARSEFLOAT:
            case ISNAN:
            case ISFINITE:
            case DECODEURI:
            case DECODEURICOMPONENT:
            case ENCODEURI:
            case ENCODEURICOMPONENT:
            case PRINT:
            case ALERT:
            case ESCAPE:
            case UNESCAPE:
                res = JSGlobal.evaluate(nativeobject, call, c);
                break;

            case ARRAY_PROTOTYPE:
            case BOOLEAN_PROTOTYPE:
            case DATE_PROTOTYPE:
            case ERROR_PROTOTYPE:
            case EVAL_ERROR_PROTOTYPE:
            case GLOBAL:
            case MATH:
            case NUMBER_PROTOTYPE:
            case OBJECT_PROTOTYPE:
            case RANGE_ERROR_PROTOTYPE:
            case REFERENCE_ERROR_PROTOTYPE:
            case REGEXP_PROTOTYPE:
            case STRING_PROTOTYPE:
            case SYNTAX_ERROR_PROTOTYPE:
            case TYPE_ERROR_PROTOTYPE:
            case URI_ERROR_PROTOTYPE:
                throw new AnalysisException("Native object is not a function: " + nativeobject);

            default:
                String msg = call.getSourceNode().getSourceLocation() + ": No transfer function for native function " + nativeobject;
                if (c.getAnalysis().getUnsoundness().maySkipMissingModelOfNativeFunction(c.getNode(), nativeobject)) {
                    return Value.makeUndef();
                }
                throw new AnalysisLimitationException.AnalysisModelLimitationException(msg);
        }
        return res;
    }

    /**
     * toString conversion for ECMAScript built-in objects.
     */
    public static Value internalToString(ObjectLabel thisobj, ECMAScriptObjects obj, Solver.SolverInterface c) {
        Value thisval = Value.makeObject(thisobj);
        Value result;
        switch (obj) {
            case OBJECT_TOSTRING:
                return JSObject.evaluateToString(thisval, c);
            case FUNCTION_TOSTRING:
                return JSFunction.evaluateToString(thisval, c);
            case ARRAY_TOSTRING:
                return JSArray.evaluateToString(thisval, c);
            case STRING_TOSTRING:
                return JSString.evaluateToString(thisval, c);
            case BOOLEAN_TOSTRING:
                return JSBoolean.evaluateToString(thisval, c);
            case NUMBER_TOSTRING:
                return JSNumber.evaluateToString(thisval, Value.makeUndef(), c);
            case REGEXP_TOSTRING:
                return JSRegExp.evaluateToString(thisval, c);
            case DATE_TOSTRING:
                return JSDate.evaluateToString(thisval, c);
            case ERROR_TOSTRING:
                return JSError.evaluateToString();
            default:
                c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "Implicit call to native non-toString method");
                result = Value.makeAnyStr(); // FIXME: implicit call to native non-toString method - github #254
                break;
        }
        return result;
    }

    /**
     * valueOf conversion for ECMAScript built-in objects.
     */
    public static Value internalValueOf(ObjectLabel thiss, ECMAScriptObjects obj, Solver.SolverInterface c) {
        Value result = null;
        switch (obj) {
            case OBJECT_VALUEOF:
                // 15.2.4.4 Object.prototype.valueOf ( )
                // The valueOf method returns its this value.
                result = Value.makeObject(thiss); // this function is generic, no TypeError
                break;
            case STRING_VALUEOF:
                // 15.5.4.3 String.prototype.valueOf ( )
                // Returns this string value.
                if (thiss.getKind() == Kind.STRING) {
                    Value v = c.getState().readInternalValue(singleton(thiss));
                    v = UnknownValueResolver.getRealValue(v, c.getState());
                    result = v;
                } else
                    Exceptions.throwTypeError(c);
                break;
            case BOOLEAN_VALUEOF:
                // 15.6.4.3 Boolean.prototype.valueOf ( )
                // Returns this boolean value.
                if (thiss.getKind() == Kind.BOOLEAN) {
                    Value v = c.getState().readInternalValue(singleton(thiss));
                    v = UnknownValueResolver.getRealValue(v, c.getState());
                    result = v;
                } else
                    Exceptions.throwTypeError(c);
                break;
            case NUMBER_VALUEOF:
                // 15.7.4.4 Number.prototype.valueOf ( )
                // Returns this number value.
                if (thiss.getKind() == Kind.NUMBER) {
                    Value v = c.getState().readInternalValue(singleton(thiss));
                    v = UnknownValueResolver.getRealValue(v, c.getState());
                    result = v;
                } else
                    Exceptions.throwTypeError(c);
                break;
            case DATE_VALUEOF:
                // 15.9.5.8 Date.prototype.valueOf ( )
                // The valueOf function returns a number, which is this time value.
                if (thiss.getKind() == Kind.DATE)
                    result = Value.makeAnyNumNotNaNInf();
                else
                    Exceptions.throwTypeError(c); // not generic according to 15.9.5
                break;
            default:
                c.getMonitoring().addMessage(c.getNode(), Severity.HIGH, "Implicit call to native non-valueOf method");
                result = Value.makeAnyStr(); // FIXME: implicit call to native non-valueOf method - github #254
                break;
        }
        return result;
    }
}
