var x = 1;

var t1 = -x;
var t2 = +x;
var t3 = ~x;
var t4 = !x;

dumpValue(t1);
dumpValue(t2);
dumpValue(t3);
dumpValue(t4);

assert(t1 === -1);
assert(t2 === 1);
assert(t3 === -2);
assert(t4 === false);
