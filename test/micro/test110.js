var x = {a:42}
var y = {b:x}
y.b.a += 1;
TAJS_dumpValue(y.b.a);
x.a += 1;
TAJS_dumpValue(y.b.a);
var z = Math.random() ? null : x;
TAJS_dumpValue(z);
z.a += 1;
TAJS_dumpValue(x.a);
