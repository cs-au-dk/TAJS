function F() {}
F.prototype.a = 42;

var x = new F;
assert(x.a === 42);

x.a = 7;
assert(x.a === 7);
assert(F.prototype.a === 42);
assert(F.prototype.toString !== undefined);

F.prototype.toString = 123;
assert(F.prototype.toString === 123);
dumpValue("done");