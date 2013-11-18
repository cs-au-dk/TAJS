/*
 * Copyright 2009-2013 Aarhus University
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

import static dk.brics.tajs.util.Collections.singleton;

import org.jdom.Document;

import dk.brics.tajs.analysis.dom.DOMBuilder;
import dk.brics.tajs.analysis.dom.DOMEvents;
import dk.brics.tajs.analysis.dom.DOMVisitor;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.CallbackKind;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.ExecutionContext;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.GenericSolver;
import dk.brics.tajs.solver.IInitialStateBuilder;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

// TODO: split this into nativeobjects, dom, and dsl

/**
 * Sets up the initial state (Chapter 15).
 */
public class InitialStateBuilder implements IInitialStateBuilder<State, Context, CallEdge<State>> { // TODO: to be replaced by the host model system

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
	public void addInitialState(BasicBlock global_entry_block, GenericSolver<State, Context, CallEdge<State>, ?, ?>.SolverInterface c, Document document) {
		State s = new State(c, global_entry_block);
		ObjectLabel global = GLOBAL; // same as DOMBuilder.WINDOW
		s.newObject(global);

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
		s.writePropertyWithAttributes(global, "NaN", Value.makeNum(Double.NaN).setAttributes(true, true, false));
		s.writePropertyWithAttributes(global, "Infinity", Value.makeNum(Double.POSITIVE_INFINITY).setAttributes(true, true, false));
		s.writePropertyWithAttributes(global, "undefined", Value.makeUndef().setAttributes(true, true, false));
		// TODO: 15.1 the values of the [[Prototype]] and [[Class]] properties of the global object are implementation-dependent
		s.writeInternalPrototype(global, Value.makeObject(lObjectPrototype)); // Rhino's implementation choice

		// 15.1.2 function properties of the global object
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.EVAL, "eval", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.PARSEINT, "parseInt", 2);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.PARSEFLOAT, "parseFloat", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ISNAN, "isNaN", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ISFINITE, "isFinite", 1);

		// 15.1.3 URI handling function properties
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DECODEURI, "decodeURI", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DECODEURICOMPONENT, "decodeURIComponent", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ENCODEURI, "encodeURI", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ENCODEURICOMPONENT, "encodeURIComponent", 1);

		// Rhino's print and alert functions
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.PRINT, "print", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ALERT, "alert", 1);

		// 15.1.4 constructor properties of the global object
		createPrimitiveConstructor(s, global, lFunProto, lObjectPrototype, lObject, "Object", 1);
		createPrimitiveConstructor(s, global, lFunProto, lFunProto, lFunction, "Function", 1);
		createPrimitiveConstructor(s, global, lFunProto, lArrayProto, lArray, "Array", 1);
		createPrimitiveConstructor(s, global, lFunProto, lStringProto, lString, "String", 1);
		createPrimitiveConstructor(s, global, lFunProto, lBooleanProto, lBoolean, "Boolean", 1);
		createPrimitiveConstructor(s, global, lFunProto, lNumberProto, lNumber, "Number", 1);
		createPrimitiveConstructor(s, global, lFunProto, lDateProto, lDate, "Date", 7);
		createPrimitiveConstructor(s, global, lFunProto, lRegExpProto, lRegExp, "RegExp", 2);
		createPrimitiveConstructor(s, global, lFunProto, lErrorProto, lError, "Error", 1);
		createPrimitiveConstructor(s, global, lFunProto, lEvalErrorProto, lEvalError, "EvalError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lRangeErrorProto, lRangeError, "RangeError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lReferenceErrorProto, lReferenceError, "ReferenceError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lSyntaxErrorProto, lSyntaxError, "SyntaxError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lTypeErrorProto, lTypeError, "TypeError", 1);
		createPrimitiveConstructor(s, global, lFunProto, lURIErrorProto, lURIError, "URIError", 1);

		// 15.2.3 Properties of the Object Constructor
		createPrimitiveFunction(s, lObject, lFunProto, ECMAScriptObjects.OBJECT_DEFINE_PROPERTY, "defineProperty", 3);

		// 15.2.4 properties of the Object prototype object
		s.writeInternalPrototype(lObjectPrototype, Value.makeNull());
		s.writePropertyWithAttributes(lObjectPrototype, "constructor", Value.makeObject(lObject).setAttributes(true, false, false));
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_HASOWNPROPERTY, "hasOwnProperty", 1);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_ISPROTOTYPEOF, "isPrototypeOf", 1);
		createPrimitiveFunction(s, lObjectPrototype, lFunProto, ECMAScriptObjects.OBJECT_PROPERTYISENUMERABLE, "propertyIsEnumerable", 1);

		// 15.3.4 properties of the Function prototype object
		s.writeInternalPrototype(lFunProto, Value.makeObject(lObjectPrototype));
		s.writePropertyWithAttributes(lFunProto, "constructor", Value.makeObject(lFunction).setAttributes(true, false, false));
		createPrimitiveFunction(s, lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_APPLY, "apply", 2);
		createPrimitiveFunction(s, lFunProto, lFunProto, ECMAScriptObjects.FUNCTION_CALL, "call", 1);

        // 15.4.3.2 properties of the Array constructor
        createPrimitiveFunction(s, lArray, lFunProto, ECMAScriptObjects.ARRAY_ISARRAY, "isArray", 1);

		// 15.4.4 properties of the Array prototype object
		s.writeInternalPrototype(lArrayProto, Value.makeObject(lObjectPrototype));
		s.writePropertyWithAttributes(lArrayProto, "length", Value.makeNum(0).setAttributes(true, true, false));
		s.writePropertyWithAttributes(lArrayProto, "constructor", Value.makeObject(lArray).setAttributes(true, false, false));
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_CONCAT, "concat", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_FOREACH, "forEach", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_JOIN, "join", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_POP, "pop", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_PUSH, "push", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_REVERSE, "reverse", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SHIFT, "shift", 0);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SLICE, "slice", 2);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SOME, "some", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SORT, "sort", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_SPLICE, "splice", 2);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_UNSHIFT, "unshift", 1);
		createPrimitiveFunction(s, lArrayProto, lFunProto, ECMAScriptObjects.ARRAY_INDEXOF, "indexOf", 1);

		// 15.5.3 properties of the String constructor
		createPrimitiveFunction(s, lString, lFunProto, ECMAScriptObjects.STRING_FROMCHARCODE, "fromCharCode", 1);

		// 15.5.4 properties of the String prototype object
		s.writeInternalPrototype(lStringProto, Value.makeObject(lObjectPrototype));
		s.writePropertyWithAttributes(lStringProto, "constructor", Value.makeObject(lString).setAttributes(true, false, false));
		s.writeInternalValue(lStringProto, Value.makeStr(""));
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARAT, "charAt", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_CHARCODEAT, "charCodeAt", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_CONCAT, "concat", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_INDEXOF, "indexOf", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_LASTINDEXOF, "lastIndexOf", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_LOCALECOMPARE, "localeCompare", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_MATCH, "match", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_REPLACE, "replace", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SEARCH, "search", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SLICE, "slice", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SPLIT, "split", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SUBSTRING, "substring", 2);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOWERCASE, "toLowerCase", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALELOWERCASE, "toLocaleLowerCase", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOUPPERCASE, "toUpperCase", 0);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TOLOCALEUPPERCASE, "toLocaleUpperCase", 0);
        createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_TRIM, "trim", 0);

		// 15.6.4 properties of the Boolean prototype object
		s.writeInternalPrototype(lBooleanProto, Value.makeObject(lObjectPrototype));
		s.writeInternalValue(lBooleanProto, Value.makeBool(false));
		s.writePropertyWithAttributes(lBooleanProto, "constructor", Value.makeObject(lBoolean).setAttributes(true, false, false));
		createPrimitiveFunction(s, lBooleanProto, lFunProto, ECMAScriptObjects.BOOLEAN_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lBooleanProto, lFunProto, ECMAScriptObjects.BOOLEAN_VALUEOF, "valueOf", 0);

		// 15.7.3 properties of the Number constructor
		s.writePropertyWithAttributes(lNumber, "MAX_VALUE", Value.makeNum(Double.MAX_VALUE).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lNumber, "MIN_VALUE", Value.makeNum(Double.MIN_VALUE).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lNumber, "NaN", Value.makeNum(Double.NaN).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lNumber, "POSITIVE_INFINITY", Value.makeNum(Double.POSITIVE_INFINITY).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lNumber, "NEGATIVE_INFINITY", Value.makeNum(Double.NEGATIVE_INFINITY).setAttributes(true, true, true));

		// 15.7.4 properties of the Number prototype object
		s.writeInternalPrototype(lNumberProto, Value.makeObject(lObjectPrototype));
		s.writeInternalValue(lNumberProto, Value.makeNum(0));
		s.writePropertyWithAttributes(lNumberProto, "constructor", Value.makeObject(lNumber).setAttributes(true, false, false));
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOSTRING, "toString", 1);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOFIXED, "toFixed", 1);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOEXPONENTIAL, "toExponential", 1);
		createPrimitiveFunction(s, lNumberProto, lFunProto, ECMAScriptObjects.NUMBER_TOPRECISION, "toPrecision", 1);

		// 15.8 the Math object
		s.writePropertyWithAttributes(global, "Math", Value.makeObject(lMath).setAttributes(true, false, false));
		s.writeInternalPrototype(lMath, Value.makeObject(lObjectPrototype));
		s.writePropertyWithAttributes(lMath, "E", Value.makeNum(Math.E).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lMath, "LN10", Value.makeNum(Math.log(10)).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lMath, "LN2", Value.makeNum(Math.log(2)).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lMath, "LOG2E", Value.makeNum(1 / Math.log(2)).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lMath, "LOG10E", Value.makeNum(1 / Math.log(10)).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lMath, "PI", Value.makeNum(Math.PI).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lMath, "SQRT1_2", Value.makeNum(Math.sqrt(0.5)).setAttributes(true, true, true));
		s.writePropertyWithAttributes(lMath, "SQRT2", Value.makeNum(Math.sqrt(2)).setAttributes(true, true, true));
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ABS, "abs", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ACOS, "acos", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ASIN, "asin", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ATAN, "atan", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ATAN2, "atan2", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_CEIL, "ceil", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_COS, "cos", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_EXP, "exp", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_FLOOR, "floor", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_LOG, "log", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_MAX, "max", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_MIN, "min", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_POW, "pow", 2);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_RANDOM, "random", 0);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_ROUND, "round", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_SIN, "sin", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_SQRT, "sqrt", 1);
		createPrimitiveFunction(s, lMath, lFunProto, ECMAScriptObjects.MATH_TAN, "tan", 1);

		// 15.9.4 properties of Date constructor
		createPrimitiveFunction(s, lDate, lFunProto, ECMAScriptObjects.DATE_PARSE, "parse", 1);
		createPrimitiveFunction(s, lDate, lFunProto, ECMAScriptObjects.DATE_UTC, "UTC", 7);

		// 15.9.5 properties of the Date prototype object
		s.writeInternalPrototype(lDateProto, Value.makeObject(lObjectPrototype));
		s.writePropertyWithAttributes(lDateProto, "constructor", Value.makeObject(lDate).setAttributes(true, false, false));
		s.writeInternalValue(lDateProto, Value.makeNum(Double.NaN));
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOSTRING, "toString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TODATESTRING, "toDateString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOTIMESTRING, "toTimeString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALESTRING, "toLocaleString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALEDATESTRING, "toLocaleDateString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOLOCALETIMESTRING, "toLocaleTimeString", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_VALUEOF, "valueOf", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETTIME, "getTime", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETFULLYEAR, "getFullYear", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCFULLYEAR, "getUTCFullYear", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMONTH, "getMonth", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMONTH, "getUTCMonth", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETDATE, "getDate", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCDATE, "getUTCDate", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETDAY, "getDay", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCDAY, "getUTCDay", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETHOURS, "getHours", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCHOURS, "getUTCHours", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMINUTES, "getMinutes", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMINUTES, "getUTCMinutes", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETSECONDS, "getSeconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCSECONDS, "getUTCSeconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETMILLISECONDS, "getMilliseconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETUTCMILLISECONDS, "getUTCMilliseconds", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETTIMEZONEOFFSET, "getTimezoneOffset", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETTIME, "setTime", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMILLISECONDS, "setMilliseconds", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMILLISECONDS, "setUTCMilliseconds", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETSECONDS, "setSeconds", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCSECONDS, "setUTCSeconds", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMINUTES, "setMinutes", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMINUTES, "setUTCMinutes", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETHOURS, "setHours", 4);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCHOURS, "setUTCHours", 4);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETDATE, "setDate", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCDATE, "setUTCDate", 1);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETMONTH, "setMonth", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCMONTH, "setUTCMonth", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETFULLYEAR, "setFullYear", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETUTCFULLYEAR, "setUTCFullYear", 3);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOUTCSTRING, "toUTCFullString", 0);

		// 15.10.6 properties of the RegExp prototype object
		s.writeInternalPrototype(lRegExpProto, Value.makeObject(lObjectPrototype));
		s.writePropertyWithAttributes(lRegExpProto, "valueOf", Value.makeObject(new ObjectLabel(ECMAScriptObjects.OBJECT_VALUEOF, Kind.FUNCTION))
				.setAttributes(true, false, false));
		s.writePropertyWithAttributes(lRegExpProto, "constructor", Value.makeObject(lRegExp).setAttributes(true, false, false));
		createPrimitiveFunction(s, lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_EXEC, "exec", 1);
		createPrimitiveFunction(s, lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_TEST, "test", 1);
		createPrimitiveFunction(s, lRegExpProto, lFunProto, ECMAScriptObjects.REGEXP_TOSTRING, "toString", 0);

		// 15.11.4 properties of the Error prototype object
		s.writeInternalPrototype(lErrorProto, Value.makeObject(lObjectPrototype));
		s.writePropertyWithAttributes(lErrorProto, "constructor", Value.makeObject(lError).setAttributes(true, false, false));
		s.writePropertyWithAttributes(lErrorProto, "name", Value.makeStr("Error").setAttributes(true, false, false));
		s.writePropertyWithAttributes(lErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false)); // implementation dependent
																												// string
		createPrimitiveFunction(s, lErrorProto, lFunProto, ECMAScriptObjects.ERROR_TOSTRING, "toString", 0);

		// 15.11.7 native error objects
		s.writeInternalPrototype(lEvalErrorProto, Value.makeObject(lErrorProto));
		s.writePropertyWithAttributes(lEvalErrorProto, "constructor", Value.makeObject(lEvalError).setAttributes(true, false, false));
		s.writePropertyWithAttributes(lEvalErrorProto, "name", Value.makeStr("EvalError").setAttributes(true, false, false));
		s.writePropertyWithAttributes(lEvalErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
		s.writeInternalPrototype(lRangeErrorProto, Value.makeObject(lErrorProto));
		s.writePropertyWithAttributes(lRangeErrorProto, "constructor", Value.makeObject(lRangeError).setAttributes(true, false, false));
		s.writePropertyWithAttributes(lRangeErrorProto, "name", Value.makeStr("RangeError").setAttributes(true, false, false));
		s.writePropertyWithAttributes(lRangeErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
		s.writeInternalPrototype(lReferenceErrorProto, Value.makeObject(lErrorProto));
		s.writePropertyWithAttributes(lReferenceErrorProto, "constructor", Value.makeObject(lReferenceError).setAttributes(true, false, false));
		s.writePropertyWithAttributes(lReferenceErrorProto, "name", Value.makeStr("ReferenceError").setAttributes(true, false, false));
		s.writePropertyWithAttributes(lReferenceErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
		s.writeInternalPrototype(lSyntaxErrorProto, Value.makeObject(lErrorProto));
		s.writePropertyWithAttributes(lSyntaxErrorProto, "constructor", Value.makeObject(lSyntaxError).setAttributes(true, false, false));
		s.writePropertyWithAttributes(lSyntaxErrorProto, "name", Value.makeStr("SyntaxError").setAttributes(true, false, false));
		s.writePropertyWithAttributes(lSyntaxErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
		s.writeInternalPrototype(lTypeErrorProto, Value.makeObject(lErrorProto));
		s.writePropertyWithAttributes(lTypeErrorProto, "constructor", Value.makeObject(lTypeError).setAttributes(true, false, false));
		s.writePropertyWithAttributes(lTypeErrorProto, "name", Value.makeStr("TypeError").setAttributes(true, false, false));
		s.writePropertyWithAttributes(lTypeErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));
		s.writeInternalPrototype(lURIErrorProto, Value.makeObject(lErrorProto));
		s.writePropertyWithAttributes(lURIErrorProto, "constructor", Value.makeObject(lURIError).setAttributes(true, false, false));
		s.writePropertyWithAttributes(lURIErrorProto, "name", Value.makeStr("URIError").setAttributes(true, false, false));
		s.writePropertyWithAttributes(lURIErrorProto, "message", Value.makeAnyStr().setAttributes(true, false, false));

		// Annex B functions
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ESCAPE, "escape", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.UNESCAPE, "unescape", 1);
		createPrimitiveFunction(s, lStringProto, lFunProto, ECMAScriptObjects.STRING_SUBSTR, "substr", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_GETYEAR, "getYear", 0);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_SETYEAR, "setYear", 2);
		createPrimitiveFunction(s, lDateProto, lFunProto, ECMAScriptObjects.DATE_TOGMTSTRING, "toGMTString", 0);

		// our own host defined properties
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSERT, "assert", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPVALUE, "dumpValue", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPPROTOTYPE, "dumpPrototype", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPOBJECT, "dumpObject", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPSTATE, "dumpState", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPMODIFIEDSTATE, "dumpModifiedState", 0);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPATTRIBUTES, "dumpAttributes", 2);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPOBJECTORIGIN, "dumpObjectOrigin", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPEXPRESSION, "dumpExp", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.DUMPNF, "dumpNF", 1);
        createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSERT_SINGLE_NUM, "assertSingleNum", 1);
        createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSERT_ONE_OBJ, "assertOneObj", 1);
        createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSERT_ABSENT, "assertAbsent", 1);
        createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSERT_MOST_RECENT_OBJ, "assertMostRecentObj", 1);
        createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.ASSERT_SUMMARY_OBJ, "assertSummaryObj", 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.CONVERSION_TO_PRIMITIVE, "conversionToPrimitive", 2);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_ADD_CONTEXT_SENSITIVITY, ECMAScriptObjects.TAJS_ADD_CONTEXT_SENSITIVITY.toString(), 1);
		createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_NEW_OBJECT, ECMAScriptObjects.TAJS_NEW_OBJECT.toString(), 0);
		
        if (Options.isDOMEnabled()) {
    		// build initial DOM state
        	createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_UI_EVENT, "TAJS_getUIEvent", 0);
        	createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_MOUSE_EVENT, "TAJS_getMouseEvent", 0);
        	createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_KEYBOARD_EVENT, "TAJS_getKeyboardEvent", 0);
        	createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_EVENT_LISTENER, "TAJS_getEventListener", 0);
        	createPrimitiveFunction(s, global, lFunProto, ECMAScriptObjects.TAJS_GET_WHEEL_EVENT, "TAJS_getWheelEvent", 0);

			DOMBuilder.addInitialState(s);
		}

        if (Options.isDOMEnabled()) {
            for (Function f : c.getFlowGraph().getCallbacksByKind(CallbackKind.LOAD)) {
                createAndRegisterEventHandler(s, f, CallbackKind.LOAD);
            }
            for (Function f : c.getFlowGraph().getCallbacksByKind(CallbackKind.UNKNOWN)) {
                createAndRegisterEventHandler(s, f, CallbackKind.UNKNOWN);
            }
            for (Function f : c.getFlowGraph().getCallbacksByKind(CallbackKind.MOUSE)) {
                createAndRegisterEventHandler(s, f, CallbackKind.MOUSE);
            }
            for (Function f : c.getFlowGraph().getCallbacksByKind(CallbackKind.KEYBOARD)) {
                createAndRegisterEventHandler(s, f, CallbackKind.KEYBOARD);
            }
            for (Function f : c.getFlowGraph().getCallbacksByKind(CallbackKind.UNKNOWN)) {
                createAndRegisterEventHandler(s, f, CallbackKind.UNKNOWN);
            }
        }

		if (document != null && Options.isDOMEnabled()) {
            DOMVisitor visitor = new DOMVisitor(document, s);
            visitor.visitDocument();
        }

		s.setExecutionContext(new ExecutionContext(ScopeChain.make(global), singleton(global), singleton(global)));
		s.clearEffects();
		s.freezeBasisStore();

        Context context = Context.makeInitialContext(s, global_entry_block);
		c.propagateToBasicBlock(s, global_entry_block, context);
	}

    /**
	 * Creates a new built-in function.
	 */
	public static void createPrimitiveFunction(State s, ObjectLabel target, ObjectLabel internal_proto, HostObject primitive, String name, int arity) {
		ObjectLabel objlabel = new ObjectLabel(primitive, Kind.FUNCTION);
		s.newObject(objlabel);
		s.writePropertyWithAttributes(target, name, Value.makeObject(objlabel).setAttributes(true, false, false));
		s.writePropertyWithAttributes(objlabel, "length", Value.makeNum(arity).setAttributes(true, true, true));
		s.writeInternalPrototype(objlabel, Value.makeObject(internal_proto));
	}

	/**
	 * Creates a new built-in constructor.
	 */
	public static void createPrimitiveConstructor(State s, ObjectLabel target, ObjectLabel internal_proto, ObjectLabel prototype,
			ObjectLabel objlabel, String name, int arity) {
		s.writePropertyWithAttributes(target, name, Value.makeObject(objlabel).setAttributes(true, false, false));
		s.writePropertyWithAttributes(objlabel, "length", Value.makeNum(arity).setAttributes(true, true, true));
		s.writePropertyWithAttributes(objlabel, "prototype", Value.makeObject(prototype).setAttributes(true, true, true));
		s.writeInternalPrototype(objlabel, Value.makeObject(internal_proto));
	}

    /**
     * Creates and registers event handlers
     */
    private static void createAndRegisterEventHandler(State s, Function f, CallbackKind kind) {
        if (Options.isDOMEnabled()) {
            switch (kind) {
            case LOAD:
                DOMEvents.addLoadEventHandler(s, Collections.singleton(new ObjectLabel(f)));
                break;
            case UNLOAD:
                DOMEvents.addUnloadEventHandler(s, Collections.singleton(new ObjectLabel(f)));
                break;
            case KEYBOARD:
                DOMEvents.addKeyboardEventHandler(s, Collections.singleton(new ObjectLabel(f)));
                break;
            case MOUSE:
                DOMEvents.addMouseEventHandler(s, Collections.singleton(new ObjectLabel(f)));
                break;
            case UNKNOWN:
                DOMEvents.addUnknownEventHandler(s, Collections.singleton(new ObjectLabel(f)));
                break;
            default:
                throw new AnalysisException("Unknown event handler type");
            }
        }
    }
}
