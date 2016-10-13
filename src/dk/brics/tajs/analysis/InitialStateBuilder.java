/*
 * Copyright 2009-2016 Aarhus University
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

import dk.brics.tajs.analysis.dom.DOMBuilder;
import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.EventHandlerKind;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IInitialStateBuilder;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import net.htmlparser.jericho.Source;

import java.util.Collection;
import java.util.Map.Entry;

import static dk.brics.tajs.util.Collections.singleton;

/**
 * Sets up the initial state (Chapter 15).
 */
public class InitialStateBuilder implements IInitialStateBuilder<State, Context, CallEdge, IAnalysisMonitoring, Analysis> {

    /**
     * Object label for the global object.
     */
    public static final ObjectLabel GLOBAL = new ObjectLabel(ECMAScriptObjects.GLOBAL, Kind.OBJECT);

    /**
     * Object label for Object.prototype.
     */
    public static final ObjectLabel OBJECT_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.OBJECT_PROTOTYPE, Kind.OBJECT);

    /**
     * Object label for Function.prototype.
     */
    public static final ObjectLabel FUNCTION_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.FUNCTION_PROTOTYPE, Kind.FUNCTION);

    /**
     * Object label for Array.prototype.
     */
    public static final ObjectLabel ARRAY_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.ARRAY_PROTOTYPE, Kind.ARRAY);

    /**
     * Object label for String.prototype.
     */
    public static final ObjectLabel STRING_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.STRING_PROTOTYPE, Kind.STRING);

    /**
     * Object label for Boolean.prototype.
     */
    public static final ObjectLabel BOOLEAN_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.BOOLEAN_PROTOTYPE, Kind.BOOLEAN);

    /**
     * Object label for Number.prototype.
     */
    public static final ObjectLabel NUMBER_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.NUMBER_PROTOTYPE, Kind.NUMBER);

    /**
     * Object label for Date.prototype.
     */
    public static final ObjectLabel DATE_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.DATE_PROTOTYPE, Kind.DATE);

    /**
     * Object label for RegExp.prototype.
     */
    public static final ObjectLabel REGEXP_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.REGEXP_PROTOTYPE, Kind.OBJECT);
    /* From ES5, Annex D:
     RegExp.prototype is now a RegExp object rather than an instance of Object. The value of its [[Class]] internal 
     property which is observable using Object.prototype.toString is now 'RegExp' rather than 'Object'.
     */ // TODO: use ES5 semantics of RegExp.prototype object kind?

    /**
     * Object label for Error.prototype.
     */
    public static final ObjectLabel ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.ERROR_PROTOTYPE, Kind.ERROR);

    /**
     * Object label for EvalError.prototype.
     */
    public static final ObjectLabel EVAL_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.EVAL_ERROR_PROTOTYPE, Kind.ERROR);

    /**
     * Object label for RangeError.prototype.
     */
    public static final ObjectLabel RANGE_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.RANGE_ERROR_PROTOTYPE, Kind.ERROR);

    /**
     * Object label for ReferenceError.prototype.
     */
    public static final ObjectLabel REFERENCE_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.REFERENCE_ERROR_PROTOTYPE, Kind.ERROR);

    /**
     * Object label for SyntaxError.prototype.
     */
    public static final ObjectLabel SYNTAX_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.SYNTAX_ERROR_PROTOTYPE, Kind.ERROR);

    /**
     * Object label for TypeError.prototype.
     */
    public static final ObjectLabel TYPE_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.TYPE_ERROR_PROTOTYPE, Kind.ERROR);

    /**
     * Object label for URIError.prototype.
     */
    public static final ObjectLabel URI_ERROR_PROTOTYPE = new ObjectLabel(ECMAScriptObjects.URI_ERROR_PROTOTYPE, Kind.ERROR);

    /**
     * Constructs a new InitialStateBuilder object.
     */
    public InitialStateBuilder() {
    }

    /**
     * Sets up the initial state.
     */
    @Override
    public void addInitialState(BasicBlock global_entry_block, Solver.SolverInterface c, Source document) {
        State s = new State(c, global_entry_block);
        c.setState(s);
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ObjectLabel global = GLOBAL; // same as DOMBuilder.WINDOW
        s.newObject(global);
        s.setExecutionContext(new ExecutionContext(ScopeChain.make(global), singleton(global), singleton(global)));

        ObjectLabel lObject = new ObjectLabel(ECMAScriptObjects.OBJECT, Kind.FUNCTION);
        s.newObject(lObject);
        ObjectLabel lFunction = new ObjectLabel(ECMAScriptObjects.FUNCTION, Kind.FUNCTION);
        s.newObject(lFunction);
        ObjectLabel lArray = new ObjectLabel(ECMAScriptObjects.ARRAY, Kind.FUNCTION);
        s.newObject(lArray);
        ObjectLabel lString = new ObjectLabel(ECMAScriptObjects.STRING, Kind.FUNCTION);
        s.newObject(lString);
        ObjectLabel lBoolean = new ObjectLabel(ECMAScriptObjects.BOOLEAN, Kind.FUNCTION);
        s.newObject(lBoolean);
        ObjectLabel lNumber = new ObjectLabel(ECMAScriptObjects.NUMBER, Kind.FUNCTION);
        s.newObject(lNumber);
        ObjectLabel lDate = new ObjectLabel(ECMAScriptObjects.DATE, Kind.FUNCTION);
        s.newObject(lDate);
        ObjectLabel lRegExp = new ObjectLabel(ECMAScriptObjects.REGEXP, Kind.FUNCTION);
        s.newObject(lRegExp);
        ObjectLabel lError = new ObjectLabel(ECMAScriptObjects.ERROR, Kind.FUNCTION);
        s.newObject(lError);
        ObjectLabel lEvalError = new ObjectLabel(ECMAScriptObjects.EVAL_ERROR, Kind.FUNCTION);
        s.newObject(lEvalError);
        ObjectLabel lRangeError = new ObjectLabel(ECMAScriptObjects.RANGE_ERROR, Kind.FUNCTION);
        s.newObject(lRangeError);
        ObjectLabel lReferenceError = new ObjectLabel(ECMAScriptObjects.REFERENCE_ERROR, Kind.FUNCTION);
        s.newObject(lReferenceError);
        ObjectLabel lSyntaxError = new ObjectLabel(ECMAScriptObjects.SYNTAX_ERROR, Kind.FUNCTION);
        s.newObject(lSyntaxError);
        ObjectLabel lTypeError = new ObjectLabel(ECMAScriptObjects.TYPE_ERROR, Kind.FUNCTION);
        s.newObject(lTypeError);
        ObjectLabel lURIError = new ObjectLabel(ECMAScriptObjects.URI_ERROR, Kind.FUNCTION);
        s.newObject(lURIError);

        ObjectLabel lMath = new ObjectLabel(ECMAScriptObjects.MATH, Kind.MATH);
        s.newObject(lMath);

        ObjectLabel lJson = new ObjectLabel(ECMAScriptObjects.JSON, Kind.OBJECT);
        s.newObject(lJson);

        ObjectLabel lObjectPrototype = OBJECT_PROTOTYPE;
        s.newObject(lObjectPrototype);
        ObjectLabel lFunProto = FUNCTION_PROTOTYPE;
        s.newObject(lFunProto);
        ObjectLabel lArrayProto = ARRAY_PROTOTYPE;
        s.newObject(lArrayProto);
        ObjectLabel lStringProto = STRING_PROTOTYPE;
        s.newObject(lStringProto);
        ObjectLabel lBooleanProto = BOOLEAN_PROTOTYPE;
        s.newObject(lBooleanProto);
        ObjectLabel lNumberProto = NUMBER_PROTOTYPE;
        s.newObject(lNumberProto);
        ObjectLabel lDateProto = DATE_PROTOTYPE;
        s.newObject(lDateProto);
        ObjectLabel lRegExpProto = REGEXP_PROTOTYPE;
        s.newObject(lRegExpProto);
        ObjectLabel lErrorProto = ERROR_PROTOTYPE;
        s.newObject(lErrorProto);
        ObjectLabel lEvalErrorProto = EVAL_ERROR_PROTOTYPE;
        s.newObject(lEvalErrorProto);
        ObjectLabel lRangeErrorProto = RANGE_ERROR_PROTOTYPE;
        s.newObject(lRangeErrorProto);
        ObjectLabel lReferenceErrorProto = REFERENCE_ERROR_PROTOTYPE;
        s.newObject(lReferenceErrorProto);
        ObjectLabel lSyntaxErrorProto = SYNTAX_ERROR_PROTOTYPE;
        s.newObject(lSyntaxErrorProto);
        ObjectLabel lTypeErrorProto = TYPE_ERROR_PROTOTYPE;
        s.newObject(lTypeErrorProto);
        ObjectLabel lURIErrorProto = URI_ERROR_PROTOTYPE;
        s.newObject(lURIErrorProto);

        // 15.1.1 value properties of the global object
        pv.writePropertyWithAttributes(global, "NaN", Value.makeNum(Double.NaN).setAttributes(true, true, false));
        pv.writePropertyWithAttributes(global, "Infinity", Value.makeNum(Double.POSITIVE_INFINITY).setAttributes(true, true, false));
        pv.writePropertyWithAttributes(global, "undefined", Value.makeUndef().setAttributes(true, true, false));
        // TODO: 15.1 the values of the [[Prototype]] and [[Class]] properties of the global object are implementation-dependent
        s.writeInternalPrototype(global, Value.makeObject(lObjectPrototype)); // Rhino's implementation choice

        // 15.1.2 function properties of the global object
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.EVAL, "eval", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.PARSEINT, "parseInt", 2, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.PARSEFLOAT, "parseFloat", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.ISNAN, "isNaN", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.ISFINITE, "isFinite", 1, c);

        // 15.1.3 URI handling function properties
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.DECODEURI, "decodeURI", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.DECODEURICOMPONENT, "decodeURIComponent", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.ENCODEURI, "encodeURI", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.ENCODEURICOMPONENT, "encodeURIComponent", 1, c);

        // Rhino's print and alert functions
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.PRINT, "print", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.ALERT, "alert", 1, c);

        // 15.1.4 constructor properties of the global object
        createPrimitiveConstructor(global, lFunProto, lObjectPrototype, lObject, "Object", 1, c);
        createPrimitiveConstructor(global, lFunProto, lFunProto, lFunction, "Function", 1, c);
        createPrimitiveConstructor(global, lFunProto, lArrayProto, lArray, "Array", 1, c);
        createPrimitiveConstructor(global, lFunProto, lStringProto, lString, "String", 1, c);
        createPrimitiveConstructor(global, lFunProto, lBooleanProto, lBoolean, "Boolean", 1, c);
        createPrimitiveConstructor(global, lFunProto, lNumberProto, lNumber, "Number", 1, c);
        createPrimitiveConstructor(global, lFunProto, lDateProto, lDate, "Date", 7, c);
        createPrimitiveConstructor(global, lFunProto, lRegExpProto, lRegExp, "RegExp", 2, c);
        createPrimitiveConstructor(global, lFunProto, lErrorProto, lError, "Error", 1, c);
        createPrimitiveConstructor(global, lFunProto, lEvalErrorProto, lEvalError, "EvalError", 1, c);
        createPrimitiveConstructor(global, lFunProto, lRangeErrorProto, lRangeError, "RangeError", 1, c);
        createPrimitiveConstructor(global, lFunProto, lReferenceErrorProto, lReferenceError, "ReferenceError", 1, c);
        createPrimitiveConstructor(global, lFunProto, lSyntaxErrorProto, lSyntaxError, "SyntaxError", 1, c);
        createPrimitiveConstructor(global, lFunProto, lTypeErrorProto, lTypeError, "TypeError", 1, c);
        createPrimitiveConstructor(global, lFunProto, lURIErrorProto, lURIError, "URIError", 1, c);

        pv.writePropertyWithAttributes(global, "JSON", Value.makeObject(lJson).setAttributes(true, false, false));
        createPrimitiveFunction(lJson, lFunProto, ECMAScriptObjects.JSON_PARSE, "parse", 1, c);
        createPrimitiveFunction(lJson, lFunProto, ECMAScriptObjects.JSON_STRINGIFY, "stringify", 1, c);
        
        
        // 15.2.3 Properties of the Object Constructor
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_DEFINE_PROPERTY, "defineProperty", 3, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_CREATE, "create", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_DEFINEPPROPERTIES, "defineProperties", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_FREEZE, "freeze", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_GETOWNPROPERTYDESCRIPTOR, "getOwnPropertyDescriptor", 1, c);//
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_GETPROTOTYPEOF, "getPrototypeOf", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_ISEXTENSIBLE, "isExtensible", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_ISFROZEN, "isFrozen", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_ISSEALED, "isSealed", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_KEYS, "keys", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_PREVENTEXTENSIONS, "preventExtensions", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_SEAL, "seal", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_GETOWNPROPERTYNAMES, "getOwnPropertyNames", 0, c);
        
        // 15.2.4 properties of the Object prototype object
        s.writeInternalPrototype(lObjectPrototype, Value.makeNull());
        pv.writePropertyWithAttributes(lObjectPrototype, "constructor", Value.makeObject(lObject).setAttributes(true, false, false));
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_TOLOCALESTRING, "toLocaleString", 0, c);
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_VALUEOF, "valueOf", 0, c);
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_HASOWNPROPERTY, "hasOwnProperty", 1, c);
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_ISPROTOTYPEOF, "isPrototypeOf", 1, c);
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_PROPERTYISENUMERABLE, "propertyIsEnumerable", 1, c);
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_DEFINEGETTER, "__defineGetter__", 2, c);
        createPrimitiveFunction(lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_DEFINESETTER, "__defineSetter__", 2, c);
        
        // 15.3.4 properties of the Function prototype object
        s.writeInternalPrototype(lFunProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lFunProto, "constructor", Value.makeObject(lFunction).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lFunProto, "length", Value.makeNum(0).setAttributes(true, true, true));
        createPrimitiveFunction(lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_APPLY, "apply", 2, c);
        createPrimitiveFunction(lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_CALL, "call", 1, c);
        createPrimitiveFunction(lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_BIND, "bind", 1, c);
        // TODO pv.writePropertyWithAttributes(lFunction, "prototype", Value.makeObject(EMPTY).setAttributes(false, false, false);

        // 15.4.3.2 properties of the Array constructor
        createPrimitiveFunction(lArray, lFunProto, ECMAScriptObjects.ARRAY_ISARRAY, "isArray", 1, c);

        // 15.4.4 properties of the Array prototype object
        s.writeInternalPrototype(lArrayProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lArrayProto, "length", Value.makeNum(0).setAttributes(true, true, false));
        pv.writePropertyWithAttributes(lArrayProto, "constructor", Value.makeObject(lArray).setAttributes(true, false, false));
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_TOLOCALESTRING, "toLocaleString", 0, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_CONCAT, "concat", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_JOIN, "join", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_POP, "pop", 0, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_PUSH, "push", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_REVERSE, "reverse", 0, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SHIFT, "shift", 0, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SLICE, "slice", 2, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SOME, "some", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SORT, "sort", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SPLICE, "splice", 2, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_UNSHIFT, "unshift", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_INDEXOF, "indexOf", 1, c);

        // 15.5.3 properties of the String constructor
        createPrimitiveFunction(lString, lFunProto, ECMAScriptObjects.STRING_FROMCHARCODE, "fromCharCode", 1, c);

        // 15.5.4 properties of the String prototype object
        s.writeInternalPrototype(lStringProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lStringProto, "constructor", Value.makeObject(lString).setAttributes(true, false, false));
        s.writeInternalValue(lStringProto, Value.makeStr(""));

        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_VALUEOF, "valueOf", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARAT, "charAt", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARCODEAT, "charCodeAt", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_CONCAT, "concat", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_INDEXOF, "indexOf", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_LASTINDEXOF, "lastIndexOf", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_LOCALECOMPARE, "localeCompare", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_MATCH, "match", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_REPLACE, "replace", 2, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SEARCH, "search", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SLICE, "slice", 2, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SPLIT, "split", 2, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SUBSTRING, "substring", 2, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOWERCASE, "toLowerCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALELOWERCASE, "toLocaleLowerCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOUPPERCASE, "toUpperCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALEUPPERCASE, "toLocaleUpperCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TRIM, "trim", 0, c);
        pv.writePropertyWithAttributes(lStringProto, "length", Value.makeAnyNum().setAttributes(true, true, true));


        // 15.6.4 properties of the Boolean prototype object
        s.writeInternalPrototype(lBooleanProto, Value.makeObject(lObjectPrototype));
        s.writeInternalValue(lBooleanProto, Value.makeBool(false));
        pv.writePropertyWithAttributes(lBooleanProto, "constructor", Value.makeObject(lBoolean).setAttributes(true, false, false));
        createPrimitiveFunction(lBooleanProto, lFunProto, ECMAScriptObjects.BOOLEAN_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lBooleanProto, lFunProto, ECMAScriptObjects.BOOLEAN_VALUEOF, "valueOf", 0, c);

        // 15.7.3 properties of the Number constructor
        pv.writePropertyWithAttributes(lNumber, "MAX_VALUE", Value.makeNum(Double.MAX_VALUE).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lNumber, "MIN_VALUE", Value.makeNum(Double.MIN_VALUE).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lNumber, "NaN", Value.makeNum(Double.NaN).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lNumber, "POSITIVE_INFINITY", Value.makeNum(Double.POSITIVE_INFINITY).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lNumber, "NEGATIVE_INFINITY", Value.makeNum(Double.NEGATIVE_INFINITY).setAttributes(true, true, true));

        // 15.7.4 properties of the Number prototype object
        s.writeInternalPrototype(lNumberProto, Value.makeObject(lObjectPrototype));
        s.writeInternalValue(lNumberProto, Value.makeNum(0));
        pv.writePropertyWithAttributes(lNumberProto, "constructor", Value.makeObject(lNumber).setAttributes(true, false, false));
        createPrimitiveFunction(lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOSTRING, "toString", 1, c);
        createPrimitiveFunction(lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOLOCALESTRING, "toLocaleString", 0, c);
        createPrimitiveFunction(lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_VALUEOF, "valueOf", 0, c);
        createPrimitiveFunction(lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOFIXED, "toFixed", 1, c);
        createPrimitiveFunction(lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOEXPONENTIAL, "toExponential", 1, c);
        createPrimitiveFunction(lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOPRECISION, "toPrecision", 1, c);

        // 15.8 the Math object
        pv.writePropertyWithAttributes(global, "Math", Value.makeObject(lMath).setAttributes(true, false, false));
        s.writeInternalPrototype(lMath, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lMath, "E", Value.makeNum(Math.E).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lMath, "LN10", Value.makeNum(Math.log(10)).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lMath, "LN2", Value.makeNum(Math.log(2)).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lMath, "LOG2E", Value.makeNum(1 / Math.log(2)).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lMath, "LOG10E", Value.makeNum(1 / Math.log(10)).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lMath, "PI", Value.makeNum(Math.PI).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lMath, "SQRT1_2", Value.makeNum(Math.sqrt(0.5)).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lMath, "SQRT2", Value.makeNum(Math.sqrt(2)).setAttributes(true, true, true));
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ABS, "abs", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ACOS, "acos", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ASIN, "asin", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ATAN, "atan", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ATAN2, "atan2", 2, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_CEIL, "ceil", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_COS, "cos", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_EXP, "exp", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_FLOOR, "floor", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_LOG, "log", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_MAX, "max", 2, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_MIN, "min", 2, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_POW, "pow", 2, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_RANDOM, "random", 0, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ROUND, "round", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_SIN, "sin", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_SQRT, "sqrt", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_TAN, "tan", 1, c);

        // 15.9.4 properties of Date constructor
        createPrimitiveFunction(lDate, lFunProto, ECMAScriptObjects.DATE_PARSE, "parse", 1, c);
        createPrimitiveFunction(lDate, lFunProto, ECMAScriptObjects.DATE_UTC, "UTC", 7, c);
        // properties of Date object
        createPrimitiveFunction(lDate, lFunProto, ECMAScriptObjects.DATE_NOW, "now", 0, c);

        // 15.9.5 properties of the Date prototype object
        s.writeInternalPrototype(lDateProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lDateProto, "constructor", Value.makeObject(lDate).setAttributes(true, false, false));
        s.writeInternalValue(lDateProto, Value.makeNum(Double.NaN));

        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TODATESTRING, "toDateString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOTIMESTRING, "toTimeString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALESTRING, "toLocaleString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALEDATESTRING, "toLocaleDateString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALETIMESTRING, "toLocaleTimeString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_VALUEOF, "valueOf", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETTIME, "getTime", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETFULLYEAR, "getFullYear", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCFULLYEAR, "getUTCFullYear", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMONTH, "getMonth", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMONTH, "getUTCMonth", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETDATE, "getDate", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCDATE, "getUTCDate", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETDAY, "getDay", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCDAY, "getUTCDay", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETHOURS, "getHours", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCHOURS, "getUTCHours", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMINUTES, "getMinutes", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMINUTES, "getUTCMinutes", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETSECONDS, "getSeconds", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCSECONDS, "getUTCSeconds", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMILLISECONDS, "getMilliseconds", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMILLISECONDS, "getUTCMilliseconds", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETTIMEZONEOFFSET, "getTimezoneOffset", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETTIME, "setTime", 1, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMILLISECONDS, "setMilliseconds", 1, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMILLISECONDS, "setUTCMilliseconds", 1, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETSECONDS, "setSeconds", 2, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCSECONDS, "setUTCSeconds", 2, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMINUTES, "setMinutes", 3, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMINUTES, "setUTCMinutes", 3, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETHOURS, "setHours", 4, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCHOURS, "setUTCHours", 4, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETDATE, "setDate", 1, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCDATE, "setUTCDate", 1, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMONTH, "setMonth", 2, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMONTH, "setUTCMonth", 2, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETFULLYEAR, "setFullYear", 3, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCFULLYEAR, "setUTCFullYear", 3, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOISOSTRING, "toISOString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOUTCSTRING, "toUTCString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOJSON, "toJSON", 0, c);
        
        // properties of RegExp object
        writePropertyWeakly(lRegExp, "lastMatch", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$1", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$2", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$3", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$4", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$5", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$6", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$7", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$8", Value.makeAnyStr(), c);
        writePropertyWeakly(lRegExp, "$9", Value.makeAnyStr(), c);

        // 15.10.6 properties of the RegExp prototype object
        s.writeInternalPrototype(lRegExpProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lRegExpProto, "valueOf", Value.makeObject(new ObjectLabel(ECMAScriptObjects.OBJECT_VALUEOF, Kind.FUNCTION))
                .setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lRegExpProto, "constructor", Value.makeObject(lRegExp).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lRegExpProto, "multiline", Value.makeBool(false).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lRegExpProto, "source", Value.makeStr("(?:)").setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lRegExpProto, "ignoreCase", Value.makeBool(false).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lRegExpProto, "global", Value.makeBool(false).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lRegExpProto, "lastIndex", Value.makeNum(0).setAttributes(true, true, true));

        createPrimitiveFunction(lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_EXEC, "exec", 1, c);
        createPrimitiveFunction(lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_TEST, "test", 1, c);
        createPrimitiveFunction(lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_COMPILE, "compile", 1, c);

        // 15.11.4 properties of the Error prototype object
        s.writeInternalPrototype(lErrorProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lErrorProto, "constructor", Value.makeObject(lError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lErrorProto, "name", Value.makeStr("Error").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false)); // implementation dependent
        // string
        createPrimitiveFunction(lErrorProto, lFunProto, ECMAScriptObjects.ERROR_TOSTRING, "toString", 0, c);

        // 15.11.7 native error objects
        s.writeInternalPrototype(lEvalErrorProto, Value.makeObject(lErrorProto));
        pv.writePropertyWithAttributes(lEvalErrorProto, "constructor", Value.makeObject(lEvalError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lEvalErrorProto, "name", Value.makeStr("EvalError").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lEvalErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
        s.writeInternalPrototype(lRangeErrorProto, Value.makeObject(lErrorProto));
        pv.writePropertyWithAttributes(lRangeErrorProto, "constructor", Value.makeObject(lRangeError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lRangeErrorProto, "name", Value.makeStr("RangeError").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lRangeErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
        s.writeInternalPrototype(lReferenceErrorProto, Value.makeObject(lErrorProto));
        pv.writePropertyWithAttributes(lReferenceErrorProto, "constructor", Value.makeObject(lReferenceError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lReferenceErrorProto, "name", Value.makeStr("ReferenceError").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lReferenceErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
        s.writeInternalPrototype(lSyntaxErrorProto, Value.makeObject(lErrorProto));
        pv.writePropertyWithAttributes(lSyntaxErrorProto, "constructor", Value.makeObject(lSyntaxError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lSyntaxErrorProto, "name", Value.makeStr("SyntaxError").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lSyntaxErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
        s.writeInternalPrototype(lTypeErrorProto, Value.makeObject(lErrorProto));
        pv.writePropertyWithAttributes(lTypeErrorProto, "constructor", Value.makeObject(lTypeError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lTypeErrorProto, "name", Value.makeStr("TypeError").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lTypeErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
        s.writeInternalPrototype(lURIErrorProto, Value.makeObject(lErrorProto));
        pv.writePropertyWithAttributes(lURIErrorProto, "constructor", Value.makeObject(lURIError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lURIErrorProto, "name", Value.makeStr("URIError").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lURIErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));

        // Annex B functions
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.ESCAPE, "escape", 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.UNESCAPE, "unescape", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SUBSTR, "substr", 2, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_GETYEAR, "getYear", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_SETYEAR, "setYear", 2, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOGMTSTRING, "toGMTString", 0, c);

        // our own host defined properties
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPVALUE, ECMAScriptObjects.TAJS_DUMPVALUE.toString(), 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPPROTOTYPE, ECMAScriptObjects.TAJS_DUMPPROTOTYPE.toString(), 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPOBJECT, ECMAScriptObjects.TAJS_DUMPOBJECT.toString(), 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPSTATE, ECMAScriptObjects.TAJS_DUMPSTATE.toString(), 0, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPMODIFIEDSTATE, ECMAScriptObjects.TAJS_DUMPMODIFIEDSTATE.toString(), 0, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPATTRIBUTES, ECMAScriptObjects.TAJS_DUMPATTRIBUTES.toString(), 2, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPEXPRESSION, ECMAScriptObjects.TAJS_DUMPEXPRESSION.toString(), 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_DUMPNF, ECMAScriptObjects.TAJS_DUMPNF.toString(), 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_CONVERSION_TO_PRIMITIVE, ECMAScriptObjects.TAJS_CONVERSION_TO_PRIMITIVE.toString(), 2, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_ADD_CONTEXT_SENSITIVITY, ECMAScriptObjects.TAJS_ADD_CONTEXT_SENSITIVITY.toString(), 1, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_ASSERT, ECMAScriptObjects.TAJS_ASSERT.toString(), 3, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_NEW_OBJECT, ECMAScriptObjects.TAJS_NEW_OBJECT.toString(), 0, c);
        createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_ASYNC_LISTEN, ECMAScriptObjects.TAJS_ASYNC_LISTEN.toString(), 0, c);

        if (Options.get().isDOMEnabled()) {
            // build initial DOM state
            createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_GET_UI_EVENT, ECMAScriptObjects.TAJS_GET_UI_EVENT.toString(), 0, c);
            createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_GET_MOUSE_EVENT, ECMAScriptObjects.TAJS_GET_MOUSE_EVENT.toString(), 0, c);
            createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_GET_KEYBOARD_EVENT, ECMAScriptObjects.TAJS_GET_KEYBOARD_EVENT.toString(), 0, c);
            createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_GET_EVENT_LISTENER, ECMAScriptObjects.TAJS_GET_EVENT_LISTENER.toString(), 0, c);
            createPrimitiveFunction(global, lFunProto, ECMAScriptObjects.TAJS_GET_WHEEL_EVENT, ECMAScriptObjects.TAJS_GET_WHEEL_EVENT.toString(), 0, c);

            DOMBuilder.addInitialState(document, c);
        }

        if (Options.get().isDOMEnabled()) {
            for (Entry<EventHandlerKind, Collection<Function>> me : c.getFlowGraph().getEventHandlers().entrySet()) {
                EventHandlerKind e = me.getKey();
                for (Function f : me.getValue()) {
                    createAndRegisterEventHandler(f, e, c);
                }
            }
        }

        s.clearEffects();
        s.freezeBasisStore();

        Context context = c.getAnalysis().getContextSensitivityStrategy().makeInitialContext();
        c.propagateToBasicBlock(s, global_entry_block, context);
    }

    /**
     * Utility method for maybe writing a (non-standard, but common) property
     */
    private void writePropertyWeakly(ObjectLabel objectLabel, String propertyName, Value value, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        pv.writeProperty(Collections.singleton(objectLabel), Value.makeTemporaryStr(propertyName), value, true, true);
    }

    /**
     * Creates a new built-in function.
     */
    public static void createPrimitiveFunction(ObjectLabel target, ObjectLabel internal_proto, HostObject primitive, String name, int arity, Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ObjectLabel objlabel = new ObjectLabel(primitive, Kind.FUNCTION);
        s.newObject(objlabel);
        pv.writePropertyWithAttributes(target, name, Value.makeObject(objlabel).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(objlabel, "length", Value.makeNum(arity).setAttributes(true, true, true));
        s.writeInternalPrototype(objlabel, Value.makeObject(internal_proto));
    }

    public static void createPrimitiveFunctionWeakly(ObjectLabel target, ObjectLabel internal_proto, HostObject primitive, String name, int arity, Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ObjectLabel objlabel = new ObjectLabel(primitive, Kind.FUNCTION);
        s.newObject(objlabel);
        pv.writePropertyWithAttributes(Collections.singleton(target), name, Value.makeObject(objlabel).setAttributes(true, false, false), false, true, true);
        pv.writePropertyWithAttributes(objlabel, "length", Value.makeNum(arity).setAttributes(true, true, true));
    }

    /**
     * Creates a new built-in constructor.
     */
    public static void createPrimitiveConstructor(ObjectLabel target, ObjectLabel internal_proto, ObjectLabel prototype,
                                                  ObjectLabel objlabel, String name, int arity, Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        pv.writePropertyWithAttributes(target, name, Value.makeObject(objlabel).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(objlabel, "length", Value.makeNum(arity).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(objlabel, "prototype", Value.makeObject(prototype).setAttributes(true, true, true));
        s.writeInternalPrototype(objlabel, Value.makeObject(internal_proto));
    }

    /**
     * Creates and registers event handlers
     */
    private static void createAndRegisterEventHandler(Function f, EventHandlerKind kind, Solver.SolverInterface c) {
        State s = c.getState();
        if (Options.get().isDOMEnabled()) {
            switch (kind) {
                case LOAD:
                    DOMEvents.addLoadEventHandler(s, singleton(new ObjectLabel(f)));
                    break;
                case UNLOAD:
                    DOMEvents.addUnloadEventHandler(s, singleton(new ObjectLabel(f)));
                    break;
                case KEYBOARD:
                    DOMEvents.addKeyboardEventHandler(s, singleton(new ObjectLabel(f)));
                    break;
                case MOUSE:
                    DOMEvents.addMouseEventHandler(s, singleton(new ObjectLabel(f)));
                    break;
                case UNKNOWN:
                    DOMEvents.addUnknownEventHandler(s, singleton(new ObjectLabel(f)));
                    break;
                default:
                    throw new AnalysisException("Unknown event handler type");
            }
        }
    }
}
