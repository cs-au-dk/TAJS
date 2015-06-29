var q = {a:5};
q.a++;
TAJS_dumpValue(q.a);
TAJS_assert(q.a == 6);

var x = {a:6}
x.a ^= 42;
TAJS_assert(x.a == 44);
