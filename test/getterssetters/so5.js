var foo = {_bar: 123};
Object.defineProperty(foo, 'bar', {
    get: function () {
        return this._bar;
    },
    set: function (value) {
        this._bar = value;
    }
});
var gaz = foo.bar;
TAJS_assert(gaz === 123);
foo.bar = 456;
var gaz = foo.bar;
TAJS_assert(gaz === 456);
