// Number Objects
// 15.7.1 the Number Constructor Called as a Function
var a = Number();
var b = Number(42.66);
var c = Number(true);
TAJS_dumpValue(a);
TAJS_dumpValue(b);
TAJS_dumpValue(c);
TAJS_assert(a == 0);
TAJS_assert(b == 42.66);
TAJS_assert(c == 1);

// 15.7.2
var d = new Number();
var e = new Number(47.11);
var f = new Number(true);
TAJS_dumpValue(d);
TAJS_dumpValue(d.valueOf());
TAJS_dumpValue(e);
TAJS_dumpValue(e.valueOf());
TAJS_dumpValue(f);
TAJS_dumpValue(f.valueOf());

// 15.7.3.1-6
TAJS_dumpValue(Number.prototype.valueOf());
TAJS_dumpValue(Number.MAX_VALUE);
TAJS_dumpValue(Number.MIN_VALUE);
TAJS_dumpValue(Number.NaN);
TAJS_dumpValue(Number.NEGATIVE_INFINITY);
TAJS_dumpValue(Number.POSITIVE_INFINITY);

// 15.7.4
TAJS_dumpValue(e.toString());
TAJS_dumpValue(b.toString());
TAJS_dumpValue(b.valueOf());
TAJS_dumpValue(b.toExponential(1));
TAJS_dumpValue(e.toFixed());
TAJS_dumpValue(e.toExponential(4));
TAJS_dumpValue(e.toPrecision(10));
