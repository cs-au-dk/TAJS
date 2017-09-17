var a = new Boolean();
TAJS_dumpObject(a);
var b = new Boolean(36984);
TAJS_dumpObject(b);
TAJS_dumpValue(b.xy);
var c = new Boolean(true);
TAJS_dumpObject(c);
var d = Boolean("false");
TAJS_dumpValue(d);
var e = Boolean("");
TAJS_dumpValue(e);
var f = b.toString();
TAJS_dumpValue(f);
var g = c.valueOf();
TAJS_dumpValue(g)
var h = new Number(16384);

h.booString = a.toString;
h.booString();
h.valuOf = a.valueOf;
h.valuOf();

b.boo = a.toString;
var i = b.boo();
TAJS_dumpValue(i);

