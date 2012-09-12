var x = {a:42}
var y = {b:x}
y.b.a += 1;
dumpValue(y.b.a);
x.a += 1;
dumpValue(y.b.a);
var z = Math.random() ? null : x;
dumpValue(z);
z.a += 1;
dumpValue(x.a);
