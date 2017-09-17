var foo = {
    _bar: 123,
    get: function (name) {
        return this['_' + name];
    },
    set: function (name, value) {
        this['_' + name] = value;
    }
};
var gaz = foo._bar;
TAJS_assert(gaz === 123);
foo.set('bar', 456);
var gaz = foo.get('bar');
TAJS_assert(gaz === 456);
