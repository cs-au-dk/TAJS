var foo = {bar: 123};
Object.defineProperty(foo, 'bar', {
    get: function () {
        TAJS_dumpValue(this)
        return this.bar; // RangeError: Maximum call stack size exceeded
    }
});
var gaz = foo.bar;
TAJS_dumpValue(gaz);
TAJS_assert(false);
