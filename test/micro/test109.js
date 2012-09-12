function f() { return 42}
assert(f.call.call.call(f) === 42)
assert(f.apply.apply.apply(f) === 42)
assert(Function.prototype.apply.apply(f) == 42)
dumpValue(f.apply.apply.apply(f))

function g(x) { return x + 1}
assert(Function.prototype.call(g,45) === undefined)
assert(isNaN(g.call.call(g,45)))
dumpValue(g.call.call(g,45))
assert(Function.prototype.call.call(g,null,87) == 88);
dumpValue(Function.prototype.call.call(g,null,87));