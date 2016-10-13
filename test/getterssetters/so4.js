var foo = {
    _bar: 123,
    value: function (name, value) {
        if (arguments.length < 2) {
            return this['_' + name];
        }
        this['_' + name] = value;
    }
};
var gaz = foo._bar;
TAJS_assert(gaz === 123);
foo.value('bar', 456);
var gaz = foo.value('bar');
gaz.KILL_UNDEFINED;
TAJS_assert(gaz, 'isMaybeNumUInt');