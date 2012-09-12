// Number Objects
// 15.7.1 the Number Constructor Called as a Function
var a = Number();
var b = Number(42.66);
var c = Number(true);
dumpValue(a);
dumpValue(b);
dumpValue(c);
assert(a == 0);
assert(b == 42.66);
assert(c == 1);

// 15.7.2
var d = new Number();
var e = new Number(47.11);
var f = new Number(true);
dumpValue(d);
dumpValue(d.valueOf());
dumpValue(e);
dumpValue(e.valueOf());
dumpValue(f);
dumpValue(f.valueOf());

// 15.7.3.1-6
dumpValue(Number.prototype.valueOf());
dumpValue(Number.MAX_VALUE);
dumpValue(Number.MIN_VALUE);
dumpValue(Number.NaN);
dumpValue(Number.NEGATIVE_INFINITY);
dumpValue(Number.POSITIVE_INFINITY);

// 15.7.4
dumpValue(e.toString());
dumpValue(b.toString());
dumpValue(b.valueOf());
dumpValue(b.toExponential(1));
dumpValue(e.toFixed());
dumpValue(e.toExponential(4));
dumpValue(e.toPrecision(10));
