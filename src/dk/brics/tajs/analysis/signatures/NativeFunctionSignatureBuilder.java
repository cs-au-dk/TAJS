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

package dk.brics.tajs.analysis.signatures;

import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.analysis.signatures.types.Parameter;
import dk.brics.tajs.analysis.signatures.types.Signature;
import dk.brics.tajs.analysis.signatures.types.ValueDescription;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.util.AnalysisException;

import java.util.Map;

import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ALERT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_CONCAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_INDEXOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_ISARRAY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_JOIN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_POP;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_PUSH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_REVERSE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_SHIFT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_SLICE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_SORT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_SPLICE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_TOLOCALESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_UNSHIFT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ARRAY_VALUES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.BOOLEAN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.BOOLEAN_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.BOOLEAN_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETDATE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETDAY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETFULLYEAR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETHOURS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETMILLISECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETMINUTES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETMONTH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETSECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETTIME;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETTIMEZONEOFFSET;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCDATE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCDAY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCFULLYEAR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCHOURS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCMILLISECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCMINUTES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCMONTH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETUTCSECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_GETYEAR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_NOW;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_PARSE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETDATE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETFULLYEAR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETHOURS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETMILLISECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETMINUTES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETMONTH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETSECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETTIME;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETUTCDATE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETUTCFULLYEAR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETUTCHOURS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETUTCMILLISECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETUTCMINUTES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETUTCMONTH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_SETUTCSECONDS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TODATESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TOGMTSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TOLOCALEDATESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TOLOCALESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TOLOCALETIMESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TOTIMESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_TOUTCSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_UTC;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DATE_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DECODEURI;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.DECODEURICOMPONENT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ENCODEURI;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ENCODEURICOMPONENT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ERROR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ERROR_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ESCAPE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.EVAL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.EVAL_ERROR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.FUNCTION;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.FUNCTION_APPLY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.FUNCTION_CALL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.FUNCTION_PROTOTYPE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.FUNCTION_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ISFINITE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.ISNAN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.JSON_PARSE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.JSON_STRINGIFY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ABS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ACOS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ACOSH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ASIN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ASINH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ATAN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ATAN2;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ATANH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_CBRT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_CEIL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_CLZ32;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_COS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_COSH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_EXP;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_EXPM1;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_FLOOR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_FROUND;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_HYPOT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_IMUL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_LOG;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_LOG10;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_LOG1P;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_LOG2;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_MAX;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_MIN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_POW;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_RANDOM;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_ROUND;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_SIGN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_SIN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_SINH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_SQRT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_TAN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_TANH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.MATH_TRUNC;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_ISFINITE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_ISINTEGER;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_ISNAN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_ISSAFEINTEGER;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOEXPONENTIAL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOFIXED;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOLOCALESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOPRECISION;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.NUMBER_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_ASSIGN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_CREATE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_DEFINEGETTER;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_DEFINESETTER;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_DEFINE_PROPERTIES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_DEFINE_PROPERTY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_FREEZE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_GETOWNPROPERTYDESCRIPTOR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_GETOWNPROPERTYNAMES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_GETOWNPROPERTYSYMBOLS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_GETPROTOTYPEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_HASOWNPROPERTY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_IS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_ISEXTENSIBLE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_ISFROZEN;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_ISPROTOTYPEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_ISSEALED;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_KEYS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_PREVENTEXTENSIONS;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_PROPERTYISENUMERABLE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_SEAL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_SETPROTOTYPEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_TOLOCALESTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.OBJECT_VALUES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.PARSEFLOAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.PARSEINT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.PRINT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.PROXY;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.RANGE_ERROR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REFERENCE_ERROR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_COMPILE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_EXEC;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_TEST;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.REGEXP_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_CHARAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_CHARCODEAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_CODEPOINTAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_CONCAT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_ENDSWITH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_FROMCHARCODE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_FROMCODEPOINT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_INCLUDES;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_INDEXOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_LASTINDEXOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_LOCALECOMPARE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_MATCH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SEARCH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SLICE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SPLIT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_STARTSWITH;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SUBSTR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_SUBSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOLOCALELOWERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOLOCALEUPPERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOLOWERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TOUPPERCASE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TRIM;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TRIMLEFT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_TRIMRIGHT;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.STRING_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.SYMBOL;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.SYMBOL_FOR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.SYMBOL_KEYFOR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.SYMBOL_TOSOURCE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.SYMBOL_TOSTRING;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.SYMBOL_VALUEOF;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.SYNTAX_ERROR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.TYPE_ERROR;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.UNESCAPE;
import static dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects.URI_ERROR;
import static dk.brics.tajs.analysis.signatures.NativeFunctionSignatureUtilities.MandatoryParameters;
import static dk.brics.tajs.analysis.signatures.NativeFunctionSignatureUtilities.None;
import static dk.brics.tajs.analysis.signatures.NativeFunctionSignatureUtilities.OptionalParameters;
import static dk.brics.tajs.analysis.signatures.NativeFunctionSignatureUtilities.Receivers;
import static dk.brics.tajs.util.Collections.newMap;

/**
 * Signature definitions for native functions.
 */
public class NativeFunctionSignatureBuilder {

    Map<HostObject, Signature> signatures;

    public NativeFunctionSignatureBuilder() {
        signatures = newMap();
        initializeSignatures();
    }

    /**
     * Registers the signature for a static function, e.g. 'Object.getPrototypeOf' or constructors like 'Object'.
     */
    private void addAnyStaticSig(ECMAScriptObjects hostObject, boolean isConstructor, Parameter... parameters) {
        addSig(hostObject, new NativeFunctionSignatureChecker.SimpleSignature(isConstructor, None, parameters));
    }

    /**
     * Registers the signature for a static variadic function, e.g. 'String.fromCharCode'.
     */
    private void addAnyStaticVarSig(ECMAScriptObjects hostObject, boolean isConstructor, Parameter... parameters) {
        addSig(hostObject, new NativeFunctionSignatureChecker.VarSignature(isConstructor, None, parameters));
    }

    /**
     * Registers the signature for a variadic function, e.g. 'String.prototype.concat'.
     */
    private void addVarSig(ECMAScriptObjects hostObject, ValueDescription base, Parameter... parameters) {
        addSig(hostObject, new NativeFunctionSignatureChecker.VarSignature(false, base, parameters));
    }

    /**
     * Registers the signature for a function, e.g. 'String.prototype.toString'.
     */
    private void addSig(ECMAScriptObjects hostObject, ValueDescription base, Parameter... parameters) {
        addSig(hostObject, new NativeFunctionSignatureChecker.SimpleSignature(false, base, parameters));
    }

    /**
     * Registers the signature for a function.
     */
    private void addSig(ECMAScriptObjects hostObject, Signature signature) {
        if (signatures.containsKey(hostObject)) {
            throw new AnalysisException("Attempting to redefine signature: " + hostObject);
        }
        signatures.put(hostObject, signature);
    }

    /**
     * Registers the signature for a variadic constructor function, e.g. 'Array'.
     */
    private void addConstructorVarSig(ECMAScriptObjects hostObject, Parameter... parameters) {
        addAnyStaticVarSig(hostObject, true, parameters);
    }

    /**
     * Registers the signature for a constructor function, e.g. 'Object'.
     */
    private void addConstructorSig(ECMAScriptObjects hostObject, Parameter... parameters) {
        addAnyStaticSig(hostObject, true, parameters);
    }

    /**
     * Registers the signature for a static non-constructor function, e.g. 'Object.getPrototypeOf'.
     */
    private void addStaticSig(ECMAScriptObjects hostObject, Parameter... parameters) {
        addAnyStaticSig(hostObject, false, parameters);
    }

    /**
     * Registers the signature for a static variadic non-constructor function, e.g. 'String.prototype.concat'..
     */
    private void addStaticVarSig(ECMAScriptObjects hostObject, Parameter... parameters) {
        addAnyStaticVarSig(hostObject, false, parameters);
    }

    /**
     * Builder method: creates the signatures. This could in principle be spread out across {@link dk.brics.tajs.analysis.nativeobjects.JSString} et al.
     */
    private void initializeSignatures() {
        ValueDescription none = None;

        // CONSTRUCTORS
        addConstructorSig(STRING, OptionalParameters.StringOrSymbol);
        addConstructorSig(NUMBER, OptionalParameters.Number);
        addConstructorSig(REGEXP, OptionalParameters.StringIfNotRegExpOfUndefined, OptionalParameters.StringIfNotUndefined);
        addConstructorVarSig(ARRAY, OptionalParameters.DontCare);
        addConstructorSig(OBJECT, OptionalParameters.ObjectIfNotNullUndefined);
        addConstructorVarSig(FUNCTION, OptionalParameters.String);
        addConstructorSig(BOOLEAN, OptionalParameters.Boolean);
        addSig(DATE,
                new NativeFunctionSignatureChecker.ArityOverloadedSignature(
                        new NativeFunctionSignatureChecker.SimpleSignature(true, none, MandatoryParameters.Number /* year */, MandatoryParameters.Number /* month */, OptionalParameters.Number /* date */, OptionalParameters.Number /* hours */, OptionalParameters.Number /* minutes */, OptionalParameters.Number /* seconds */, OptionalParameters.Number /* milliseconds*/),
                        new NativeFunctionSignatureChecker.SimpleSignature(true, none, MandatoryParameters.NumberIfNotString),
                        new NativeFunctionSignatureChecker.SimpleSignature(true, none)
                )
        );
        addConstructorSig(ERROR, OptionalParameters.String);
        addConstructorSig(EVAL_ERROR, OptionalParameters.String);
        addConstructorSig(RANGE_ERROR, OptionalParameters.String);
        addConstructorSig(REFERENCE_ERROR, OptionalParameters.String);
        addConstructorSig(SYNTAX_ERROR, OptionalParameters.String);
        addConstructorSig(TYPE_ERROR, OptionalParameters.String);
        addConstructorSig(URI_ERROR, OptionalParameters.String);

        // STRING FUNCTIONS
        addStaticVarSig(STRING_FROMCHARCODE, OptionalParameters.Number);
        addStaticVarSig(STRING_FROMCODEPOINT, OptionalParameters.Number);
        addSig(STRING_TOSTRING, Receivers.String);
        addSig(STRING_VALUEOF, Receivers.String);
        addSig(STRING_CHARAT, Receivers.NotNullUndefCoerceString, MandatoryParameters.Number);
        addSig(STRING_CHARCODEAT, Receivers.NotNullUndefCoerceString, MandatoryParameters.Number);
        addVarSig(STRING_CONCAT, Receivers.NotNullUndefCoerceString, OptionalParameters.String);
        addSig(STRING_INDEXOF, Receivers.NotNullUndefCoerceString, MandatoryParameters.String, OptionalParameters.Number);
        addSig(STRING_LASTINDEXOF, Receivers.NotNullUndefCoerceString, MandatoryParameters.String, OptionalParameters.Number);
        addSig(STRING_LOCALECOMPARE, Receivers.NotNullUndefCoerceString, MandatoryParameters.String);
        addSig(STRING_MATCH, Receivers.NotNullUndefCoerceString, MandatoryParameters.RegExp);
        addSig(STRING_SEARCH, Receivers.NotNullUndefCoerceString, MandatoryParameters.RegExp);
        addSig(STRING_SLICE, Receivers.NotNullUndefCoerceString, OptionalParameters.Number, OptionalParameters.Number);
        addSig(STRING_SUBSTRING, Receivers.NotNullUndefCoerceString, OptionalParameters.Number, OptionalParameters.Number);
        addSig(STRING_SUBSTR, Receivers.NotNullUndefCoerceString, OptionalParameters.Number, OptionalParameters.Number);
        addSig(STRING_SPLIT, Receivers.NotNullUndefCoerceString, OptionalParameters.StringIfNotRegExp, OptionalParameters.Number);
        addSig(STRING_TOLOWERCASE, Receivers.NotNullUndefCoerceString);
        addSig(STRING_TOLOCALELOWERCASE, Receivers.NotNullUndefCoerceString);
        addSig(STRING_TOUPPERCASE, Receivers.NotNullUndefCoerceString);
        addSig(STRING_TOLOCALEUPPERCASE, Receivers.NotNullUndefCoerceString);
        addSig(STRING_TRIM, Receivers.NotNullUndefCoerceString);
        addSig(STRING_TRIMLEFT, Receivers.NotNullUndefCoerceString);
        addSig(STRING_TRIMRIGHT, Receivers.NotNullUndefCoerceString);
        addSig(STRING_STARTSWITH, Receivers.NotNullUndefCoerceString, MandatoryParameters.NotRegExpCoerceString, OptionalParameters.Integer);
        addSig(STRING_ENDSWITH, Receivers.NotNullUndefCoerceString, MandatoryParameters.NotRegExpCoerceString, OptionalParameters.Integer);
        addSig(STRING_INCLUDES, Receivers.NotNullUndefCoerceString, MandatoryParameters.StringThrowOnRegExp, OptionalParameters.Integer);
        addSig(STRING_CODEPOINTAT, Receivers.NotNullUndefCoerceString, MandatoryParameters.Number);

        // NUMBER FUNCTIONS
        addStaticSig(NUMBER_ISFINITE, MandatoryParameters.DontCare);
        addStaticSig(NUMBER_ISSAFEINTEGER, MandatoryParameters.DontCare);
        addStaticSig(NUMBER_ISINTEGER, MandatoryParameters.DontCare);
        addStaticSig(NUMBER_ISNAN, MandatoryParameters.DontCare);
        addSig(NUMBER_TOFIXED, Receivers.Number, OptionalParameters.Integer);
        addSig(NUMBER_TOEXPONENTIAL, Receivers.Number, OptionalParameters.Integer);
        addSig(NUMBER_TOPRECISION, Receivers.Number, OptionalParameters.Integer);
        addSig(NUMBER_TOSTRING, Receivers.Number, OptionalParameters.Integer);
        addSig(NUMBER_TOLOCALESTRING, Receivers.Number, OptionalParameters.Integer);
        addSig(NUMBER_VALUEOF, Receivers.Number);

        // REGEXP FUNCTIONS
        addSig(REGEXP_COMPILE, Receivers.RegExp, OptionalParameters.String /* close enough for very deprecated API */, OptionalParameters.StringIfNotUndefined);
        addSig(REGEXP_EXEC, Receivers.RegExp, OptionalParameters.String);
        addSig(REGEXP_TEST, Receivers.RegExp, OptionalParameters.String);
        addSig(REGEXP_TOSTRING, Receivers.RegExp);

        // ARRAY FUNCTIONS
        addSig(ARRAY_INDEXOF, Receivers.CoerceObject, MandatoryParameters.DontCare, OptionalParameters.Integer);
        addSig(ARRAY_JOIN, Receivers.CoerceObject, OptionalParameters.StringIfNotUndefined);
        addSig(ARRAY_POP, Receivers.CoerceObject);
        addSig(ARRAY_REVERSE, Receivers.CoerceObject);
        addSig(ARRAY_SHIFT, Receivers.CoerceObject);
        addSig(ARRAY_SLICE, Receivers.CoerceObject, OptionalParameters.Integer, OptionalParameters.Integer);
        addSig(ARRAY_SORT, Receivers.CoerceObject, OptionalParameters.DontCare /* yep, non-functions are silently ignored! */);
        addSig(ARRAY_TOLOCALESTRING, Receivers.CoerceObject);
        addSig(ARRAY_TOSTRING, Receivers.CoerceObject);
        addSig(ARRAY_VALUES, Receivers.CoerceObject);

        addStaticSig(ARRAY_ISARRAY, MandatoryParameters.DontCare);

        addVarSig(ARRAY_CONCAT, Receivers.CoerceObject, OptionalParameters.DontCare);
        addVarSig(ARRAY_PUSH, Receivers.CoerceObject, OptionalParameters.DontCare);
        addVarSig(ARRAY_SPLICE, Receivers.CoerceObject, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.DontCare);
        addVarSig(ARRAY_UNSHIFT, Receivers.CoerceObject, OptionalParameters.DontCare);

        // OBJECT FUNCTIONS
        addSig(OBJECT_DEFINEGETTER, Receivers.DontCare, MandatoryParameters.String, MandatoryParameters.Function);
        addSig(OBJECT_DEFINESETTER, Receivers.DontCare, MandatoryParameters.String, MandatoryParameters.Function);
        addSig(OBJECT_HASOWNPROPERTY, Receivers.CoerceObject, MandatoryParameters.String);
        addSig(OBJECT_ISPROTOTYPEOF, Receivers.DontCare, MandatoryParameters.DontCare);
        addSig(OBJECT_PROPERTYISENUMERABLE, Receivers.CoerceObject, MandatoryParameters.String);
        addSig(OBJECT_TOLOCALESTRING, Receivers.DontCare);
        addSig(OBJECT_TOSTRING, Receivers.DontCare);
        addSig(OBJECT_VALUEOF, Receivers.CoerceObject);

        addStaticSig(OBJECT_CREATE, MandatoryParameters.DontCare, OptionalParameters.Object);
        addStaticSig(OBJECT_DEFINE_PROPERTIES, MandatoryParameters.ObjectNoCoercion, MandatoryParameters.Object);
        addStaticSig(OBJECT_DEFINE_PROPERTY, MandatoryParameters.ObjectNoCoercion, MandatoryParameters.StringOrSymbol, MandatoryParameters.Object);
        addStaticSig(OBJECT_FREEZE, MandatoryParameters.Object);
        addStaticSig(OBJECT_GETOWNPROPERTYDESCRIPTOR, MandatoryParameters.ObjectNoCoercion, MandatoryParameters.String);
        addStaticSig(OBJECT_GETOWNPROPERTYNAMES, MandatoryParameters.Object);
        addStaticSig(OBJECT_GETOWNPROPERTYSYMBOLS, MandatoryParameters.Object);
        addStaticSig(OBJECT_GETPROTOTYPEOF, MandatoryParameters.Object);
        addStaticSig(OBJECT_ISEXTENSIBLE, MandatoryParameters.Object);
        addStaticSig(OBJECT_ISFROZEN, MandatoryParameters.Object);
        addStaticSig(OBJECT_ISSEALED, MandatoryParameters.Object);
        addStaticSig(OBJECT_KEYS, MandatoryParameters.Object);
        addStaticSig(OBJECT_PREVENTEXTENSIONS, MandatoryParameters.Object);
        addStaticSig(OBJECT_SEAL, MandatoryParameters.Object);
        addStaticSig(OBJECT_SETPROTOTYPEOF, MandatoryParameters.Object, MandatoryParameters.DontCare);
        addStaticSig(OBJECT_IS, MandatoryParameters.Object, MandatoryParameters.DontCare);
        addStaticSig(OBJECT_ASSIGN, MandatoryParameters.Object, MandatoryParameters.DontCare);
        addStaticSig(OBJECT_VALUES, MandatoryParameters.Object);

        // FUNCTION FUNCTIONS
        addSig(FUNCTION_TOSTRING, Receivers.Function);
        addSig(FUNCTION_APPLY, Receivers.Function, OptionalParameters.DontCare, OptionalParameters.ObjectIfNotNullUndefined);

        addStaticSig(FUNCTION_PROTOTYPE);

        addVarSig(FUNCTION_CALL, Receivers.DontCare /* should be function, but that would prevent a better message from the transfer function */, OptionalParameters.DontCare, OptionalParameters.DontCare);

        // BOOLEAN FUNCTIONS
        addSig(BOOLEAN_TOSTRING, Receivers.Boolean);
        addSig(BOOLEAN_VALUEOF, Receivers.Boolean);

        // SYMBOLS FUNCTIONS
        addStaticSig(SYMBOL, OptionalParameters.String);
        addStaticSig(SYMBOL_FOR, OptionalParameters.String);
        addStaticSig(SYMBOL_KEYFOR, MandatoryParameters.Symbol);
        addSig(SYMBOL_VALUEOF, Receivers.Symbol);
        addSig(SYMBOL_TOSTRING, Receivers.Symbol);
        addSig(SYMBOL_TOSOURCE, Receivers.Symbol);

        // DATE FUNCTIONS
        addSig(DATE_GETDATE, Receivers.Date);
        addSig(DATE_GETDAY, Receivers.Date);
        addSig(DATE_GETFULLYEAR, Receivers.Date);
        addSig(DATE_GETHOURS, Receivers.Date);
        addSig(DATE_GETMILLISECONDS, Receivers.Date);
        addSig(DATE_GETMINUTES, Receivers.Date);
        addSig(DATE_GETMONTH, Receivers.Date);
        addSig(DATE_GETSECONDS, Receivers.Date);
        addSig(DATE_GETTIME, Receivers.Date);
        addSig(DATE_GETTIMEZONEOFFSET, Receivers.Date);
        addSig(DATE_GETUTCDATE, Receivers.Date);
        addSig(DATE_GETUTCDAY, Receivers.Date);
        addSig(DATE_GETUTCFULLYEAR, Receivers.Date);
        addSig(DATE_GETUTCHOURS, Receivers.Date);
        addSig(DATE_GETUTCMILLISECONDS, Receivers.Date);
        addSig(DATE_GETUTCMINUTES, Receivers.Date);
        addSig(DATE_GETUTCMONTH, Receivers.Date);
        addSig(DATE_GETUTCSECONDS, Receivers.Date);
        addSig(DATE_GETYEAR, Receivers.Date);
        addSig(DATE_SETDATE, Receivers.Date, MandatoryParameters.Integer);
        addSig(DATE_SETFULLYEAR, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETHOURS, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETMILLISECONDS, Receivers.Date, MandatoryParameters.Integer);
        addSig(DATE_SETMINUTES, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETMONTH, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETSECONDS, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETTIME, Receivers.Date, MandatoryParameters.Integer);
        addSig(DATE_SETUTCDATE, Receivers.Date, MandatoryParameters.Integer);
        addSig(DATE_SETUTCFULLYEAR, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETUTCHOURS, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETUTCMILLISECONDS, Receivers.Date, MandatoryParameters.Integer);
        addSig(DATE_SETUTCMINUTES, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETUTCMONTH, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_SETUTCSECONDS, Receivers.Date, MandatoryParameters.Integer, OptionalParameters.Integer);
        addSig(DATE_TODATESTRING, Receivers.Date);
        addSig(DATE_TOGMTSTRING, Receivers.Date);
        addSig(DATE_TOLOCALEDATESTRING, Receivers.Date);
        addSig(DATE_TOLOCALESTRING, Receivers.Date);
        addSig(DATE_TOLOCALETIMESTRING, Receivers.Date);
        addSig(DATE_TOSTRING, Receivers.Date);
        addSig(DATE_TOTIMESTRING, Receivers.Date);
        addSig(DATE_TOUTCSTRING, Receivers.Date);
        addSig(DATE_VALUEOF, Receivers.Date);

        addStaticSig(DATE_NOW);
        addStaticSig(DATE_PARSE, MandatoryParameters.String);
        addStaticSig(DATE_UTC, MandatoryParameters.Integer, MandatoryParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer, OptionalParameters.Integer);

        // ERROR FUNCTIONS
        addSig(ERROR_TOSTRING, none);

        // JSON FUNCTIONS
        addSig(JSON_PARSE, none, OptionalParameters.String);
        addSig(JSON_STRINGIFY, none, MandatoryParameters.DontCare);

        // MATH FUNCTIONS
        addStaticSig(MATH_ABS, MandatoryParameters.Number);
        addStaticSig(MATH_ACOS, MandatoryParameters.Number);
        addStaticSig(MATH_ASIN, MandatoryParameters.Number);
        addStaticSig(MATH_ATAN, MandatoryParameters.Number);
        addStaticSig(MATH_ATAN2, MandatoryParameters.Number, MandatoryParameters.Number);
        addStaticSig(MATH_CEIL, MandatoryParameters.Number);
        addStaticSig(MATH_COS, MandatoryParameters.Number);
        addStaticSig(MATH_EXP, MandatoryParameters.Number);
        addStaticSig(MATH_FLOOR, MandatoryParameters.Number);
        addStaticSig(MATH_LOG, MandatoryParameters.Number);
        addStaticSig(MATH_POW, MandatoryParameters.Number, MandatoryParameters.Number);
        addStaticSig(MATH_RANDOM);
        addStaticSig(MATH_ROUND, MandatoryParameters.Number);
        addStaticSig(MATH_SIN, MandatoryParameters.Number);
        addStaticSig(MATH_SQRT, MandatoryParameters.Number);
        addStaticSig(MATH_TAN, MandatoryParameters.Number);
        addStaticSig(MATH_IMUL, MandatoryParameters.Number);
        addStaticSig(MATH_SIGN, MandatoryParameters.Number);
        addStaticSig(MATH_TRUNC, MandatoryParameters.Number);
        addStaticSig(MATH_TANH, MandatoryParameters.Number);
        addStaticSig(MATH_ASINH, MandatoryParameters.Number);
        addStaticSig(MATH_ACOSH, MandatoryParameters.Number);
        addStaticSig(MATH_ATANH, MandatoryParameters.Number);
        addStaticSig(MATH_HYPOT, MandatoryParameters.Number);
        addStaticSig(MATH_FROUND, MandatoryParameters.Number);
        addStaticSig(MATH_CLZ32, MandatoryParameters.Number);
        addStaticSig(MATH_CBRT, MandatoryParameters.Number);
        addStaticSig(MATH_SINH, MandatoryParameters.Number);
        addStaticSig(MATH_COSH, MandatoryParameters.Number);
        addStaticSig(MATH_LOG10, MandatoryParameters.Number);
        addStaticSig(MATH_LOG2, MandatoryParameters.Number);
        addStaticSig(MATH_LOG1P, MandatoryParameters.Number);
        addStaticSig(MATH_EXPM1, MandatoryParameters.Number);

        addStaticVarSig(MATH_MAX, OptionalParameters.Number);
        addStaticVarSig(MATH_MIN, OptionalParameters.Number);

        // GLOBAL FUNCTIONS
        addStaticSig(ALERT, MandatoryParameters.DontCare);
        addStaticSig(DECODEURI, MandatoryParameters.String);
        addStaticSig(DECODEURICOMPONENT, MandatoryParameters.String);
        addStaticSig(ENCODEURI, MandatoryParameters.String);
        addStaticSig(ENCODEURICOMPONENT, MandatoryParameters.String);
        addStaticSig(ESCAPE, MandatoryParameters.String);
        addStaticSig(EVAL, MandatoryParameters.DontCare);
        addStaticSig(ISFINITE, MandatoryParameters.Number);
        addStaticSig(ISNAN, MandatoryParameters.Number);
        addStaticSig(PARSEFLOAT, MandatoryParameters.String);
        addStaticSig(PARSEINT, MandatoryParameters.String, OptionalParameters.Integer);
        addStaticSig(PRINT, MandatoryParameters.DontCare);
        addStaticSig(UNESCAPE, MandatoryParameters.String);

        // PROXY SIGNATURES
        addConstructorSig(PROXY, MandatoryParameters.Object, MandatoryParameters.Object);
    }

    public Map<HostObject, Signature> getSignatures() {
        return signatures;
    }
}
