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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.lattice.HostObject;

/**
 * Native ECMAScript object descriptors.
 */
public enum ECMAScriptObjects implements HostObject {

    GLOBAL("<the global object>"),

    OBJECT("Object"),
    OBJECT_DEFINE_PROPERTY("Object.defineProperty"),
    OBJECT_CREATE("Object.create"),
    OBJECT_DEFINE_PROPERTIES("Object.defineProperties"),
    OBJECT_FREEZE("Object.freeze"),
    OBJECT_GETOWNPROPERTYDESCRIPTOR("Object.getOwnPropertyDescriptor"),
    OBJECT_GETOWNPROPERTYNAMES("Object.getOwnPropertyNames"),
    OBJECT_GETPROTOTYPEOF("Object.getPrototypeOf"),
    OBJECT_SETPROTOTYPEOF("Object.setPrototypeOf"),
    OBJECT_ISEXTENSIBLE("Object.isExtensible"),
    OBJECT_ISFROZEN("Object.isFrozen"),
    OBJECT_ISSEALED("Object.isSealed"),
    OBJECT_KEYS("Object.keys"),
    OBJECT_PREVENTEXTENSIONS("Object.preventExtensions"),
    OBJECT_SEAL("Object.seal"),
    OBJECT_IS("Object.is"),
    OBJECT_ASSIGN("Object.assign"),
    OBJECT_GETOWNPROPERTYSYMBOLS("Object.getOwnPropertySymbols"),
    OBJECT_VALUES("Object.values"),

    OBJECT_PROTOTYPE("Object.prototype"),
    OBJECT_TOSTRING("Object.prototype.toString"),
    OBJECT_TOLOCALESTRING("Object.prototype.toLocaleString"),
    OBJECT_VALUEOF("Object.prototype.valueOf"),
    OBJECT_HASOWNPROPERTY("Object.prototype.hasOwnProperty"),
    OBJECT_ISPROTOTYPEOF("Object.prototype.isPrototypeOf"),
    OBJECT_PROPERTYISENUMERABLE("Object.prototype.propertyIsEnumerable"),
    OBJECT_DEFINEGETTER("Object.prototype.__defineGetter__"),
    OBJECT_DEFINESETTER("Object.prototype.__defineSetter__"),

    FUNCTION("Function"),
    FUNCTION_PROTOTYPE("Function.prototype"),
    FUNCTION_TOSTRING("Function.prototype.toString"),
    FUNCTION_APPLY("Function.prototype.apply"),
    FUNCTION_CALL("Function.prototype.call"),

    ARRAY("Array"),
    ARRAY_ISARRAY("Array.isArray"),
    ARRAY_PROTOTYPE("Array.prototype"),
    ARRAY_TOSTRING("Array.prototype.toString"),
    ARRAY_TOLOCALESTRING("Array.prototype.toLocaleString"),
    ARRAY_CONCAT("Array.prototype.concat"),
    ARRAY_JOIN("Array.prototype.join"),
    ARRAY_POP("Array.prototype.pop"),
    ARRAY_PUSH("Array.prototype.push"),
    ARRAY_REVERSE("Array.prototype.reverse"),
    ARRAY_SHIFT("Array.prototype.shift"),
    ARRAY_SLICE("Array.prototype.slice"),
    ARRAY_SORT("Array.prototype.sort"),
    ARRAY_SPLICE("Array.prototype.splice"),
    ARRAY_UNSHIFT("Array.prototype.unshift"),
    ARRAY_INDEXOF("Array.prototype.indexOf"),
    ARRAY_VALUES("Array.prototype.values"),

    STRING("String"),
    STRING_PROTOTYPE("String.prototype"),
    STRING_FROMCHARCODE("String.fromCharCode"),
    STRING_FROMCODEPOINT("String.fromCodePoint"),
    STRING_TOSTRING("String.prototype.toString"),
    STRING_VALUEOF("String.prototype.valueOf"),
    STRING_CHARAT("String.prototype.charAt"),
    STRING_CHARCODEAT("String.prototype.charCodeAt"),
    STRING_CONCAT("String.prototype.concat"),
    STRING_INDEXOF("String.prototype.indexOf"),
    STRING_LASTINDEXOF("String.prototype.lastIndexOf"),
    STRING_LOCALECOMPARE("String.prototype.localeCompare"),
    STRING_MATCH("String.prototype.match"),
    STRING_SEARCH("String.prototype.search"),
    STRING_SLICE("String.prototype.slice"),
    STRING_SPLIT("String.prototype.split"),
    STRING_SUBSTR("String.prototype.substr"),
    STRING_SUBSTRING("String.prototype.substring"),
    STRING_TOLOWERCASE("String.prototype.toLowerCase"),
    STRING_TOLOCALELOWERCASE("String.prototype.toLocaleLowerCase"),
    STRING_TOUPPERCASE("String.prototype.toUpperCase"),
    STRING_TOLOCALEUPPERCASE("String.prototype.toLocaleUpperCase"),
    STRING_TRIM("String.prototype.trim"),
    STRING_TRIMLEFT("String.prototype.trimLeft"),
    STRING_TRIMRIGHT("String.prototype.trimRight"),
    STRING_STARTSWITH("String.prototype.startsWith"),
    STRING_ENDSWITH("String.prototype.endsWith"),
    STRING_INCLUDES("String.prototype.includes"),
    STRING_CODEPOINTAT("String.prototype.codePointAt"),

    BOOLEAN("Boolean"),
    BOOLEAN_PROTOTYPE("Boolean.prototype"),
    BOOLEAN_TOSTRING("Boolean.prototype.toString"),
    BOOLEAN_VALUEOF("Boolean.prototype.valueOf"),

    NUMBER("Number"),
    NUMBER_PROTOTYPE("Number.prototype"),
    NUMBER_TOSTRING("Number.prototype.toString"),
    NUMBER_TOLOCALESTRING("Number.prototype.toLocaleString"),
    NUMBER_VALUEOF("Number.prototype.valueOf"),
    NUMBER_TOFIXED("Number.prototype.toFixed"),
    NUMBER_TOEXPONENTIAL("Number.prototype.toExponential"),
    NUMBER_TOPRECISION("Number.prototype.toPrecision"),
    NUMBER_ISFINITE("Number.isFinite"),
    NUMBER_ISSAFEINTEGER("Number.isSafeInteger"),
    NUMBER_ISINTEGER("Number.isInteger"),
    NUMBER_ISNAN("Number.isNan"),

    MATH("Math"),
    MATH_MAX("Math.max"),
    MATH_MIN("Math.min"),
    MATH_POW("Math.pow"),
    MATH_RANDOM("Math.random"),
    MATH_ROUND("Math.round"),
    MATH_SIN("Math.sin"),
    MATH_SQRT("Math.sqrt"),
    MATH_TAN("Math.tan"),
    MATH_ABS("Math.abs"),
    MATH_ACOS("Math.acos"),
    MATH_ASIN("Math.asin"),
    MATH_ATAN("Math.atan"),
    MATH_ATAN2("Math.atan2"),
    MATH_CEIL("Math.ceil"),
    MATH_COS("Math.cos"),
    MATH_EXP("Math.exp"),
    MATH_FLOOR("Math.floor"),
    MATH_LOG("Math.log"),
    MATH_IMUL("Math.imul"),
    MATH_SIGN("Math.sign"),
    MATH_TRUNC("Math.trunc"),
    MATH_TANH("Math.tanh"),
    MATH_ASINH("Math.asinh"),
    MATH_ACOSH("Math.acosh"),
    MATH_ATANH("Math.atanh"),
    MATH_HYPOT("Math.hypot"),
    MATH_FROUND("Math.fround"),
    MATH_CLZ32("Math.clz32"),
    MATH_CBRT("Math.cbrt"),
    MATH_SINH("Math.sinh"),
    MATH_COSH("Math.cosh"),
    MATH_LOG10("Math.log10"),
    MATH_LOG2("Math.log2"),
    MATH_LOG1P("Math.log1p"),
    MATH_EXPM1("Math.expm1"),

    DATE("Date"),
    DATE_PARSE("Date.parse"),
    DATE_UTC("Date.UTC"),
    DATE_PROTOTYPE("Date.prototype"),
    DATE_TOSTRING("Date.prototype.toString"),
    DATE_TODATESTRING("Date.prototype.toDateString"),
    DATE_TOTIMESTRING("Date.prototype.toTimeString"),
    DATE_TOLOCALESTRING("Date.prototype.toLocaleString"),
    DATE_TOLOCALEDATESTRING("Date.prototype.toLocaleDateString"),
    DATE_TOLOCALETIMESTRING("Date.prototype.toLocaleTimeString"),
    DATE_VALUEOF("Date.prototype.valueOf"),
    DATE_GETTIME("Date.prototype.getTime"),
    DATE_GETFULLYEAR("Date.prototype.getFullYear"),
    DATE_GETUTCFULLYEAR("Date.prototype.getUTCFullYear"),
    DATE_GETMONTH("Date.prototype.getMonth"),
    DATE_GETUTCMONTH("Date.prototype.getUTCMonth"),
    DATE_GETDATE("Date.prototype.getDate"),
    DATE_GETUTCDATE("Date.prototype.getUTCDate"),
    DATE_GETDAY("Date.prototype.getDay"),
    DATE_GETUTCDAY("Date.prototype.getUTCDay"),
    DATE_GETHOURS("Date.prototype.getHours"),
    DATE_GETUTCHOURS("Date.prototype.getUTCHours"),
    DATE_GETMINUTES("Date.prototype.getMinutes"),
    DATE_GETUTCMINUTES("Date.prototype.getUTCMinutes"),
    DATE_GETSECONDS("Date.prototype.getSeconds"),
    DATE_GETUTCSECONDS("Date.prototype.getUTCSeconds"),
    DATE_GETMILLISECONDS("Date.prototype.getMilliseconds"),
    DATE_GETUTCMILLISECONDS("Date.prototype.getUTCMilliseconds"),
    DATE_GETTIMEZONEOFFSET("Date.prototype.getTimezoneOffset"),
    DATE_NOW("Date.now"),
    DATE_SETTIME("Date.prototype.setTime"),
    DATE_SETMILLISECONDS("Date.prototype.setMilliseconds"),
    DATE_SETUTCMILLISECONDS("Date.prototype.setUTCMilliseconds"),
    DATE_SETSECONDS("Date.prototype.setSeconds"),
    DATE_SETUTCSECONDS("Date.prototype.setUTCSeconds"),
    DATE_SETMINUTES("Date.prototype.setMinutes"),
    DATE_SETUTCMINUTES("Date.prototype.setUTCMinutes"),
    DATE_SETHOURS("Date.prototype.setHours"),
    DATE_SETUTCHOURS("Date.prototype.setUTCHours"),
    DATE_SETDATE("Date.prototype.setDate"),
    DATE_SETUTCDATE("Date.prototype.setUTCDate"),
    DATE_SETMONTH("Date.prototype.setMonth"),
    DATE_SETUTCMONTH("Date.prototype.setUTCMonth"),
    DATE_SETFULLYEAR("Date.prototype.setFullYear"),
    DATE_SETUTCFULLYEAR("Date.prototype.setUTCFullYear"),
    DATE_TOJSON("Date.prototype.toJSON"),
    DATE_TOUTCSTRING("Date.prototype.toUTCString"),
    DATE_GETYEAR("Date.prototype.getYear"),
    DATE_SETYEAR("Date.prototype.setYear"),
    DATE_TOGMTSTRING("Date.prototype.toGMTString"),

    PROXY("Proxy"),
    PROXY_PROTOTYPE("Proxy.prototype"),
    PROXY_TOSTRING("Proxy.prototype.toString"),

    REGEXP("RegExp"),
    REGEXP_PROTOTYPE("RegExp.prototype"),
    REGEXP_COMPILE("RegExp.prototype.compile"),
    REGEXP_EXEC("RegExp.prototype.exec"),
    REGEXP_LASTINDEX("RegExp.prototype.lastIndex"),
    REGEXP_TEST("RegExp.prototype.test"),
    REGEXP_TOSTRING("RegExp.prototype.toString"),

    SYMBOL("Symbol"),
    SYMBOL_INSTANCES("Symbol instances"), // TODO: what is this? and why the name "Symbol instances"? - github #512
    SYMBOL_PROTOTYPE("Symbol.prototype"),
    SYMBOL_TOSTRING("Symbol.prototype.toString"),
    SYMBOL_TOSOURCE("Symbol.prototype.toSource"),
    SYMBOL_VALUEOF("Symbol.prototype.valueOf"),
    SYMBOL_PROTOTYPE_TOPRIMITIVE("Symbol.prototype[@@toPrimitive]"),
    SYMBOL_FOR("Symbol.for"),
    SYMBOL_KEYFOR("Symbol.keyFor"),
    SYMBOL_HAS_INSTANCE("Symbol.hasInstance"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_IS_CONCAT_SPREADABLE("Symbol.isConcatSpreadable"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_ITERATOR("Symbol.iterator"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_MATCH("Symbol.match"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_REPLACE("Symbol.replace"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_SEARCH("Symbol.search"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_SPECIES("Symbol.species"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_SPLIT("Symbol.split"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_TO_PRIMITIVE("Symbol.toPrimitive"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_TO_STRING_TAG("Symbol.toStringTag"), // TODO: not yet wired to the native functions - github #511
    SYMBOL_UNSCOPABLES("Symbol.unscopables"), // TODO: not yet wired to the native functions - github #511

    ERROR("Error"),
    ERROR_PROTOTYPE("Error.prototype"),
    ERROR_CAPTURESTACKTRACE("Error.captureStackTrace"),
    ERROR_TOSTRING("Error.toString"),
    EVAL_ERROR("EvalError"),
    EVAL_ERROR_PROTOTYPE("EvalError.prototype"),
    RANGE_ERROR("RangeError"),
    RANGE_ERROR_PROTOTYPE("RangeError.prototype"),
    REFERENCE_ERROR("ReferenceError"),
    REFERENCE_ERROR_PROTOTYPE("ReferenceError.prototype"),
    SYNTAX_ERROR("SyntaxError"),
    SYNTAX_ERROR_PROTOTYPE("SyntaxError.prototype"),
    TYPE_ERROR("TypeError"),
    TYPE_ERROR_PROTOTYPE("TypeError.prototype"),
    URI_ERROR("URIError"),
    URI_ERROR_PROTOTYPE("URIError.prototype"),

    JSON("JSON"),
    JSON_OBJECT("JSONObject"),
    JSON_PARSE("JSON.parse"),
    JSON_STRINGIFY("JSON.stringify"),

    EVAL("eval"),
    PARSEINT("parseInt"),
    PARSEFLOAT("parseFloat"),
    ISNAN("isNaN"),
    ISFINITE("isFinite"),
    DECODEURI("decodeURI"),
    DECODEURICOMPONENT("decodeURIComponent"),
    ENCODEURI("encodeURI"),
    ENCODEURICOMPONENT("encodeURIComponent"),
    PRINT("print"), // nonstandard
    ALERT("alert"), // nonstandard
    ESCAPE("escape"),
    UNESCAPE("unescape"),

    ;

    private HostAPIs api;

    private String string;

    ECMAScriptObjects(String str) {
        api = HostAPIs.ECMASCRIPT_NATIVE;
        string = str;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public HostAPIs getAPI() {
        return api;
    }
}
