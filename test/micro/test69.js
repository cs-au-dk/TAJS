var x = "123";
var y = +x;
dumpValue(y);
assert(y === 123.0);
assert(y !== "123");
assert(y == "123");
