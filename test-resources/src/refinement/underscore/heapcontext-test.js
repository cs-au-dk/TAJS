var _ = {};

var obj = {foo: function() { return 1; }, bar: function() { return 2; }};

var fun = function(name) {
    var func = obj[name]
    _[name] = function() {
        return func.apply(_, []);
    };
};

var arr = ["foo", "bar"].sort();

fun(arr[0]);
fun(arr[1]);

TAJS_assertEquals(1, _.foo());
// TAJS_assertEquals(2, _.bar());

