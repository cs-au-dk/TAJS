var foo = {baz123: false}
var p = "baz" + Math.random();
var q = p;
foo.__defineGetter__(p, function () {
    return p;
});
foo.__defineSetter__(p, function (value) {
    q = value;
});
TAJS_dumpObject(foo);
var t1 = foo.baz123;
TAJS_dumpValue(t1);
TAJS_assert(t1, "isMaybeAnyStr || isMaybeFalseButNotTrue")
foo.baz123 = true;
TAJS_dumpValue(q);
TAJS_assert(q, "isMaybeAnyStr || isMaybeTrueButNotFalse")