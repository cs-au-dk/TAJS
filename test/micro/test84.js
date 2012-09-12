var q = {a:5};
q.a++;
dumpValue(q.a);
assert(q.a == 6);

var x = {a:6}
x.a ^= 42;
assert(x.a == 44);
