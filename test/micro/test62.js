var x = {}
var y = x instanceof Object;
var z = x instanceof Function;

dumpValue(y);
dumpValue(z);

assert(y);
assert(!z);
