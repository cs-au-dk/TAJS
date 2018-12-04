var x = "123";
var y = +x;
TAJS_dumpValue(y);
TAJS_assert(y === 123.0);
TAJS_assert(y !== "123");
TAJS_assert(y == "123");
