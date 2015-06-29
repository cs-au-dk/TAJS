var x = {}
var y = x instanceof Object;
var z = x instanceof Function;

TAJS_dumpValue(y);
TAJS_dumpValue(z);

TAJS_assert(y);
TAJS_assert(!z);
