var foo = {barr: 123};
var barr = "WRONG";
Object.defineProperty(foo, 'bar', {
    get: function () {
        TAJS_dumpValue(this)
        return this.baz;
    }
});
Object.defineProperty(foo, 'baz', {
    get: function () {
        TAJS_dumpValue(this)
        return this.barr;
    }
});
var gaz = foo.bar;
TAJS_dumpValue(gaz);
TAJS_assert(gaz === 123);
