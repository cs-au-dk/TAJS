var _ = {}

var obj = {foo: function() {}, bar: function() {}};

var fun = function(name) {
    _[name] = obj[name];
};

var arr = ["foo", "bar"].sort();

TAJS_dumpObject(_);
fun(arr[0]);
fun(arr[1]);
TAJS_assert(_.foo, 'isMaybeObject');
TAJS_assert(_.bar, 'isMaybeObject');
TAJS_assert(_.baz, 'isMaybeUndef');
TAJS_dumpObject(_);

var expected = TAJS_join(obj.foo, undefined);
TAJS_assertEquals(expected, _.foo);

var expected2 = TAJS_join(obj.bar, undefined);
TAJS_assertEquals(expected2, _.bar);

