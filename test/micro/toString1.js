var m = 0;
var x = {toString: function() {m++; return "bar";}}
var t = "foo" + x;
TAJS_dumpValue(t);
TAJS_dumpValue(m);
TAJS_assert(t === "foobar");
TAJS_assert(m === 1);

m = 0;
var t2 = "foo" + x;
TAJS_dumpValue(t2);
TAJS_dumpValue(m);
TAJS_assert(t === "foobar");
TAJS_assert(m === 1);
