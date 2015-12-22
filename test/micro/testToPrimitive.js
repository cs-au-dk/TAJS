var a = TAJS_conversionToPrimitive(null);
TAJS_assert(a === null);
var b = TAJS_conversionToPrimitive(undefined);
TAJS_assert(b === undefined);
var c = TAJS_conversionToPrimitive(true);
TAJS_assert(c === true);
var d = TAJS_conversionToPrimitive(47.11);
TAJS_assert(d === 47.11);
var e = TAJS_conversionToPrimitive("use it or lose it");
TAJS_assert(e === "use it or lose it");
var oo = {};

var f = TAJS_conversionToPrimitive(oo);
TAJS_assert(f === '[object Object]');
TAJS_assert(TAJS_conversionToPrimitive(oo, "NUM") === '[object Object]');
TAJS_assert(TAJS_conversionToPrimitive(oo, "STR") === '[object Object]');

TAJS_assert(TAJS_conversionToPrimitive(Math, "NUM") === '[object Math]');
TAJS_assert(TAJS_conversionToPrimitive(Math, "STR") === '[object Math]');

// TODO increase precision of RegExp.prototype.toString
TAJS_assert(TAJS_conversionToPrimitive(/xy*/, "NUM"), 'isMaybeAnyStr');
TAJS_assert(TAJS_conversionToPrimitive(/xy*/, "STR"), 'isMaybeAnyStr');

var nn = new Number(42.001);
var g = TAJS_conversionToPrimitive(nn);
TAJS_assert(g === 42.001);
TAJS_assert(TAJS_conversionToPrimitive(nn, "NUM") === 42.001);
TAJS_assert(TAJS_conversionToPrimitive(nn, "STR") === "42.001");

var bb = new Boolean(false);
TAJS_assert(TAJS_conversionToPrimitive(bb) === false);
TAJS_assert(TAJS_conversionToPrimitive(bb, "NUM") === false);
TAJS_assert(TAJS_conversionToPrimitive(bb, "STR") === "false");

var bn = Math.random() ? nn : bb;
TAJS_assert(TAJS_conversionToPrimitive(bn), 'isMaybeSingleNum||isMaybeFalseButNotTrue');
TAJS_assert(TAJS_conversionToPrimitive(bn, "NUM"), 'isMaybeSingleNum||isMaybeFalseButNotTrue');
TAJS_assert(TAJS_conversionToPrimitive(bn, "STR"), 'isMaybeStrOther||isMaybeStrIdentifierParts');
