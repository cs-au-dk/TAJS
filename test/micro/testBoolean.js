var a = new Boolean();
dumpObject(a);
var b = new Boolean(36984);
dumpObject(b);
dumpValue(b.xy);
var c = new Boolean(true);
dumpObject(c);
var d = Boolean("false");
dumpValue(d);
var e = Boolean("");
dumpValue(e);
var f = b.toString();
dumpValue(f);
var g = c.valueOf();
dumpValue(g)
var h = new Number(16384);

h.booString = a.toString;
h.booString();
h.valuOf = a.valueOf;
h.valuOf();

b.boo = a.toString;
var i = b.boo();
dumpValue(i);

