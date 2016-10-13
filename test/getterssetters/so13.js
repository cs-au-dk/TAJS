var foo = {foo123bar: true};
var p = "foo" + Math.random() + "bar";
Object.defineProperty(foo, p, {
    get: function () {
        return this.foo123bar; // Uncaught RangeError: Maximum call stack size exceeded
    }
});
var ww;
try {
    var t1 = foo.foo123bar; // either returns true or RangeError exception
    TAJS_dumpValue(t1);
    ww = t1;
    TAJS_assert(t1);
} catch (e) {
    ww = p;
    TAJS_dumpObject(e);
}
TAJS_assert(ww, "isMaybeTrueButNotFalse || isMaybeStrIdentifierParts || isMaybeStrOther");
