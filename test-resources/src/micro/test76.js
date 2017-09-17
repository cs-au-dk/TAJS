var x = 1;

var t1 = -x;
var t2 = +x;
var t3 = ~x;
var t4 = !x;

TAJS_dumpValue(t1);
TAJS_dumpValue(t2);
TAJS_dumpValue(t3);
TAJS_dumpValue(t4);

TAJS_assert(t1 === -1);
TAJS_assert(t2 === 1);
TAJS_assert(t3 === -2);
TAJS_assert(t4 === false);
