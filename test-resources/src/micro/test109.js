function f() { return 42}
TAJS_assert(f.call.call.call(f) === 42)
TAJS_assert(f.apply.apply.apply(f) === 42)
TAJS_assert(Function.prototype.apply.apply(f) == 42)
TAJS_dumpValue(f.apply.apply.apply(f))

function g(x) { return x + 1}
TAJS_assert(Function.prototype.call(g,45) === undefined)
TAJS_assert(isNaN(g.call.call(g,45)))
TAJS_dumpValue(g.call.call(g,45))
TAJS_assert(Function.prototype.call.call(g, null, 87), 'isMaybeNumUInt||isMaybeNaN');
TAJS_dumpValue(Function.prototype.call.call(g,null,87));