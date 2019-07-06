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

import dk.brics.tajs.analysis.dom.DOMBuilder;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IInitialStateBuilder;
import dk.brics.tajs.util.Collections;
import net.htmlparser.jericho.Source;

import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Sets up the initial state (Chapter 15).
 */
public class InitialStateBuilder implements IInitialStateBuilder<State, Context, CallEdge, IAnalysisMonitoring, Analysis> {

    /**
     * Object label for the global object.
     */
    public static ObjectLabel GLOBAL;

    /**
     * Object label for Object.prototype.
     */
    public static ObjectLabel OBJECT_PROTOTYPE;

    /**
     * Object label for Function.prototype.
     */
    public static ObjectLabel FUNCTION_PROTOTYPE;

    /**
     * Object label for Array.prototype.
     */
    public static ObjectLabel ARRAY_PROTOTYPE;

    /**
     * Object label for String.prototype.
     */
    public static ObjectLabel STRING_PROTOTYPE;

    /**
     * Object label for Boolean.prototype.
     */
    public static ObjectLabel BOOLEAN_PROTOTYPE;

    /**
     * Object label for Number.prototype.
     */
    public static ObjectLabel NUMBER_PROTOTYPE;

    /**
     * Object label for Date.prototype.
     */
    public static ObjectLabel DATE_PROTOTYPE;

    /**
     * Object label for Proxy.prototype.
     */
    public static ObjectLabel PROXY_PROTOTYPE;

    /**
     * Object label for RegExp.prototype.
     */
    public static ObjectLabel REGEXP_PROTOTYPE;
    /* From ES5, Annex D:
     RegExp.prototype is now a RegExp object rather than an instance of Object. The value of its [[Class]] internal 
     property which is observable using Object.prototype.toString is now 'RegExp' rather than 'Object'.
     */ // TODO: use ES5 semantics of RegExp.prototype object kind?

    /**
     * Object label for Symbol.prototype.
     */
    public static ObjectLabel SYMBOL_PROTOTYPE;

    /**
     * Object label for Error.prototype.
     */
    public static ObjectLabel ERROR_PROTOTYPE;

    /**
     * Object label for EvalError.prototype.
     */
    public static ObjectLabel EVAL_ERROR_PROTOTYPE;

    /**
     * Object label for RangeError.prototype.
     */
    public static ObjectLabel RANGE_ERROR_PROTOTYPE;

    /**
     * Object label for ReferenceError.prototype.
     */
    public static ObjectLabel REFERENCE_ERROR_PROTOTYPE;

    /**
     * Object label for SyntaxError.prototype.
     */
    public static ObjectLabel SYNTAX_ERROR_PROTOTYPE;

    /**
     * Object label for TypeError.prototype.
     */
    public static ObjectLabel TYPE_ERROR_PROTOTYPE;

    /**
     * Object label for URIError.prototype.
     */
    public static ObjectLabel URI_ERROR_PROTOTYPE;

    /**
     * Object label for JSON object
     */
    public static ObjectLabel JSON_OBJECT;

    /**
     * Well-known EC6 Symbols
     */
    public static ObjectLabel UNKNOWN_SYMBOL_INSTANCES;
    public static ObjectLabel WELLKNOWN_SYMBOL_HAS_INSTANCE;
    public static ObjectLabel WELLKNOWN_SYMBOL_IS_CONCAT_SPREADABLE;
    public static ObjectLabel WELLKNOWN_SYMBOL_ITERATOR;
    public static ObjectLabel WELLKNOWN_SYMBOL_MATCH;
    public static ObjectLabel WELLKNOWN_SYMBOL_REPLACE;
    public static ObjectLabel WELLKNOWN_SYMBOL_SEARCH;
    public static ObjectLabel WELLKNOWN_SYMBOL_SPECIES;
    public static ObjectLabel WELLKNOWN_SYMBOL_SPLIT;
    public static ObjectLabel WELLKNOWN_SYMBOL_TO_PRIMITIVE;
    public static ObjectLabel WELLKNOWN_SYMBOL_TO_STRING_TAG;
    public static ObjectLabel WELLKNOWN_SYMBOL_UNSCOPABLES;

    /**
     * Constructs a new InitialStateBuilder object.
     */
    public InitialStateBuilder() {
        reset();
    }

    public static void reset() {
        GLOBAL = ObjectLabel.make(ECMAScriptObjects.GLOBAL, Kind.OBJECT);
        OBJECT_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.OBJECT_PROTOTYPE, Kind.OBJECT);
        FUNCTION_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.FUNCTION_PROTOTYPE, Kind.FUNCTION);
        ARRAY_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.ARRAY_PROTOTYPE, Kind.ARRAY);
        STRING_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.STRING_PROTOTYPE, Kind.STRING); // FIXME: as of ES6, String.prototype is an ordinary object, not a string object -- but not yet supported by Chrome... (GitHub #351)
        BOOLEAN_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.BOOLEAN_PROTOTYPE, Kind.BOOLEAN);
        NUMBER_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.NUMBER_PROTOTYPE, Kind.NUMBER);
        DATE_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.DATE_PROTOTYPE, Kind.DATE);
        PROXY_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.PROXY_PROTOTYPE, Kind.OBJECT);
        REGEXP_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.REGEXP_PROTOTYPE, Kind.OBJECT);
        SYMBOL_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.SYMBOL_PROTOTYPE, Kind.OBJECT);
        ERROR_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.ERROR_PROTOTYPE, Kind.ERROR);
        EVAL_ERROR_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.EVAL_ERROR_PROTOTYPE, Kind.ERROR);
        RANGE_ERROR_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.RANGE_ERROR_PROTOTYPE, Kind.ERROR);
        REFERENCE_ERROR_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.REFERENCE_ERROR_PROTOTYPE, Kind.ERROR);
        SYNTAX_ERROR_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.SYNTAX_ERROR_PROTOTYPE, Kind.ERROR);
        TYPE_ERROR_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.TYPE_ERROR_PROTOTYPE, Kind.ERROR);
        URI_ERROR_PROTOTYPE = ObjectLabel.make(ECMAScriptObjects.URI_ERROR_PROTOTYPE, Kind.ERROR);
        JSON_OBJECT = ObjectLabel.make(ECMAScriptObjects.JSON, Kind.OBJECT);
        UNKNOWN_SYMBOL_INSTANCES = ObjectLabel.make(ECMAScriptObjects.SYMBOL_INSTANCES, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_HAS_INSTANCE = ObjectLabel.make(ECMAScriptObjects.SYMBOL_HAS_INSTANCE, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_IS_CONCAT_SPREADABLE = ObjectLabel.make(ECMAScriptObjects.SYMBOL_IS_CONCAT_SPREADABLE, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_ITERATOR = ObjectLabel.make(ECMAScriptObjects.SYMBOL_ITERATOR, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_MATCH = ObjectLabel.make(ECMAScriptObjects.SYMBOL_MATCH, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_REPLACE = ObjectLabel.make(ECMAScriptObjects.SYMBOL_REPLACE, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_SEARCH = ObjectLabel.make(ECMAScriptObjects.SYMBOL_SEARCH, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_SPECIES = ObjectLabel.make(ECMAScriptObjects.SYMBOL_SPECIES, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_SPLIT = ObjectLabel.make(ECMAScriptObjects.SYMBOL_SPLIT, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_TO_PRIMITIVE = ObjectLabel.make(ECMAScriptObjects.SYMBOL_TO_PRIMITIVE, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_TO_STRING_TAG = ObjectLabel.make(ECMAScriptObjects.SYMBOL_TO_STRING_TAG, Kind.SYMBOL);
        WELLKNOWN_SYMBOL_UNSCOPABLES = ObjectLabel.make(ECMAScriptObjects.SYMBOL_UNSCOPABLES, Kind.SYMBOL);
    }

    /**
     * Sets up the initial state.
     */
    @Override
    public State build(BasicBlock global_entry_block, Solver.SolverInterface c, Source document) {
        // make empty state
        State initialState = new State(c, global_entry_block);
        initialState.setContext(c.getAnalysis().getContextSensitivityStrategy().makeInitialContext());

//        // TODO: include top-level in stacked_funentries?
//        Set<BlockAndContext<Context>> fs = newSet();
//        fs.add(new BlockAndContext<>(initialState.getBasicBlock(), initialState.getContext()));
//        initialState.setStacked(null, fs);

        // add to the state
        c.withState(initialState, () -> {
            buildECMAInitialState(c);
            if (Options.get().isDOMEnabled()) {
                DOMBuilder.build(document, c);
            }
        });

        // cleanup
        initialState.clearEffects();
        initialState.freezeBasisStore();
        return initialState;
    }

    private void buildECMAInitialState(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        ObjectLabel global = GLOBAL; // same as DOMBuilder.WINDOW
        s.newObject(global);
        s.setExecutionContext(new ExecutionContext(ScopeChain.make(global), singleton(global), Value.makeObject(global)));

        ObjectLabel lObject = ObjectLabel.make(ECMAScriptObjects.OBJECT, Kind.FUNCTION);
        s.newObject(lObject);
        ObjectLabel lFunction = ObjectLabel.make(ECMAScriptObjects.FUNCTION, Kind.FUNCTION);
        s.newObject(lFunction);
        ObjectLabel lArray = ObjectLabel.make(ECMAScriptObjects.ARRAY, Kind.FUNCTION);
        s.newObject(lArray);
        ObjectLabel lString = ObjectLabel.make(ECMAScriptObjects.STRING, Kind.FUNCTION);
        s.newObject(lString);
        ObjectLabel lBoolean = ObjectLabel.make(ECMAScriptObjects.BOOLEAN, Kind.FUNCTION);
        s.newObject(lBoolean);
        ObjectLabel lNumber = ObjectLabel.make(ECMAScriptObjects.NUMBER, Kind.FUNCTION);
        s.newObject(lNumber);
        ObjectLabel lDate = ObjectLabel.make(ECMAScriptObjects.DATE, Kind.FUNCTION);
        s.newObject(lDate);
        ObjectLabel lProxy = ObjectLabel.make(ECMAScriptObjects.PROXY, Kind.FUNCTION);
        s.newObject(lProxy);
        ObjectLabel lRegExp = ObjectLabel.make(ECMAScriptObjects.REGEXP, Kind.FUNCTION);
        s.newObject(lRegExp);
        ObjectLabel lSymb = ObjectLabel.make(ECMAScriptObjects.SYMBOL, Kind.FUNCTION);
        s.newObject(lSymb);
        ObjectLabel lError = ObjectLabel.make(ECMAScriptObjects.ERROR, Kind.FUNCTION);
        s.newObject(lError);
        ObjectLabel lEvalError = ObjectLabel.make(ECMAScriptObjects.EVAL_ERROR, Kind.FUNCTION);
        s.newObject(lEvalError);
        ObjectLabel lRangeError = ObjectLabel.make(ECMAScriptObjects.RANGE_ERROR, Kind.FUNCTION);
        s.newObject(lRangeError);
        ObjectLabel lReferenceError = ObjectLabel.make(ECMAScriptObjects.REFERENCE_ERROR, Kind.FUNCTION);
        s.newObject(lReferenceError);
        ObjectLabel lSyntaxError = ObjectLabel.make(ECMAScriptObjects.SYNTAX_ERROR, Kind.FUNCTION);
        s.newObject(lSyntaxError);
        ObjectLabel lTypeError = ObjectLabel.make(ECMAScriptObjects.TYPE_ERROR, Kind.FUNCTION);
        s.newObject(lTypeError);
        ObjectLabel lURIError = ObjectLabel.make(ECMAScriptObjects.URI_ERROR, Kind.FUNCTION);
        s.newObject(lURIError);

        ObjectLabel lMath = ObjectLabel.make(ECMAScriptObjects.MATH, Kind.MATH);
        s.newObject(lMath);

        ObjectLabel lJson = JSON_OBJECT;
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
        ObjectLabel lProxyProto = PROXY_PROTOTYPE;
        s.newObject(lProxyProto);
        ObjectLabel lRegExpProto = REGEXP_PROTOTYPE;
        s.newObject(lRegExpProto);
        ObjectLabel lSymbProto = SYMBOL_PROTOTYPE;
        s.newObject(lSymbProto);
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
        pv.writePropertyWithAttributes(global, "undefined", Value.makeUndef().setAttributes(true, true, true));
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

        // EC6 Symbol
        createPrimitiveConstructor(global, lFunProto, lSymbProto, lSymb, "Symbol", 1, c);

        pv.writePropertyWithAttributes(global, "JSON", Value.makeObject(lJson).setAttributes(true, false, false));
        createPrimitiveFunction(lJson, lFunProto, ECMAScriptObjects.JSON_PARSE, "parse", 1, c);
        createPrimitiveFunction(lJson, lFunProto, ECMAScriptObjects.JSON_STRINGIFY, "stringify", 1, c);
        

        // 15.2.3 Properties of the Object Constructor
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_DEFINE_PROPERTY, "defineProperty", 3, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_CREATE, "create", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_DEFINE_PROPERTIES, "defineProperties", 2, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_FREEZE, "freeze", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_GETOWNPROPERTYDESCRIPTOR, "getOwnPropertyDescriptor", 1, c);//
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_GETPROTOTYPEOF, "getPrototypeOf", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_SETPROTOTYPEOF, "setPrototypeOf", 2, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_ISEXTENSIBLE, "isExtensible", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_ISFROZEN, "isFrozen", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_ISSEALED, "isSealed", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_KEYS, "keys", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_PREVENTEXTENSIONS, "preventExtensions", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_SEAL, "seal", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_GETOWNPROPERTYNAMES, "getOwnPropertyNames", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_IS, "is", 2, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_ASSIGN, "assign", 2, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_GETOWNPROPERTYSYMBOLS, "getOwnPropertySymbols", 1, c);
        createPrimitiveFunction(lObject, lFunProto, ECMAScriptObjects.OBJECT_VALUES, "values", 1, c);

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
        pv.writePropertyWithAttributes(lFunProto, "length", Value.makeNum(0).setAttributes(true, false, true));
        pv.writePropertyWithAttributes(lFunProto, "name", Value.makeStr("").setAttributes(true, true, true));
        createPrimitiveFunction(lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_APPLY, "apply", 2, c);
        createPrimitiveFunction(lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_CALL, "call", 1, c);
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
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SORT, "sort", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SPLICE, "splice", 2, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_UNSHIFT, "unshift", 1, c);
        createPrimitiveFunction(lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_INDEXOF, "indexOf", 1, c);

        // 15.5.3 properties of the String constructor
        createPrimitiveFunction(lString, lFunProto, ECMAScriptObjects.STRING_FROMCHARCODE, "fromCharCode", 1, c);
        createPrimitiveFunction(lString, lFunProto, ECMAScriptObjects.STRING_FROMCODEPOINT, "fromCodePoint", 1, c);

        // 15.5.4 properties of the String prototype object
        s.writeInternalPrototype(lStringProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lStringProto, "length", Value.makeNum(0).setAttributes(true, true, false));  // FIXME: remove this line if switching to ES6 mode (see comment where STRING_PROTOTYPE is created) (GitHub #351)
        s.writeInternalValue(lStringProto, Value.makeStr("")); // FIXME: remove this line if switching to ES6 mode (see comment where STRING_PROTOTYPE is created) (GitHub #351)
        pv.writePropertyWithAttributes(lStringProto, "constructor", Value.makeObject(lString).setAttributes(true, false, false));

        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_VALUEOF, "valueOf", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARAT, "charAt", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARCODEAT, "charCodeAt", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_CONCAT, "concat", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_INDEXOF, "indexOf", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_LASTINDEXOF, "lastIndexOf", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_LOCALECOMPARE, "localeCompare", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_MATCH, "match", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SEARCH, "search", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SLICE, "slice", 2, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SPLIT, "split", 2, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_SUBSTRING, "substring", 2, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOWERCASE, "toLowerCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALELOWERCASE, "toLocaleLowerCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOUPPERCASE, "toUpperCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALEUPPERCASE, "toLocaleUpperCase", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TRIM, "trim", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TRIMLEFT, "trimLeft", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_TRIMRIGHT, "trimRight", 0, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_STARTSWITH, "startsWith", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_ENDSWITH, "endsWith", 1, c);

        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_INCLUDES, "includes", 1, c);
        createPrimitiveFunction(lStringProto, lFunProto, ECMAScriptObjects.STRING_CODEPOINTAT, "codePointAt", 1, c);

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
        pv.writePropertyWithAttributes(lNumber, "EPSILON", Value.makeAnyNumOther().setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lNumber, "POSITIVE_INFINITY", Value.makeNum(Double.POSITIVE_INFINITY).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(lNumber, "NEGATIVE_INFINITY", Value.makeNum(Double.NEGATIVE_INFINITY).setAttributes(true, true, true));
        createPrimitiveFunction(lNumber, lFunProto, ECMAScriptObjects.NUMBER_ISFINITE, "isFinite", 1, c);

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
        createPrimitiveFunction(lNumber, lFunProto, ECMAScriptObjects.NUMBER_ISFINITE, "isFinite", 1, c);
        createPrimitiveFunction(lNumber, lFunProto, ECMAScriptObjects.NUMBER_ISSAFEINTEGER, "isSafeInteger", 1, c);
        createPrimitiveFunction(lNumber, lFunProto, ECMAScriptObjects.NUMBER_ISINTEGER, "isInteger", 1, c);
        createPrimitiveFunction(lNumber, lFunProto, ECMAScriptObjects.NUMBER_ISNAN, "isNan", 1, c);

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
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_IMUL, "imul", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_SIGN, "sign", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_TRUNC, "trunc", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_TANH, "tanh", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ASINH, "asinh", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ACOSH, "acosh", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_ATANH, "atanh", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_HYPOT, "hypot", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_FROUND, "fround", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_CLZ32, "clz32", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_CBRT, "cbrt", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_SINH, "sinh", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_COSH, "cosh", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_LOG10, "log10", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_LOG2, "log2", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_LOG1P, "log1p", 1, c);
        createPrimitiveFunction(lMath, lFunProto, ECMAScriptObjects.MATH_EXPM1, "expm1", 1, c);

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
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOUTCSTRING, "toUTCString", 0, c);
        createPrimitiveFunction(lDateProto, lFunProto, ECMAScriptObjects.DATE_TOJSON, "toJSON", 0, c);

        // ES6 Proxy
        createPrimitiveConstructor(global, lFunProto, lProxyProto, lProxy, "Proxy", 2, c);

        // properties of ES6 Proxy object
        createPrimitiveFunction(lProxyProto, lFunProto, ECMAScriptObjects.PROXY_TOSTRING, "toString", 0, c);

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
        pv.writePropertyWithAttributes(lRegExpProto, "valueOf", Value.makeObject(ObjectLabel.make(ECMAScriptObjects.OBJECT_VALUEOF, Kind.FUNCTION))
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

        // properties of the Error object
        pv.writeProperty(lError, "stackTraceLimit", Value.makeAnyNumUInt());
        createPrimitiveFunction(lError, lFunProto, ECMAScriptObjects.ERROR_CAPTURESTACKTRACE, "captureStackTrace", 2, c);

        // ES6 Symbol properties
        pv.writePropertyWithAttributes(global, "Symbol", Value.makeObject(lSymb).setAttributes(true, false, false));

        pv.writePropertyWithAttributes(lSymb, "length", Value.makeNum(0).setAttributes(true, true, true));

        pv.writeProperty(lSymb, "hasInstance", Value.makeSymbol(WELLKNOWN_SYMBOL_HAS_INSTANCE).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "isConcatSpreadable", Value.makeSymbol(WELLKNOWN_SYMBOL_IS_CONCAT_SPREADABLE).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "iterator", Value.makeSymbol(WELLKNOWN_SYMBOL_ITERATOR).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "match", Value.makeSymbol(WELLKNOWN_SYMBOL_MATCH).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "replace", Value.makeSymbol(WELLKNOWN_SYMBOL_REPLACE).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "search", Value.makeSymbol(WELLKNOWN_SYMBOL_SEARCH).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "species", Value.makeSymbol(WELLKNOWN_SYMBOL_SPECIES).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "split", Value.makeSymbol(WELLKNOWN_SYMBOL_SPLIT).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "toPrimitive", Value.makeSymbol(WELLKNOWN_SYMBOL_TO_PRIMITIVE).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "toStringTag", Value.makeSymbol(WELLKNOWN_SYMBOL_TO_STRING_TAG).setAttributes(true, true, true));
        pv.writeProperty(lSymb, "unscopables", Value.makeSymbol(WELLKNOWN_SYMBOL_UNSCOPABLES).setAttributes(true, true, true));

        createPrimitiveFunction(lSymb, lFunProto, ECMAScriptObjects.SYMBOL_FOR, "for", 1, c);
        createPrimitiveFunction(lSymb, lFunProto, ECMAScriptObjects.SYMBOL_KEYFOR, "keyFor", 1, c);

        createPrimitiveFunction(lSymbProto, lFunProto, ECMAScriptObjects.SYMBOL_TOSTRING, "toString", 0, c);
        createPrimitiveFunction(lSymbProto, lFunProto, ECMAScriptObjects.SYMBOL_TOSOURCE, "toSource", 0, c);
        createPrimitiveFunction(lSymbProto, lFunProto, ECMAScriptObjects.SYMBOL_VALUEOF, "valueOf", 0, c);
        //createPrimitiveFunction(lSymbProto, lFunProto, ECMAScriptObjects.SYMBOL_PROTOTYPE_TOPRIMITIVE, SymbolPKey.make(ECMAScriptObjects.SYMBOL_TO_PRIMITIVE)), 0, c); // FIXME: github 515 (name is a string, key is a symbol)
        pv.writeProperty(lSymbProto, "constructor", Value.makeObject(lSymb));

        // ES6 Symbol instance properties

        Set<ObjectLabel> allSymbols = newSet();
        allSymbols.add(UNKNOWN_SYMBOL_INSTANCES);
        allSymbols.add(WELLKNOWN_SYMBOL_HAS_INSTANCE);
        allSymbols.add(WELLKNOWN_SYMBOL_IS_CONCAT_SPREADABLE);
        allSymbols.add(WELLKNOWN_SYMBOL_ITERATOR);
        allSymbols.add(WELLKNOWN_SYMBOL_MATCH);
        allSymbols.add(WELLKNOWN_SYMBOL_REPLACE);
        allSymbols.add(WELLKNOWN_SYMBOL_SEARCH);
        allSymbols.add(WELLKNOWN_SYMBOL_SPECIES);
        allSymbols.add(WELLKNOWN_SYMBOL_SPLIT);
        allSymbols.add(WELLKNOWN_SYMBOL_TO_PRIMITIVE);
        allSymbols.add(WELLKNOWN_SYMBOL_TO_STRING_TAG);
        allSymbols.add(WELLKNOWN_SYMBOL_UNSCOPABLES);

        for (ObjectLabel l : allSymbols) {
            s.newObject(l);
            s.writeInternalPrototype(l, Value.makeObject(InitialStateBuilder.SYMBOL_PROTOTYPE));
        }

        // properties of the Error object
        pv.writeProperty(lError, "stackTraceLimit", Value.makeAnyNumUInt());
        createPrimitiveFunction(lError, lFunProto, ECMAScriptObjects.ERROR_CAPTURESTACKTRACE, "captureStackTrace", 2, c);

        // 15.11.4 properties of the Error prototype object
        s.writeInternalPrototype(lErrorProto, Value.makeObject(lObjectPrototype));
        pv.writePropertyWithAttributes(lErrorProto, "constructor", Value.makeObject(lError).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lErrorProto, "name", Value.makeStr("Error").setAttributes(true, false, false));
        pv.writePropertyWithAttributes(lErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false)); // implementation dependent
        pv.writePropertyWithAttributes(lErrorProto, "stack", Value.makeAnyStr().setAttributes(true, false, false)); // implementation dependent

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
        ObjectLabel objlabel = ObjectLabel.make(primitive, Kind.FUNCTION);
        c.getState().newObject(objlabel);
        createPrimitiveFunctionOrConstructor(target, internal_proto, name, arity, objlabel, c);
    }

    /**
     * Creates a new built-in constructor.
     */
    public static void createPrimitiveConstructor(ObjectLabel target, ObjectLabel internal_proto, ObjectLabel prototype,
                                                  ObjectLabel objlabel, String name, int arity, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        createPrimitiveFunctionOrConstructor(target, internal_proto, name, arity, objlabel, c);
        pv.writePropertyWithAttributes(objlabel, "prototype", Value.makeObject(prototype).setAttributes(true, true, true));
    }

    private static void createPrimitiveFunctionOrConstructor(ObjectLabel target, ObjectLabel internal_proto, String name, int arity, ObjectLabel objlabel, Solver.SolverInterface c) {
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        pv.writePropertyWithAttributes(target, name, Value.makeObject(objlabel).setAttributes(true, false, false));
        pv.writePropertyWithAttributes(objlabel, "length", Value.makeNum(arity).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(objlabel, "name", Value.makeStr(name).setAttributes(true, false, true));
        c.getState().writeInternalPrototype(objlabel, Value.makeObject(internal_proto));
    }
}
