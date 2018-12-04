function F() {}
F.prototype.a = 42;

var x = new F;
TAJS_assert(x.a === 42);

x.a = 7;
TAJS_assert(x.a === 7);
TAJS_assert(F.prototype.a === 42);
TAJS_assert(F.prototype.toString !== undefined);

F.prototype.toString = 123;
TAJS_assert(F.prototype.toString === 123);
TAJS_dumpValue("done");