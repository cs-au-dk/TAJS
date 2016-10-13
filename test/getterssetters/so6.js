var foo = {_bar: 123}
foo.__defineGetter__('bar', function () {
    return this._bar;
});
foo.__defineSetter__('bar', function (value) {
    this._bar = value;
});
var gaz = foo.bar;
TAJS_assert(gaz === 123);
foo.bar = 456;
var gaz = foo._bar;
TAJS_assert(gaz === 456);
